package com.math4.user.mathplace;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    EditText textSearch;
    static ProgressBar textLoad,textLoad2;
    private AppBarConfiguration mAppBarConfiguration;
    static ImageView imageUser;
    static LinearLayout viewChats;
    static MenuItem searchItem, settingsItem;
    static TextView nameHead;
    static Context contextMain;
    static Toolbar toolbar;
    static BottomSheetBehavior bottomSheetBehaviorUsers;
    private NotificationUtils mNotificationUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mNotificationUtils = new NotificationUtils(this);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            Notification.Builder nb = mNotificationUtils.
//                    getAndroidChannelNotification("Привет", "Пора уделить немного времени математике", this);
//            mNotificationUtils.getManager().notify(101, nb.build());
//        }
        textLoad=findViewById(R.id.textViewLoad);
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();


        viewChats = findViewById(R.id.containerBottomSheetSearch);
        bottomSheetBehaviorUsers = BottomSheetBehavior.from(viewChats);
        bottomSheetBehaviorUsers.setPeekHeight(0);

        bottomSheetBehaviorUsers.setHideable(false);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        contextMain=this;
        BottomNavigationView navView = findViewById(R.id.nav_viewMain);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_play, R.id.navigation_dashboard,R.id.navigation_rank, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragmentMain);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        db = FirebaseFirestore.getInstance();
    }

    private void restartNotify() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, TimeNotification.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                    intent, PendingIntent.FLAG_CANCEL_CURRENT);
            // На случай, если мы ранее запускали активити, а потом поменяли время,
            // откажемся от уведомления
            am.cancel(pendingIntent);
            // Устанавливаем разовое напоминание
            Date stamp = new Date();
            Date stamp2 = new Date();
            stamp = new Date(53700000 + stamp.getTime() - ((stamp.getTime() % 86400000)));
            Log.e("check_time", String.valueOf((stamp.getTime() % 86400000) / 3600000) + " " + String.valueOf(((stamp.getTime() % 86400000) / 60000) % 60));
            Log.e("check_time", String.valueOf((stamp2.getTime() % 86400000) / 3600000) + " " + String.valueOf(((stamp2.getTime() % 86400000) / 60000) % 60));
            am.set(AlarmManager.RTC_WAKEUP, stamp.getTime(), pendingIntent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        textLoad.setVisibility(View.INVISIBLE);
//        textLoad2.setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_search, menu);
        searchItem=menu.findItem(R.id.action_search);
        settingsItem=menu.findItem(R.id.action_settings);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        final Uri selectedImage = imageReturnedIntent.getData();
        imageUser.setImageURI(selectedImage);
        Log.e("checkcheckUri",selectedImage.toString());

    }

    public void search(MenuItem item){
        Intent intent=new Intent(getApplicationContext(),SearchActivity.class);
        startActivity(intent);
    }

    public void bookmark(MenuItem item){
        Intent intent=new Intent(getApplicationContext(), Bookmark.class);
        startActivity(intent);
    }
    public void settings(MenuItem item){
        Intent intent=new Intent(getApplicationContext(), Settings.class);
        startActivity(intent);
    }
    public void test(MenuItem item){
        Intent intent=new Intent(getApplicationContext(), Bookmark.class);
        startActivity(intent);
    }
    public void share(MenuItem item){
        final Intent intent = new Intent(Intent.ACTION_SEND);
        Intent intent1= Something.share(intent);
        try {
            startActivity(Intent.createChooser(intent1,"Поделиться с помощью:" ));
        }
        catch (android.content.ActivityNotFoundException e)
        {
            Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
        }
    }
    public void game(View view){
        Intent intent;
        if(TrainingParam.classTraining1.click){
            if(TrainingParam.classTrainingVvod.click){
                intent = new Intent(MainActivity.this,Training.class);
            }
            else {
                intent = new Intent(MainActivity.this, TrueFalse.class);
            }
        }
        else{
            if(TrainingParam.classTrainingVvod.click) {
                intent = new Intent(MainActivity.this, MultiPlayer.class);
            }
            else{
                intent = new Intent(MainActivity.this, TFMultiPlayer.class);
            }
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
    }
    final String[] mChooseCats = { "09:00", "13:00", "18:00" };
    private final int IDD_THREE_BUTTONS = 0 , IDD_LISTVIEW_BUTTON=1,IDD_ALERTDIALOG=2;
    public void Notificate(View view) {
        showDialog(IDD_THREE_BUTTONS);
    }
    public void showTheme() { showDialog(IDD_LISTVIEW_BUTTON); }
    public void showInternet() { showDialog(IDD_ALERTDIALOG); }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case IDD_THREE_BUTTONS:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
                builder.setTitle("Выберите время уведомлений")
                        .setCancelable(false)
                        .setNeutralButton("Назад",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton("Окей",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                        .setSingleChoiceItems(mChooseCats, -1,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                return builder.create();
            case IDD_LISTVIEW_BUTTON:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("Выберите время уведомлений")
                        .setCancelable(false)
                        .setNeutralButton("Назад",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton("Окей",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                        .setSingleChoiceItems(mChooseCats, -1,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                return builder2.create();
            case IDD_ALERTDIALOG:
////                AlertDialog.Builder builder3 = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
//                return dialog;
            default:
                return null;
        }
    }
    public void empty(View view){
        textSearch.setText("");
    }
    public void aboutapp(View view){
        startActivity(new Intent(MainActivity.this, AboutApp.class));
    }
    public void playmarket(MenuItem item){
        final AlertDialog.Builder ratingdialog = new AlertDialog.Builder(this, R.style.RateMathPlace);

        ratingdialog.setIcon(R.drawable.logo3);
        ratingdialog.setTitle("Оцените MathPlace");

        View linearlayout = getLayoutInflater().inflate(R.layout.ratingdialog, null);
        ratingdialog.setView(linearlayout);

        final RatingBar rating = (RatingBar)linearlayout.findViewById(R.id.ratingbar);
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                Toast.makeText(getApplicationContext(),"GOOOD",Toast.LENGTH_LONG).show();
                ratingdialog.setPositiveButton(R.string.ok_decide, null);
//                ratingdialog.show();
            }
        });
        ratingdialog.setPositiveButton("Оценить",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        txtView.setText(String.valueOf(rating.getRating()));
                        if(rating.getRating()>=4) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.math4.user.mathplace")));
                        }else{
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://ledokolpro.tilda.ws/review")));
                        }
                        dialog.dismiss();
                    }
                })

                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        ratingdialog.create();
        ratingdialog.show();
    }
    public void like(MenuItem item){
        startActivity(new Intent(MainActivity.this, Like.class));
    }

}
