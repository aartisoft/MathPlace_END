package com.math4.user.mathplace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;


public class Auth extends Fragment {
//    TextInputEditText emailAuth,passwordAuth;
    static EditText emailAuth,passwordAuth;
    private static String  finalStringTAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    Fragment[] m={new Hello1(),new Hello2(),new Hello3()};

    //  [START declare_auth]
    static FirebaseAuth mAuth;
    //  [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.activity_auth,container,false);
        mAuth=FirebaseAuth.getInstance();
        emailAuth=view.findViewById(R.id.emailAuth);
        passwordAuth=view.findViewById(R.id.passwordAuth);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("832330186047-54t7qn6t5l6maauniqhi3nc5a8p405it.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        Button buttonAuth=view.findViewById(R.id.buttonAuth);
        buttonAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(!emailAuth.getText().toString().isEmpty()&&!passwordAuth.getText().toString().isEmpty())
                        Auth(emailAuth.getText().toString(),passwordAuth.getText().toString());
                    else{
                        Snackbar.make(view,"Введите все данные",Snackbar.LENGTH_SHORT).show();
                    }
            }
        });
//        SignInButton buttonGoogle=view.findViewById(R.id.buttonGoogle);
//        buttonGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(),"Идёт загрузка",Toast.LENGTH_LONG).show();;
//                signIn();
//            }
//        });
//        TextView textView = view.findViewById(R.id.textViewPolicy); //находим TextView
////Экранируем кавычки в атрибуте html тега слэшем:
//        String textWithLink = "Регистрируясь, Вы соглашаетесь с "+"<a href=\"http://ledokolpro.tilda.ws/policy\">Политикой конфиденциальности</a>";
////Указываем с помощью Html.fromHtml, что у нас не просто текст:
//        textView.setText(Html.fromHtml(textWithLink, null, null));
//////Указываем что разрешаем ссылки кликать:
//        textView.setLinksClickable(true);
//        textView.setMovementMethod(LinkMovementMethod.getInstance());
////Научаемся отлавливать клики пропустив текст через наш класс из пред. пункта.
//        CharSequence text = textView.getText();
//        if (text instanceof Spannable)
//        {
//            textView.setText(MakeLinksClicable.reformatText(text));
//        }
        return view;
    }
    private void signIn() {
        Log.e("checkcheckAuth","Мы тут");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.e("checkcheckAuth","Закончили");
//        Toast.makeText(getActivity(),"Вы успешно вошли",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("checkcheckAuth","Мы тут");

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
//                Toast.makeText(getActivity(),"Первый",Toast.LENGTH_LONG).show();
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e("DEBUGPROBLEM", "Google sign in failed", e);
                Toast.makeText(getActivity(),"Ошибка. Попробуйте зарегистрироваться через почту",Toast.LENGTH_LONG).show();
//                Log.w("DEBUGPROBLEM", "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d("CHECKTAG", "firebaseAuthWithGoogle:" + acct.getId());
//        Toast.makeText(getActivity(),"goood",Toast.LENGTH_LONG).show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("SUCCESSTAG", "signInWithCredential:success");
                            Log.e("checkcheckAuth","firebaseAuthWithGoogle");
                            Toast.makeText(getContext(),"Загрузка...",Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            setTask(user,acct.getDisplayName());
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getActivity(),"Не получилось",Toast.LENGTH_LONG).show();
                            Log.e("FAILURETAG", "signInWithCredential:failure", task.getException());
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    public void Auth(String email,String password){
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(),"Вошёл",Toast.LENGTH_LONG).show();
                            FirebaseUser user=mAuth.getCurrentUser();
                            Intent intent=new Intent(getActivity(), Zactavka.class);
                            startActivity(intent);
//                            finish();
                        }else{
//                            Snackbar.make(, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            Toast.makeText(getActivity(),"Failed",Toast.LENGTH_LONG).show();
                            Toast.makeText(getActivity(),"Неверно",Toast.LENGTH_LONG).show();
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
            updateUI(currentUser);
        }
    }

    public void updateUI(FirebaseUser currentUser){
        Toast.makeText(getActivity(),"Вы успешно вошли",Toast.LENGTH_LONG).show();
//        Toast.makeText(getActivity(),"Вы уже зарегистрированы",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(getActivity(), Zactavka.class);
        startActivity(intent);
    }

    FirebaseFirestore db;


    public void setTask(final FirebaseUser user,final String name){
        db=FirebaseFirestore.getInstance();
        DocumentReference docRef=db.collection("account").document(user.getUid());
//                            if(docRef==null) {
            HashMap<String, Object> m3 = new HashMap<>();
            m3.put("name", name);
            m3.put("email", user.getEmail());
            m3.put("bookmark", new ArrayList<>());
            m3.put("like", new ArrayList<>());
            m3.put("submit", 0);
            m3.put("right", 0);
            m3.put("money", 100);
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
            m3.put("achiv", achiv);
            m3.put("lastTheme", "Ромб и квадрат");
            docRef.set(m3, SetOptions.merge());
            updateUI(user);

    }
}
