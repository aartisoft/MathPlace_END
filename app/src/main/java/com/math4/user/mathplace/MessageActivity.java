package com.math4.user.mathplace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    FirebaseUser fUser;
    static FirebaseFirestore db;

    Intent intent;
    static MessageActivity contextActivity;

    ImageButton btn_send;
    EditText text_send;
    public static String userID;
    public static String lMsg;
    public static String lRec;
    public static String lSen;
    public static long lAll=1;
    DocumentReference docRefYou;
    DocumentReference docRefPartner;
    static boolean isNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.userName);

        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();
        userID = intent.getStringExtra("userid");

        contextActivity = this;

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fUser.getUid(), userID, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "Вы не можете отправить пустое ссобщение", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        db= FirebaseFirestore.getInstance();
        DocumentReference docRef3 = db.collection("account").document(userID);

        docRef3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        final Map<String, Object> m = documentSnapshot.getData();
                        username.setText(m.get("name").toString());
                        try {
                            if(m.get("image")==null&&m.get("image").toString().equals("default")){
                                profile_image.setImageResource(R.drawable.account_new);
                            }
                            else{
                                Glide.with(MessageActivity.this).load(m.get("imageURL")).into(profile_image);
                            }
                        }catch (Exception e){
                            profile_image.setImageResource(R.drawable.account_new);
                        }
                    }
                }
            }
        });

        docRefPartner = db.collection("account").document(userID).collection("chat").document(fUser.getUid());
        docRefYou = db.collection("account").document(fUser.getUid()).collection("chat").document(userID);
        docRefYou.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        isNew = false;
                    }
                    else{
                        isNew = true;
                        HashMap<String,Object> m3=new HashMap<>();
                        m3.put("all_message",0);
                        docRefYou.set(m3, SetOptions.merge());
                        docRefPartner.set(m3, SetOptions.merge());
                    }
                }
            }
        });

        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new MessageFragment());

        viewPager.setAdapter(viewPagerAdapter);

    }


    private void sendMessage(final String sender, final String receiver, final String message){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        lMsg = message;
        lRec = receiver;
        lSen = sender;
        docRefPartner = db.collection("account").document(userID).collection("chat").document(fUser.getUid());
        docRefYou = db.collection("account").document(fUser.getUid()).collection("chat").document(userID);
        docRefYou.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        final Map<String, Object> m = documentSnapshot.getData();
                        ArrayList msg = new ArrayList<>();
                        msg.add(message);
                        msg.add(receiver);

                        long all = Long.parseLong(m.get("all_message").toString());
                        lAll = all;
                        m.put("message" + all + "time", FieldValue.serverTimestamp()); // Передаём время на данный момент
                        m.put("message" + all, msg);
                        m.put("all_message", ++all);
                        docRefYou.set(m, SetOptions.merge());
                        docRefPartner.set(m, SetOptions.merge());
                        if(all < 1)
                            MessageFragment.update();
                    }
                    else{
                        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                        DocumentReference docRef1= db2.collection("account").document(fUser.getUid()).collection("chat").document(userID);
                        DocumentReference docRef2= db2.collection("account").document(userID).collection("chat").document(fUser.getUid());
                        HashMap<String,Object> m3=new HashMap<>();
                        m3.put("all_message",0);
                        docRef1.set(m3, SetOptions.merge());
                        docRef2.set(m3, SetOptions.merge());
                        sendMessage(sender, receiver, message);
                    }

                }
            }
        });
    }


    class  ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public  void addFragment(Fragment fragment){
            fragments.add(fragment);
        }

    }
}
