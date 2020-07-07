package com.math4.user.mathplace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.math4.user.mathplace.Distrib.allInf;

public class Like extends Fragment {
    View view;
    LinearLayout linearLayout;
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";
    private InterstitialAd interstitialAd;
    DocumentReference docRef;
    @Override
    // Переопределяем метод onCreateView
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_like, container, false);
        linearLayout=view.findViewById(R.id.linearLayoutLike);

        //---------------------------------
        final LayoutInflater ltInflater = getLayoutInflater();
        final LinearLayout linearLayoutLike=view.findViewById(R.id.linearLayoutLikeEmpty);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        linearLayoutLike.setVisibility(View.INVISIBLE);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        docRef=db.collection("account").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> inf = documentSnapshot.getData();
                        ArrayList taskBookmark = (ArrayList) inf.get("like");
                        if(taskBookmark.size()==0){
                            linearLayoutLike.setVisibility(View.VISIBLE);
                        }
                        for (int i = 0; i < taskBookmark.size(); i++) {
                            final int i2=i;
                            final CardView linLayout = (CardView) ltInflater.inflate(R.layout.example_like, linearLayout, false);
                            ((TextView)linLayout.findViewById(R.id.textViewExampleLike)).setText(taskBookmark.get(i).toString());
                            ImageButton imageButton=linLayout.findViewById(R.id.imageButtonExampleLike);
                            imageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    linearLayout.removeView(linLayout);
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot documentSnapshot = task.getResult();
                                                if (documentSnapshot.exists()) {
                                                    Map<String, Object> inf = documentSnapshot.getData();
//                                                    Map<String,Object> m=new HashMap<>();
                                                    ArrayList taskRegistr = (ArrayList) inf.get("like");
                                                    taskRegistr.remove(i2);
//                                                    inf.put()
                                                    allInf.put("like",taskRegistr);
                                                    docRef.set(inf);
//                                                    m.put("submit",(Long)inf.get("submit")+1);
//                                                    m.put("right",(Long)inf.get("right")+1);
//                                                    docRef.set(m, SetOptions.merge());
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                            linLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(Menu.context_this, Topic.class);
                                    intent.putExtra("thisTheme",((TextView)linLayout.findViewById(R.id.textViewExampleLike)).getText().toString());
                                    FirebaseAuth mAuth=FirebaseAuth.getInstance();
                                    FirebaseUser user=mAuth.getCurrentUser();
                                    FirebaseFirestore db=FirebaseFirestore.getInstance();
                                    final DocumentReference docRef = db.collection("account").document(user.getUid());
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot documentSnapshot = task.getResult();
                                                if (documentSnapshot.exists()) {
                                                    Map<String, Object> inf = documentSnapshot.getData();
                                                    Map<String, Object> m = new HashMap<>();
                                                    m.put("lastTheme",((TextView)linLayout.findViewById(R.id.textViewExampleLike)).getText().toString());
                                                    docRef.set(m, SetOptions.merge());
                                                }
                                            }
                                        }
                                    });
                                    CurrentUser.setLastTheme(((TextView)linLayout.findViewById(R.id.textViewExampleLike)).getText().toString());
                                    Menu.textViewStudy.setText(CurrentUser.lastTheme);
                                    Menu.context_this.startActivity(intent);
                                }
                            });
                            linearLayout.addView(linLayout);
//                            ((TextView)(ltInflater.inflate(R.layout.example_bookmark, null, false)).findViewById(R.id.textViewExampleBookmark)).setText(taskBookmark.get(i).toString());
                            Log.e("checkcheckBookmark",taskBookmark.get(i).toString());
//                            ((TextView)(ltInflater.inflate(R.layout.example_bookmark, linearLayout, true).findViewById(R.id.textViewExampleBookmark))).setText(taskBookmark.get(i).toString());
//                            ((TextView)view1.findViewById(R.id.textViewExampleBookmark)).setText(taskBookmark.get(i).toString());
//                            linearLayout.addView(Something.addBookmarkButton(taskBookmark.get(i).toString(),getApplicationContext()));
                        }
                    }
                }
            }
        });
        return view;
    }

}
