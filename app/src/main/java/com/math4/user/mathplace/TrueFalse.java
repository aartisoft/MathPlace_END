package com.math4.user.mathplace;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.math4.user.mathplace.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TrueFalse extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    DocumentReference docRef;
    String game;
    ConstraintLayout fnl;
    private AdView mAdView;
//long maxScore;

    Button Button1, Button2;
    TextView textViewCondition, textViewPoint, textViewTimer,textViewScoreMainTF,finText,textViewScoreRecord,scoreView,textViewScoreRecordMenu;
    Random random;
    int answer,viewAnswer;
    int choice = 0;
    int valueOfTime = TrainingParam.valueOfTime;
    int mistakes = -1,mistakesLimit = 3;
    int x=10;
    int score,maxScore;

    boolean pls=false,mns=false,mlp=false,dvd=false;

    DoneTimer timer = new DoneTimer(1000000000, valueOfTime*1000);
    TimerTimer timerTimer = new TimerTimer((valueOfTime+1)*1000, 1000);

    MediaPlayer fl,tr;


    float add = 0f,sub = 0f,mul = 0f,div = 0f;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        random = new Random();
        setContentView(R.layout.activity_true_false);
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        Button1 = findViewById(R.id.buttonTrueFalse1);
        Button2 = findViewById(R.id.buttonTrueFalse2);
        textViewTimer = findViewById(R.id.textViewTimerTF);
        textViewCondition = findViewById(R.id.textViewConditionTrueFalse);
        textViewPoint = findViewById(R.id.textViewPointsTF);
        textViewScoreMainTF = findViewById(R.id.textViewScoreMainTF);
        fl = MediaPlayer.create(this, R.raw.falsesound);
        tr = MediaPlayer.create(this, R.raw.truesound);
        pls = TrainingParam.classTrainingPlus.click;
        mns = TrainingParam.classTrainingMinus.click;
        mlp = TrainingParam.classTrainingMultiply.click;
        dvd = TrainingParam.classTrainingDivide.click;
        textViewScoreRecordMenu=findViewById(R.id.textViewScoreMainTF);
        scoreView=findViewById(R.id.textViewScoreMain2TF);
        textViewScoreRecord = findViewById(R.id.textViewScoreMainTF);
        finText=findViewById(R.id.textViewFinalTF);
        fnl=findViewById(R.id.constraintLayoutFinalTF);
        fnl.setVisibility(View.INVISIBLE);
        game = "game score";
        mistakes = -1;
        mistakesLimit = 3;

        mAuth= FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        db= FirebaseFirestore.getInstance();
        docRef = db.collection("account").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if(documentSnapshot!=null && documentSnapshot.exists()){
                        Map<String,Object> m=documentSnapshot.getData();
                        maxScore = ((Long) m.get("game score")).intValue();
                        textViewScoreMainTF.setText(""+maxScore);
                    }
                }
            }
        });
        timer.start();
        timerTimer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        newCondition();
    }

    public void newCondition() {
        int task = random.nextInt(120);
        if(pls){
            if(mns){
                if(mlp){
                    if(dvd){
                        fullCond(task);
                    }
                    else{
                        pmmCond(task);
                    }
                }
                else if (dvd){
                    pmdCond(task);
                }
                else{
                    pmCond(task);
                }
            }

            else if(mlp){
                if(dvd){
                    pmudCond(task);
                }
                else{
                    pmuCond(task);
                }
            }
            else if(dvd) {
                pdCond(task);
            }
            else{
                pCond(task);
            }
        }
        ///
        else if (mns){
            if(mlp){
                if(dvd){
                    mmudCond(task);
                }
                else{
                    mmuCond(task);
                }
            }
            else if(dvd) {
                mdCond(task);
            }
            else{
                ///
                mCond(task);
            }
        }
        else if (mlp){
            if(dvd) {
                mudCond(task);
            }
            else{
                /////
                muCond(task);
            }
        }
        ///
        else if (dvd) {
            dCond(task);
        }
        else{
            fullCond(task);
        }


        onBut();
        choice = 0;
    }

    public  void fullCond(int task){
        if (task <= 30) {//сложение
            addition();
        } else if (task <= 60) {//вычитание
            subtraction();
        } else if (task <= 90) {//умножение
            try {
                multiply();
            }catch (Exception e){
                multiply();
            }
        } else if (task <= 120) {//деление
            division();
        }
    }
    public  void pmmCond(int task){
        if (task <= 40) {//сложение
            addition();
        } else if (task <= 80) {//вычитание
            subtraction();
        } else if (task <= 120) {//умножение
            try {
                multiply();
            }catch (Exception e){
                multiply();
            }
        }
    }
    public  void pmdCond(int task){
        if (task <= 40) {//сложение
            addition();
        } else if (task <= 80) {//вычитание
            subtraction();
        } else if (task <= 120) {//умножение
            division();
        }
    }
    public  void pmudCond(int task){
        if (task <= 40) {//сложение
            addition();
        } else if (task <= 80) {//вычитание
            try {
                multiply();
            }catch (Exception e){
                multiply();
            }
        } else if (task <= 120) {//умножение
            division();
        }
    }
    public  void mmudCond(int task){
        if (task <= 40) {//сложение
            subtraction();
        } else if (task <= 80) {//вычитание
            try {
                multiply();
            }catch (Exception e){
                multiply();
            }
        } else if (task <= 120) {//умножение
            division();
        }
    }
    public  void pmCond(int task){
        if (task <= 60) {//сложение
            addition();
        } else{//вычитание
            subtraction();
        }
    }
    public  void pdCond(int task){
        if (task <= 60) {//сложение
            addition();
        } else{//вычитание
            division();
        }
    }
    public  void mmuCond(int task){
        if (task <= 60) {//сложение
            subtraction();
        } else{//вычитание
            try {
                multiply();
            }catch (Exception e){
                multiply();
            }
        }
    }
    public  void mudCond(int task){
        if (task <= 60) {//сложение
            try {
                multiply();
            }catch (Exception e){
                multiply();
            }
        } else{//вычитание
            division();
        }
    }
    public  void mdCond(int task){
        if (task <= 60) {//сложение
            subtraction();
        } else{//вычитание
            division();
        }
    }
    public  void pmuCond(int task){
        if (task <= 60) {//сложение
            addition();
        } else{//вычитание
            try {
                multiply();
            }catch (Exception e){
                multiply();
            }
        }
    }
    public  void pCond(int task){
        addition();
    }
    public  void mCond(int task){
        subtraction();
    }
    public  void muCond(int task){
        try {
            multiply();
        }catch (Exception e){
            multiply();
        }
    }
    public  void dCond(int task){
        division();
    }
    public void offBut(){
        Button1.setClickable(false);
        Button2.setClickable(false);
    }

    public void onBut(){
        Button1.setClickable(true);
        Button2.setClickable(true);
        Button1.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training);
        Button2.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training);
    }


    public void addition() {
        int a = random.nextInt(2000);
        int b = random.nextInt(2000);
        int ans = a + b;
        int viewAns = 0;
        int cas = random.nextInt(4);
        switch (cas) {
            case 0: viewAns = ans;
                break;
            case 1: viewAns = ans + random.nextInt(ans)+1;
                break;
            case 2: viewAns = ans - random.nextInt(ans-2) - 1;
                break;
            case 3: viewAns = ans+1-1;
                break;

        }
        setParam(ans, a, b, "+", viewAns);
    }

    public void subtraction() {
        int b = random.nextInt(1000);
        int a = random.nextInt(2000) + b;
        int ans = a - b;
        int viewAns = 0;
        int cas = random.nextInt(4);
        switch (cas) {
            case 0: viewAns = ans;
                break;
            case 1: viewAns = ans + random.nextInt(ans)+1;
                break;
            case 2: viewAns = ans - random.nextInt(ans-2) - 1;
                break;
            case 3: viewAns = ans +1 - 1;
                break;

        }
        setParam(ans, a, b, "-", viewAns);
    }

    public void multiply() {
        int b = random.nextInt(10);
        int a = random.nextInt(200);
        int ans = a * b;
        int viewAns = 0;
        int cas = random.nextInt(4);
        switch (cas) {
            case 0: viewAns = ans;
                break;
            case 1: viewAns = ans + random.nextInt(ans)+1;
                break;
            case 2: viewAns = ans - random.nextInt(ans+2) - 1;
                break;
            case 3: viewAns = ans +1 - 1;
                break;

        }
        setParam(ans, a, b, "*",viewAns);
    }

    public void division() {
        try {
            int b = random.nextInt(15)+1;
            int a = (random.nextInt(50)+1)*b;
            int ans = a/b;
            int viewAns = 0;
            int cas = random.nextInt(4);
            switch (cas) {
                case 0: viewAns = ans;
                    break;
                case 1: viewAns = ans + random.nextInt(ans)+1;
                    break;
                case 2: viewAns = ans - random.nextInt(ans-2) - 1;
                    break;
                case 3: viewAns = ans +1 - 1;
                    break;

            }
            setParam(ans, a, b, "/",viewAns);
        }catch (Exception e){
            division();
        }
    }

    public void exsponent() {

    }


    public void setParam(int ans, int a2, int b2, String sign, int viewAns) {


        textViewCondition.setText(a2 + " " + sign + " " + b2 + " = " + viewAns);
        answer = ans;
        viewAnswer = viewAns;
    }

    public void answer1(View view) {
        Button1.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training_stroked);
        offBut();
        choice = 1;
        check();
    }

    public void answer2(View view) {
        Button2.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training_stroked);
        offBut();
        choice = 2;
        check();
    }


    public void check(){
        String s;

        if (choice == 1  && answer == viewAnswer){
            s = "" + (Integer.parseInt(textViewPoint.getText().toString()) + 1);
            textViewPoint.setText("" + s);
            tr.start();
        }
        else if(choice == 2  && answer != viewAnswer){
            s = "" + (Integer.parseInt(textViewPoint.getText().toString()) + 1);
            textViewPoint.setText("" + s);
            tr.start();
        }
        else{
            mistakes++;
        }
        if(mistakes >= mistakesLimit) {
            timer.onFinish();
            timer.cancel();
        }else {
            timerTimer.cancel();
            timerTimer = new TimerTimer((valueOfTime+1)*1000, 1000);
            time = valueOfTime;
            timerTimer.start();
            newCondition();
        }
    }
    int time = valueOfTime;

    public void timer(){
        textViewTimer.setText("" + time);
        time--;
        if(time == -1){
            check();
        }
    }

    public void homeTraining(View view){
//        Intent intent=new Intent(TrueFalse.this,MainActivity.class);
//        tr.stop();
//        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slidein2, R.anim.slideout2);
    }

    public void replay(View view){
        timer.cancel();
        timerTimer.cancel();
        timer = new DoneTimer(valueOfTime*1000*x, valueOfTime*1000);
        timerTimer = new TimerTimer((valueOfTime+1)*1000,1000);
        fnl.setVisibility(View.INVISIBLE);
        score = 0;
        choice = 0;
        mistakes = -1;
        mistakesLimit = 3;
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if(documentSnapshot!=null && documentSnapshot.exists()){
                        Map<String,Object> m=documentSnapshot.getData();
                        maxScore = ((Long) m.get("game score")).intValue();
                        textViewScoreRecord.setText(""+String.valueOf(maxScore));
                    }
                }
            }
        });
        textViewPoint.setText("" + 0);
        time = valueOfTime;
        timerTimer.start();
        timer.start();
        newCondition();
    }

    public  void finalMenu(){
        score = Integer.parseInt(textViewPoint.getText().toString());
        fnl.setVisibility(View.VISIBLE);
        scoreView.setText(""+textViewPoint.getText().toString());
        textViewScoreRecordMenu.setText(""+String.valueOf(maxScore));

        int fra = random.nextInt(3);
        switch (fra){
            case 0: finText.setText(""+"Так держать!");
                break;
            case 1: finText.setText(""+"Молодец!");
                break;
            case 2: finText.setText(""+"Всё получится!");
                break;
        }
        if (score > maxScore){
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            Map<String, Object> inf = documentSnapshot.getData();
                            Map<String,Object> m=new HashMap<>();
                            m.put("game score",score);
                            docRef.set(m, SetOptions.merge());
                        }
                    }
                }
            });
        }
    }

    class DoneTimer extends CountDownTimer{

        public DoneTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            //Check();
        }

        @Override
        public void onFinish() {
            finalMenu();
        }
    }

    class TimerTimer extends CountDownTimer{

        public TimerTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            timer();
        }

        @Override
        public void onFinish() {

        }
    }

}