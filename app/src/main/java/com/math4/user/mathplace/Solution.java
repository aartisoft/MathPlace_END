package com.math4.user.mathplace;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class Solution extends AppCompatActivity {

    static FirebaseFirestore db;
    String name,token;
    String answer,solution;
    TextView textViewTask,textViewSolution, textViewAnswer;
    int position;
    LinearLayout linearLayout,linearLayout2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        textLoad2=findViewById(R.id.textViewLoad2);
        Bundle arguments=getIntent().getExtras();
        name=arguments.getString("name");
        token=arguments.getString("token");
        final String text_task=arguments.getString("text_task");
        position=arguments.getInt("position");
        answer=arguments.getString("answer");
        solution=arguments.getString("solution");
        if(solution.equals("null")){
            setContentView(R.layout.activity_solution_2);
            Toolbar toolbar = findViewById(R.id.toolbarSolution);
            setSupportActionBar(toolbar);
            textViewAnswer = findViewById(R.id.textViewAnswer);
            linearLayout2 = findViewById(R.id.linearLayoutTaskSol);
            textViewAnswer.setText("Ответ: " + answer);
            int last = 0;
            String res = "";
            for (int i = 0; i < text_task.length() - 1; i++) {
                if (text_task.substring(i, i + 2).equals("\\n")) {
                    Log.e("checkcheckTheoryN", String.valueOf(i));
                    res = res + text_task.substring(last, i) + "\n";
                    if (i != last) {
                        addText2(text_task.substring(last, i));
                    }
                    last = i + 2;
                } else if (text_task.substring(i, i + 2).equals("[h")) {
                    int j = i;
                    while (j < text_task.length() && !text_task.substring(j, j + 1).equals("]")) {
                        j++;
                    }
                    addText2(text_task.substring(last, i));
                    last = j + 1;
                    addImage2(text_task.substring(i + 1, last - 1));
                }
            }
            if (text_task.length() != last) {
                Log.e("checkcheckTextTheory", text_task.substring(last, text_task.length()));
                addText2(text_task.substring(last, text_task.length()));
            }
            setTitle(name);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }else {
            setContentView(R.layout.activity_solution);
            Toolbar toolbar = findViewById(R.id.toolbarSolution);
            setSupportActionBar(toolbar);
            textViewAnswer = findViewById(R.id.textViewAnswer);
            linearLayout2 = findViewById(R.id.linearLayoutTaskSol);
            textViewAnswer.setText("Ответ: " + answer);
            int last = 0;
            String res = "";
            for (int i = 0; i < text_task.length() - 1; i++) {
                if (text_task.substring(i, i + 2).equals("\\n")) {
                    Log.e("checkcheckTheoryN", String.valueOf(i));
                    res = res + text_task.substring(last, i) + "\n";
                    if (i != last) {
                        addText2(text_task.substring(last, i));
                    }
                    last = i + 2;
                } else if (text_task.substring(i, i + 2).equals("[h")) {
                    int j = i;
                    while (j < text_task.length() && !text_task.substring(j, j + 1).equals("]")) {
                        j++;
                    }
                    addText2(text_task.substring(last, i));
                    last = j + 1;
                    addImage2(text_task.substring(i + 1, last - 1));
                }
            }
            if (text_task.length() != last) {
                Log.e("checkcheckTextTheory", text_task.substring(last, text_task.length()));
                addText2(text_task.substring(last, text_task.length()));
            }
            setTitle(name);
            linearLayout = findViewById(R.id.linearLayoutSolution);
            db = FirebaseFirestore.getInstance();
            DocumentReference docRef;
            if(token.equals("null")) {
                docRef = db.collection("task2").document(name);
            }else{
                docRef = db.collection("olympiad").document(token);
            }
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                    String TAG = "errorCheck";
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            Map<String, Object> inf = ((Map<String, Object>) documentSnapshot.getData());
                            ArrayList arrayList = (ArrayList) inf.get("task" + String.valueOf(position));
                            Log.e("checkcheckSolutionText", String.valueOf(position) + " " + text_task + " " + name.toString());
                            int last = 0;
                            String res = "";
                            String text = String.valueOf(arrayList.get(3));
                            for (int i = 0; i < text.length() - 1; i++) {
                                if (text.substring(i, i + 2).equals("\\n")) {
                                    Log.e("checkcheckTheoryN", String.valueOf(i));
                                    res = res + text.substring(last, i) + "\n";
                                    if (i != last) {
                                        addText(text.substring(last, i));
                                    }
                                    last = i + 2;
                                } else if (text.substring(i, i + 2).equals("[h")) {
                                    int j = i;
                                    while (j < text.length() && !text.substring(j, j + 1).equals("]")) {
                                        j++;
                                    }
                                    addText(text.substring(last, i));
                                    last = j + 1;
                                    addImage(text.substring(i + 1, last - 1));
                                }
                            }
                            if (text.length() != last) {
                                Log.e("checkcheckTextTheory", text.substring(last, text.length()));
                                addText(text.substring(last, text.length()));
                            }
                        }
                    }
                }
            });
            ActionBar actionBar = getSupportActionBar();
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void addImage(String url){
        Log.e("checkcheckUri",url);
        final ImageView imageView=new ImageView(getApplicationContext());
        RelativeLayout.LayoutParams MyParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        MyParams.leftMargin=8;
        MyParams.rightMargin=8;
        imageView.setLayoutParams(MyParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(getApplicationContext()).load(url).into(imageView);
        linearLayout.addView(imageView);
    }
    public void addText(String text){
        Log.e("checkcheckAddText",text);
        final TextView textView=new TextView(getApplicationContext());
        RelativeLayout.LayoutParams MyParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        MyParams.topMargin=10;
        MyParams.leftMargin=8;
        MyParams.rightMargin=8;
        textView.setLayoutParams(MyParams);
        textView.setText(text);
        textView.setTextSize(22);
        textView.setTextColor(Color.parseColor("#4D43BD"));
        linearLayout.addView(textView);
    }
    public void addImage2(String url){
        Log.e("checkcheckUri2",url);
        final ImageView imageView=new ImageView(getApplicationContext());
        RelativeLayout.LayoutParams MyParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        MyParams.leftMargin=8;
        MyParams.rightMargin=8;
        imageView.setLayoutParams(MyParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(getApplicationContext()).load(url).into(imageView);
        linearLayout2.addView(imageView);
    }
    public void addText2(String text){
        Log.e("checkcheckAddText",text);
        final TextView textView=new TextView(getApplicationContext());
        RelativeLayout.LayoutParams MyParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        MyParams.topMargin=16;
        MyParams.leftMargin=8;
        MyParams.rightMargin=8;
        textView.setLayoutParams(MyParams);
        textView.setText(text);
        textView.setTextSize(22);
        textView.setTextColor(Color.parseColor("#4D43BD"));
        linearLayout2.addView(textView);
    }
}
