package com.math4.user.mathplace;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.math4.user.mathplace.MainActivity.searchItem;

public class Settings extends AppCompatActivity {
    private AdView mAdView;
    String time_notification;
    String time_notify;
    Context context;
    FirebaseFirestore db;
    static long timeNotify;
    private NotificationUtils mNotificationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Настройки");
        context = this;
        searchItem.setVisible(false);
        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final FirebaseUser user=mAuth.getCurrentUser();

        TextView textViewLogOut = findViewById(R.id.textViewLogOut);
        LinearLayout textViewName = findViewById(R.id.constraintLayoutChangeName);
        textViewLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                        .setTitle("Выход из аккаунта")
                        .setMessage("Вы уверены, что вы хотите выйти и аккаунта?")

                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent=new Intent(Settings.this,Hello.class);
                                startActivity(intent);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Назад", null)
                        .show();
            }
        });
        textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                LayoutInflater inflater2 = getLayoutInflater();
                View dialogView = inflater2.inflate(R.layout.alertdialog_custom_view,null);

                // Specify alert dialog is not cancelable/not ignorable
                builder.setCancelable(false);

                // Set the custom layout as alert dialog view
                builder.setView(dialogView);

                // Get the custom alert dialog view widgets reference
                Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
                Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);
                final EditText et_name = (EditText) dialogView.findViewById(R.id.et_name);

                // Create the alert dialog
                final AlertDialog dialog = builder.create();

                // Set positive/yes button click listener
                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss the alert dialog
                        if(et_name.getText().toString().length()>0) {
                            dialog.cancel();
                            Distrib.allInf.put("name", et_name.getText().toString());
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            FirebaseUser user = mAuth.getCurrentUser();
//                            MainActivity.nameHead.setText(et_name.getText().toString());
                            final DocumentReference docRef = db.collection("account").document(user.getUid());
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if (documentSnapshot.exists()) {
                                            Map<String, Object> inf = documentSnapshot.getData();
                                            Map<String, Object> m = new HashMap<>();
                                            m.put("name", et_name.getText().toString());
                                            docRef.set(m, SetOptions.merge());
                                        }
                                    }
                                }
                            });
                            String name = et_name.getText().toString();
                            Toast.makeText(getApplicationContext(),
                                    "Новое имя : " + name, Toast.LENGTH_SHORT).show();
                            // Say hello to the submitter
                        }else{
                            Toast.makeText(getApplicationContext(),
                                    "Имя не может быть пустым", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                // Set negative/no button click listener
                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss/cancel the alert dialog
                        //dialog.cancel();
                        dialog.dismiss();
                    }
                });

                // Display the custom alert dialog on interface
                dialog.show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        final FirebaseUser user= mAuth.getCurrentUser();
        final TextView textViewTimeNotify = findViewById(R.id.time_notify);
        DocumentReference docRef = db.collection("account").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot= task.getResult();
                    if(documentSnapshot.exists()){
                        Map<String, Object> inf = documentSnapshot.getData();
                        mNotificationUtils = new NotificationUtils(context);
                        if(inf.get("notification")!=null){
                            time_notification = inf.get("notification").toString();
                            if(time_notification.equals("9")){
                                textViewTimeNotify.setText("9:00 — 10:00");
                            }else if(time_notification.equals("12")){
                                textViewTimeNotify.setText("12:00 — 13:00");
                            }else if(time_notification.equals("18")){
                                textViewTimeNotify.setText("18:00 — 19:00");
                            }else if(time_notification.equals("21")){
                                textViewTimeNotify.setText("21:00 — 22:00");
                            }else{
                                textViewTimeNotify.setText("отключена");
                            }
                        }else{
                            textViewTimeNotify.setText("18:00 — 19:00");
                        }
                    }
                }
            }
        });

        LinearLayout linearLayoutNotification = findViewById(R.id.linearLayoutNotification);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            linearLayoutNotification.setClickable(false);
            textViewTimeNotify.setText("Уведомления недоступны");
        }
        linearLayoutNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                LayoutInflater inflater2 = getLayoutInflater();
                final View dialogView = inflater2.inflate(R.layout.alertdialog_notification,null);

                // Specify alert dialog is not cancelable/not ignorable
                builder.setCancelable(true);

                // Set the custom layout as alert dialog view
                builder.setView(dialogView);
                final RadioGroup radioGroup = dialogView.findViewById(R.id.RadioGroupNotify);
                Button buttonOk = dialogView.findViewById(R.id.button_ok);
                Button buttonCancel = dialogView.findViewById(R.id.button_cancel);
                final RadioButton radioButton1= dialogView.findViewById(R.id.radio_morning);
                final RadioButton radioButton2= dialogView.findViewById(R.id.radio_afternoon);
                final RadioButton radioButton3= dialogView.findViewById(R.id.radio_evening);
                final RadioButton radioButton4= dialogView.findViewById(R.id.radio_night);
                final RadioButton radioButton5= dialogView.findViewById(R.id.radio_net);

//                Toast.makeText(getApplicationContext(),textViewTimeNotify.getText(),Toast.LENGTH_LONG).show();
                if(textViewTimeNotify.getText().toString().equals("9:00 — 10:00")){
                    Toast.makeText(getApplicationContext(),"first",Toast.LENGTH_LONG).show();
                    radioButton1.setChecked(true);
                }else if(textViewTimeNotify.getText().toString().equals("12:00 — 13:00")){
                    radioButton2.setChecked(true);
                }else if(textViewTimeNotify.getText().toString().equals("18:00 — 19:00")){
                    radioButton3.setChecked(true);
                }else if(textViewTimeNotify.getText().toString().equals("21:00 — 22:00")){
                    radioButton4.setChecked(true);
                }else if(textViewTimeNotify.getText().toString().equals("отключена")){
                    radioButton5.setChecked(true);
                }

                final AlertDialog dialog = builder.create();
                //                radioButton.set
                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(radioButton1.isChecked()){
                            time_notify="9";
                            textViewTimeNotify.setText("9:00 — 10:00");
                            restartNotify(10800000);
                        }else if(radioButton2.isChecked()){
                            time_notify="12";
                            textViewTimeNotify.setText("12:00 — 13:00");
                            restartNotify(21600000);
                        }else if(radioButton3.isChecked()){
                            time_notify="18";
                            textViewTimeNotify.setText("18:00 — 19:00");
                            restartNotify(43200000);
                        }else if(radioButton4.isChecked()){
                            time_notify="21";
                            textViewTimeNotify.setText("21:00 — 22:00");
//                            restartNotify(75600000);
                            restartNotify(54000000);
                        }else if(radioButton5.isChecked()){
                            time_notify="нет";
                            textViewTimeNotify.setText("отключена");
                            mNotificationUtils.cancalNotify();
                        }
                        final DocumentReference docRef = db.collection("account").document(user.getUid());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()) {
                                        final Map<String, Object> m = documentSnapshot.getData();
                                        m.put("notification", time_notify);
                                        docRef.set(m, SetOptions.merge());
                                    }
                                }
                            }
                        });
                        dialog.cancel();
                    }
                });
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                // Display the custom alert dialog on interface
                dialog.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                showInterstitial();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void aboutapp(View view){
        startActivity(new Intent(Settings.this, AboutApp.class));
    }


    private void restartNotify(long time) {
//        Toast.makeText(getApplicationContext(),"NotifyStart",Toast.LENGTH_LONG).show();;
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


    public void vk(View view){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/mathplace")));
    }



    public void website(View view){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://mathplace-842f7.web.app/")));
    }

    public void instagram(View view){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/ledokol_team/")));
    }



}
