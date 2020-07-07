package com.math4.user.mathplace;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.math4.user.mathplace.Settings.timeNotify;

public class LogIn extends AppCompatActivity {

    EditText emailLogIn,passwordLogIn;
    private FirebaseAuth mAuth;
    EditText Name,Surname;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        emailLogIn=findViewById(R.id.emailLogin);
        passwordLogIn=findViewById(R.id.passwordLogin);
        Name=findViewById(R.id.Name);
//        Surname=findViewById(R.id.Surname);

        TextView textView = findViewById(R.id.textViewPolicy); //находим TextView
//Экранируем кавычки в атрибуте html тега слэшем:
        String textWithLink = "Регистрируясь, Вы соглашаетесь с "+"<a href=\"http://ledokolpro.tilda.ws/policy\">Политикой конфиденциальности</a>"+" и "+"<a href=\"http://ledokolpro.tilda.ws/rules\">Условиями пользования</a>";
//Указываем с помощью Html.fromHtml, что у нас не просто текст:
        textView.setText(Html.fromHtml(textWithLink, null, null));
////Указываем что разрешаем ссылки кликать:
        textView.setLinksClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
//Научаемся отлавливать клики пропустив текст через наш класс из пред. пункта.
        CharSequence text = textView.getText();
        if (text instanceof Spannable)
        {
            textView.setText(MakeLinksClicable.reformatText(text));
        }
    }
    public  void ClickLogIn(View view){
//        Toast.makeText(getApplicationContext(),"Старт",Toast.LENGTH_LONG).show();
        Login(emailLogIn.getText().toString(),passwordLogIn.getText().toString());
    }
    public void Login(final String email,final String password){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user=mAuth.getCurrentUser();
                            FirebaseFirestore db=FirebaseFirestore.getInstance();
                            CollectionReference account=db.collection("account");
                            setTask(user,email,password);
                            Toast.makeText(getApplicationContext(),"Готово!",Toast.LENGTH_LONG).show();
//                            updateUI();
                        }else{
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                            switch (errorCode) {

                                case "ERROR_INVALID_CUSTOM_TOKEN":
                                    Toast.makeText(getApplicationContext(), "Неверный формат токена. Пожалуйста, проверьте данные", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                    Toast.makeText(getApplicationContext(), "Пользовательский токен соответствует другой аудитории.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_INVALID_CREDENTIAL":
                                    Toast.makeText(getApplicationContext(), "Предоставленные учетные данные неверны или устарели.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_INVALID_EMAIL":
                                    Toast.makeText(getApplicationContext(), "Адрес электронной почты имеет неправильный формат", Toast.LENGTH_LONG).show();
//                                    etEmail.setError("The email address is badly formatted.");
//                                    etEmail.requestFocus();
                                    break;

                                case "ERROR_WRONG_PASSWORD":
                                    Toast.makeText(getApplicationContext(), "Пароль неверен или у пользователя нет пароля.", Toast.LENGTH_LONG).show();
//                                    etPassword.setError("password is incorrect ");
//                                    etPassword.requestFocus();
//                                    etPassword.setText("");
                                    break;

                                case "ERROR_USER_MISMATCH":
                                    Toast.makeText(getApplicationContext(), "Предоставленные учетные данные не соответствуют ранее зарегистрированному пользователю.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_REQUIRES_RECENT_LOGIN":
                                    Toast.makeText(getApplicationContext(), "Эта операция чувствительна и требует недавней аутентификации. Войдите еще раз, прежде чем повторять этот запрос.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                    Toast.makeText(getApplicationContext(), "Учетная запись уже существует с тем же адресом электронной почты, но с другими учетными данными для входа. Войдите, используя аккаунт, связанного с этим адресом электронной почты.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    Toast.makeText(getApplicationContext(), "Этот адрес электронной почты уже используется другой учетной записью.", Toast.LENGTH_LONG).show();
//                                    etEmail.setError("The email address is already in use by another account.");
//                                    etEmail.requestFocus();
                                    break;

                                case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                    Toast.makeText(getApplicationContext(), "Эти учетные данные уже связаны с другой учетной записью пользователя.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_DISABLED":
                                    Toast.makeText(getApplicationContext(), "Учетная запись пользователя была отключена администратором.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_TOKEN_EXPIRED":
                                    Toast.makeText(getApplicationContext(), "Учетные данные пользователя больше не действительны. Пользователь должен снова войти в систему.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_NOT_FOUND":
                                    Toast.makeText(getApplicationContext(), "Нет записи пользователя, соответствующей этому идентификатору. Возможно, пользователь был удален.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_INVALID_USER_TOKEN":
                                    Toast.makeText(getApplicationContext(), "Учетные данные пользователя больше не действительны. Пользователь должен снова войти в систему.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_OPERATION_NOT_ALLOWED":
                                    Toast.makeText(getApplicationContext(), "Эта операция не разрешена. Вы должны включить эту службу в консоли.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_WEAK_PASSWORD":
                                    Toast.makeText(getApplicationContext(), "Пароль должен содержать минимум 6 символов", Toast.LENGTH_LONG).show();
//                                    etPassword.setError("The password is invalid it must 6 characters at least");
//                                    etPassword.requestFocus();
                                    break;

                            }
                        }
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            updateUI();
        }
    }
    public void updateUI(){
        Intent intent=new Intent(LogIn.this, Zactavka.class);
        Log.e("checkcheckVxod","VXOD GOOD");
        startActivity(intent);
    }
    public void backAuth2(View view){
        finish();
//        Intent intent=new Intent(LogIn.this,Zactavka.class);
//        startActivity(intent);
    }
    public void setTask(final FirebaseUser user,final String email,final String password) {
        final DocumentReference docRef = db.collection("account").document(user.getUid());
        final HashMap<String, Object> m3 = new HashMap<>();
        m3.put("name", Name.getText().toString());
        m3.put("bookmark", new ArrayList<>());
        m3.put("like", new ArrayList<>());
        m3.put("submit", 0);
        m3.put("right", 0);
        m3.put("notification", "18");
        m3.put("email", email);
        m3.put("money", 100);
        m3.put("ad_material",false);
        m3.put("game score", 0);
        ArrayList achiv = new ArrayList();
        achiv.add(0);
        achiv.add(0);
        achiv.add(0);
        achiv.add(0);
        achiv.add(0);
        achiv.add(0);
        achiv.add(0);
        achiv.add(0);
        restartNotify(43200000);
        m3.put("achiv", achiv);
        m3.put("lastTheme", "ОГЭ Вариант 1");
        final DocumentReference docRefUser = db.collection("task2").document("ОГЭ Вариант 1");
        docRefUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> inf = documentSnapshot.getData();
                        ArrayList arrayList=new ArrayList();
                        int allTask=((Long) inf.get("items")).intValue();
                        for(int i=0;i<allTask;i++){
                            arrayList.add(1L);
                        }
                        m3.put("ОГЭ Вариант 1",arrayList);
                        m3.put("ОГЭ Вариант 1Solution",arrayList);
                        docRef.set(m3, SetOptions.merge());
                        updateUI();
                    }
                }
            }
        });
    }



    private void restartNotify(long time) {
        Toast.makeText(getApplicationContext(),"NotifyStart",Toast.LENGTH_LONG).show();;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, TimeNotification.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                    intent, PendingIntent.FLAG_CANCEL_CURRENT);
            // На случай, если мы ранее запускали активити, а потом поменяли время,
            // откажемся от уведомления
            am.cancel(pendingIntent);
            // Устанавливаем разовое напоминание
            Date stamp = new Date();;
            long thisTime = ((long) stamp.getTime()) % ((long)86400000);
            Log.e("timeStart1",String.valueOf(stamp.getTime())+" "+String.valueOf(thisTime)+" "+String.valueOf(time));
            if(thisTime > time){
                timeNotify = ((long)stamp.getTime()) - thisTime + ((long)86400000) + time;
            }else{
                timeNotify = ((long)stamp.getTime()) - thisTime + time;
            }
            Log.e("timeFinish",String.valueOf(timeNotify));
            am.set(AlarmManager.RTC_WAKEUP, timeNotify, pendingIntent);
        }
    }
}
