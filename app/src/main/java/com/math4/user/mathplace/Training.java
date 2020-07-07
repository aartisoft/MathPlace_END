package com.math4.user.mathplace;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Training extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    DocumentReference docRef;

    private AdView mAdView;
    Button[] Buttons = new Button[4];
    TextView textViewCondition, textViewPoint, textViewTimer,scoreView,finText,textViewScoreRecord,textViewScoreRecordMenu;
    Random random;
    ConstraintLayout fnl;
    int answer;
    int choice = 0;
    int valueOfTime = TrainingParam.valueOfTime;
    Button fnlBut;
    ImageView fnlBack;
    int x=10;
    String game;
    int maxScore=0,score=0;
    int mistakes = 0,mistakesLimit = 3;

    boolean pls=false,mns=false,mlp=false,dvd=false;

    DoneTimer timer = new DoneTimer(valueOfTime*1000*x, valueOfTime*1000);
    TimerTimer timerTimer = new TimerTimer((valueOfTime+1)*1000,1000);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        random = new Random();
        setContentView(R.layout.activity_training);
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        Buttons[0] = findViewById(R.id.buttonTrain1);
        Buttons[1] = findViewById(R.id.buttonTrain2);
        Buttons[2] = findViewById(R.id.buttonTrain3);
        Buttons[3] = findViewById(R.id.buttonTrain4);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewCondition = findViewById(R.id.textViewConditionTrain);
        textViewPoint = findViewById(R.id.textViewPoints);
        textViewScoreRecord = findViewById(R.id.textViewScoreRecord);
        textViewScoreRecordMenu=findViewById(R.id.textViewScoreRecord);
        scoreView=findViewById(R.id.textViewScoreMain2);
//        fnlBack = findViewById(R.id.imageViewFinal);
//        scoreView = findViewById(R.id.textViewScoreMain);
        finText=findViewById(R.id.textViewFinal);
        fnl=findViewById(R.id.constraintLayoutFinal);
        fnl.setVisibility(View.INVISIBLE);
        pls = TrainingParam.classTrainingPlus.click;
        mns = TrainingParam.classTrainingMinus.click;
        mlp = TrainingParam.classTrainingMultiply.click;
        dvd = TrainingParam.classTrainingDivide.click;
        game = "game score";
        x = 10;
        score = 0;
        mistakes = -1;
        mistakesLimit = 3;
        timer.start();

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
                        Log.e("checkcheckGameScore",String.valueOf(m.get("game score")));
                        maxScore = ((Long) m.get("game score")).intValue();
                        textViewScoreRecord.setText(""+String.valueOf(maxScore));
                    }
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        newCondition();
        check();
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
        //score = 0;
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
        for(int i = 0;i < Buttons.length;i++){
            Buttons[i].setClickable(false);
        }
    }

    public void onBut(){
        for(int i = 0;i < Buttons.length;i++){
            Buttons[i].setClickable(true);
            Buttons[i].setBackgroundResource(R.drawable.btn_rounded_corner_ans_training);
        }
    }


    public void addition() {
        int a = random.nextInt(2000);
        int b = random.nextInt(2000);
        int ans = a + b;
        int cas = random.nextInt(4);
        int val1 = 0, val2 = 0, val3 = 0, val4 = 0;
        switch (cas) {
            case 0:
                val1 = ans;
                val2 = ans / (random.nextInt(3) + 2);
                val3 = ans + random.nextInt(40) + 1;
                val4 = ans + random.nextInt(70) + 1;
                break;
            case 1:
                val1 = ans + random.nextInt(70) + 1;
                val2 = ans;
                val3 = ans / (random.nextInt(3) + 2);
                val4 = ans + random.nextInt(40) + 1;
                break;
            case 2:
                val1 = ans + random.nextInt(40) + 1;
                val2 = ans / (random.nextInt(3) + 2);
                val3 = ans;
                val4 = ans + random.nextInt(50) + 1;
                break;
            case 3:
                val1 = ans + random.nextInt(70) + 1;
                val2 = ans / (random.nextInt(3) + 2);
                val3 = ans + random.nextInt(40) + 1;
                val4 = ans;
                break;
            default:
                val1 = ans + random.nextInt(70) + 2;
                val2 = ans / (random.nextInt(3) + 2);
                val3 = ans + random.nextInt(40) + 1;
                val4 = ans;
                break;
        }
        setParam(val1, val2, val3, val4, ans, a, b, "+");
    }

    public void subtraction() {
        int b = random.nextInt(1000);
        int a = random.nextInt(2000) + b;
        int ans = a - b;
        int cas = random.nextInt(4);
        int val1 = 0, val2 = 0, val3 = 0, val4 = 0;
        switch (cas) {
            case 0:
                val1 = ans;
                val2 = ans + random.nextInt(ans / 2 + 2) + 1;
                val3 = ans - random.nextInt(ans) + 1;
                val4 = ans + random.nextInt(ans) + 1;
                break;
            case 1:
                val1 = ans + random.nextInt(ans) + 1;
                val2 = ans;
                val3 = ans + random.nextInt(ans + 1) + 1;
                val4 = ans - random.nextInt(ans) - 1;
                break;
            case 2:
                val1 = ans - random.nextInt(ans) - 1;
                val2 = ans + random.nextInt(ans) + 1;
                val3 = ans;
                val4 = ans - (random.nextInt(ans / 2 + 1) + 1);
                break;
            case 3:
                val1 = ans + random.nextInt(ans) + 1;
                val2 = ans + random.nextInt(ans + 3) + 1;
                val3 = ans - (random.nextInt(ans) + 1);
                val4 = ans;
                break;
            default:
                val1 = ans + random.nextInt(ans) + 1;
                val2 = ans + random.nextInt(ans + 3) + 2;
                val3 = ans - (random.nextInt(ans) + 1);
                val4 = ans;
                break;
        }
        setParam(val1, val2, val3, val4, ans, a, b, "-");
    }

    public void multiply() {
        int b = random.nextInt(10);
        int a = random.nextInt(200);
        int ans = a * b;
        int cas = random.nextInt(4);
        int val1 = 0, val2 = 0, val3 = 0, val4 = 0;
        switch (cas) {
            case 0:
                val1 = ans;
                val2 = ans + random.nextInt(ans / 2 + 2) + 1;
                val3 = ans - random.nextInt(ans+1) + 1;
                val4 = ans + random.nextInt(ans+1) + 1;
                break;
            case 1:
                val1 = ans + random.nextInt(ans+1) + 1;
                val2 = ans;
                val3 = ans + random.nextInt(ans + 1) + 1;
                val4 = ans - random.nextInt(ans+1) - 1;
                break;
            case 2:
                val1 = ans - random.nextInt(ans+1) - 1;
                val2 = ans + random.nextInt(ans+1) + 1;
                val3 = ans;
                val4 = ans - (random.nextInt(ans / 2 + 1) + 1);
                break;
            case 3:
                val1 = ans + random.nextInt(ans+1) + 1;
                val2 = ans + random.nextInt(ans + 3) + 1;
                val3 = ans - (random.nextInt(ans+1) + 1);
                val4 = ans;
                break;
            default:
                val1 = ans + random.nextInt(ans+1) + 1;
                val2 = ans + random.nextInt(ans + 3) + 2;
                val3 = ans - (random.nextInt(ans+2) + 1);
                val4 = ans;
                break;
        }
        setParam(val1, val2, val3, val4, ans, a, b, "*");
    }

    public void division() {
        try {
            int b = random.nextInt(15)+1;
            int a = (random.nextInt(50)+1)*b;
            int ans = a/b;
            int cas = random.nextInt(4);
            int val1, val2, val3, val4;
            switch (cas) {
                case 0:
                    val1 = ans;
                    val2 = ans + random.nextInt(ans / 2 + 2) + 1;
                    val3 = ans - random.nextInt(ans) + 1;
                    val4 = ans + random.nextInt(ans) + 1;
                    break;
                case 1:
                    val1 = ans + random.nextInt(ans) + 1;
                    val2 = ans;
                    val3 = ans + random.nextInt(ans + 1) + 1;
                    val4 = ans - random.nextInt(ans) - 1;
                    break;
                case 2:
                    val1 = ans - random.nextInt(ans) - 1;
                    val2 = ans + random.nextInt(ans) + 1;
                    val3 = ans;
                    val4 = ans - random.nextInt(ans / 2 + 1) - 1;
                    break;
                case 3:
                    val1 = ans + random.nextInt(ans) + 1;
                    val2 = ans + random.nextInt(ans + 3) + 1;
                    val3 = ans - random.nextInt(ans) - 1;
                    val4 = ans;
                    break;
                default:
                    val1 = ans + random.nextInt(ans) + 1;
                    val2 = ans + random.nextInt(ans + 3) + 2;
                    val3 = ans - random.nextInt(ans) - 2;
                    val4 = ans;
                    break;
            }
            setParam(val1, val2, val3, val4, ans, a, b, "/");
        }catch (Exception e){
            division();
        }
    }

    public void setParam(int val1, int val2, int val3, int val4, int ans, int a2, int b2, String sign) {
        ArrayList<Integer> a_return = new ArrayList<>();
        while (a_return.size() < 4) {
            int i = random.nextInt(4);
            if (!a_return.contains(i)) {
                a_return.add(i);
            }
        }
        Buttons[0].setText(String.valueOf(val1));
        Buttons[1].setText(String.valueOf(val2));
        Buttons[2].setText(String.valueOf(val3));
        Buttons[3].setText(String.valueOf(val4));

        textViewCondition.setText(a2 + " " + sign + " " + b2 + " = ");
        answer = ans;
    }

    public void answer1(View view) {
        Buttons[0].setBackgroundResource(R.drawable.btn_rounded_corner_ans_training_stroked);
        offBut();
        choice = 1;
        check();
    }

    public void answer2(View view) {
        Buttons[1].setBackgroundResource(R.drawable.btn_rounded_corner_ans_training_stroked);
        offBut();
        choice = 2;
        check();
    }

    public void answer3(View view) {
        Buttons[2].setBackgroundResource(R.drawable.btn_rounded_corner_ans_training_stroked);
        offBut();
        choice = 3;
        check();
    }

    public void answer4(View view) {
        Buttons[3].setBackgroundResource(R.drawable.btn_rounded_corner_ans_training_stroked);
        offBut();
        choice = 4;
        check();
    }

    public void check(){
        String s;

        if ( choice != 0 && Integer.parseInt(Buttons[choice-1].getText().toString()) == answer){
            score = (Integer.parseInt(textViewPoint.getText().toString()) + 1);
            s = "" + score;
            textViewPoint.setText("" + s);
        }
        else{
            mistakes++;
        }
        if(mistakes >= mistakesLimit){
            timer.onFinish();
            timer.cancel();
        }else{
            timerTimer.cancel();
            timerTimer = new TimerTimer((valueOfTime+1)*1000,1000);
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
//        Intent intent=new Intent(Training.this,MainActivity.class);
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
            //check();
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