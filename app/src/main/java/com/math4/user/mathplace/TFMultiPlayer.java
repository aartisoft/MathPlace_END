package com.math4.user.mathplace;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.math4.user.mathplace.R;

import java.util.Random;

public class TFMultiPlayer extends AppCompatActivity {
    Button ButtonFalseTop,ButtonTrueTop,
            ButtonFalseBottom,ButtonTrueBottom;
    TextView textViewConditionTop,textViewConditionBottom, textViewPointTop,textViewPointBot,textViewTimerBot,textViewTimerTop,finText;
    Random random;
    private InterstitialAd interstitialAd;
    boolean pls=false,mns=false,mlp=false,dvd=false;
    // ПОТОКИ ПОТОКИ
    //MyThread thread = new MyThread();
    // В РАЗРАБОТКЕ В РАЗРАБОТКЕ
    int x = 100000;
    int answer,viewAnswer;
    int choiceTop = 0, choiceBot = 0, first = 0;
    int valueOfTime = TrainingParam.valueOfTime;

    int score1 = 0, score2 = 0;
    ConstraintLayout fnl;

    DoneTimer timer = new DoneTimer(1000000000, (valueOfTime+1)*1000);
    TimerTimer timerTimer = new TimerTimer((valueOfTime+1)*1000,1000);


    private static final String AD_UNIT_ID = "ca-app-pub-3206990026084887/4324193868";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        random=new Random();
        setContentView(R.layout.activity_tfmulti_player);

        finText=findViewById(R.id.textViewFinalTFMP);
        fnl=findViewById(R.id.constraintLayoutFinalTFMP);
        fnl.setVisibility(View.INVISIBLE);

        ButtonFalseTop=findViewById(R.id.buttonFalseTop);
        ButtonTrueTop=findViewById(R.id.buttonTrueTop);
        ButtonFalseBottom=findViewById(R.id.buttonFalseBottom);
        ButtonTrueBottom=findViewById(R.id.buttonTrueBottom);
        textViewConditionBottom=findViewById(R.id.textViewConditionTrueFalseBottom);
        textViewConditionTop=findViewById(R.id.textViewConditionTrueFalseTop);
        textViewPointTop=findViewById(R.id.textViewPointsTopTF);
        textViewPointBot=findViewById(R.id.textViewPointsBotTF);
        textViewTimerBot=findViewById(R.id.textViewTimerBot);
        textViewTimerTop=findViewById(R.id.textViewTimerTop);
        pls = TrainingParam.classTrainingPlus.click;
        mns = TrainingParam.classTrainingMinus.click;
        mlp = TrainingParam.classTrainingMultiply.click;
        dvd = TrainingParam.classTrainingDivide.click;
        timer.start();
        timerTimer.start();
    }
    @Override
    protected void onResume(){
        super.onResume();
        newCondition();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        interstitialAd = new InterstitialAd(this);
//        // Defined in res/values/strings.xml
//        interstitialAd.setAdUnitId(AD_UNIT_ID);
//
//        interstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
////                Toast.makeText(Bookmark.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
////                showInterstitial();
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
////                Toast.makeText(Bookmark.this,
////                        "onAdFailedToLoad() with error code: " + errorCode,
////                        Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdClosed() {
////                startGame();
//            }
//        });
//
//        startGame();

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
    public void offBut(Button b1, Button b2){
        b1.setClickable(false);
        b2.setClickable(false);
    }

    public void onBut(){
        ButtonTrueBottom.setClickable(true);
        ButtonFalseBottom.setClickable(true);
        ButtonTrueTop.setClickable(true);
        ButtonFalseTop.setClickable(true);
        ButtonTrueBottom.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training);
        ButtonFalseBottom.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training);
        ButtonTrueTop.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training);
        ButtonFalseTop.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training);
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
            case 3: viewAns = ans +1;
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


    public void exsponent(){

    }

    public void setParam(int ans, int a2, int b2, String sign, int viewAns) {


        textViewConditionBottom.setText(a2 + " " + sign + " " + b2 + " = " + viewAns);
        textViewConditionTop.setText(a2 + " " + sign + " " + b2 + " = " + viewAns);
        answer = ans;
        viewAnswer = viewAns;
        //first = 0;
    }



    public void check(){
        String s;
        if(answer == viewAnswer){
            if(choiceBot == 1 && first == 1){
                score1+=2;
                textViewPointBot.setText(""+score1);
                score2--;
                textViewPointTop.setText(""+score2);
            }
            else if(choiceTop == 1 && first == 2){
                score2+=2;
                textViewPointTop.setText(""+score2);
                score1--;
                textViewPointBot.setText(""+score1);
            }
            else if (choiceBot == 1){
                score1+=2;
                textViewPointBot.setText(""+score1);
                score2--;
                textViewPointTop.setText(""+score2);
            }
            else if(choiceTop == 1){
                score2+=2;
                textViewPointTop.setText(""+score2);
                score1--;
                textViewPointBot.setText(""+score1);
            }
        }
        else {
            if(choiceBot == 2 && first == 1){
                score1+=2;
                textViewPointBot.setText(""+score1);
                score2--;
                textViewPointTop.setText(""+score2);
            }
            else if(choiceTop == 2 && first == 2){
                score2+=2;
                textViewPointTop.setText(""+score2);
                score1--;
                textViewPointBot.setText(""+score1);
            }
            else if (choiceBot == 2){
                score1+=2;
                textViewPointBot.setText(""+score1);
                score2--;
                textViewPointTop.setText(""+score2);
            }
            else if (choiceTop == 2){
                score2+=2;
                textViewPointTop.setText(""+score2);
                score1--;
                textViewPointBot.setText(""+score1);
            }
        }
        if(score1 >= 15 || score2 >= 15){
            timer.cancel();
            timer.onFinish();
        }
        first = 0;
        timerTimer.cancel();
        timerTimer = new TimerTimer((valueOfTime+1)*1000,1000);
        time = valueOfTime;
        timerTimer.start();
        newCondition();
    }
    int time = valueOfTime;


    public void home(View view){
//        showInterstitial();
        finish();
        overridePendingTransition(R.anim.slidein2, R.anim.slideout2);
    }


    public void trueBottom(View view) {
        ButtonTrueBottom.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training_stroked);
        offBut(ButtonTrueBottom, ButtonFalseBottom);
        choiceBot = 1;
        if(first == 0){
            first = 1;
        }
    }

    public void falseBottom(View view) {
        ButtonFalseBottom.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training_stroked);
        offBut(ButtonTrueBottom, ButtonFalseBottom);
        choiceBot = 2;
        if(first == 0){
            first = 1;
        }
    }

    public void falseTop(View view) {
        ButtonFalseTop.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training_stroked);
        offBut(ButtonTrueTop, ButtonFalseTop);
        if(first == 0){
            first = 2;
        }
        choiceTop = 2;
    }

    public void trueTop(View view) {
        ButtonTrueTop.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training_stroked);
        offBut(ButtonTrueTop, ButtonFalseTop);
        if(first == 0){
            first = 2;
        }
        choiceTop = 1;
    }

    public void homeTraining(View view){
//        showInterstitial();
        finish();
        overridePendingTransition(R.anim.slidein2, R.anim.slideout2);
    }

    public void timer(){
//        textViewTimerTop.setText("" + time);
        textViewTimerTop.setText("" + time);
        textViewTimerBot.setText("" + time);
        time--;
        if(!ButtonTrueBottom.isClickable() && !ButtonTrueTop.isClickable() || time == -1){
            check();
        }
    }

    public void replay(View view){
        onBut();
        score1 = 0;
        score2 = 0;
        textViewPointTop.setText("" + score2);
        textViewPointBot.setText("" + score1);
        timer.cancel();
        timerTimer.cancel();
        timer = new DoneTimer(valueOfTime*1000*x, (valueOfTime+1)*1000);
        timerTimer = new TimerTimer((valueOfTime+1)*1000,1000);
        fnl.setVisibility(View.INVISIBLE);
        time = valueOfTime;
        timerTimer.start();
        timer.start();
        newCondition();
    }

    public  void finalMenu(){
        fnl.setVisibility(View.VISIBLE);
        if(score1 > score2){
            finText.setText("Победил первый игрок");
        }
        else if(score1 < score2){
            finText.setText("Победил второй игрок");
        }
        else{
            finText.setText("Ничья");
        }
    }

    class DoneTimer extends CountDownTimer {

        public DoneTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {

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


    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            startGame();
        }
    }

    private void startGame() {
        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
        if (!interstitialAd.isLoading() && !interstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);
        }

    }
}

// ПОТОКИ ПОТОКИ
//class MyThread extends Thread {
//    TFMultiPlayer tf = new TFMultiPlayer();
//    @Override
//    public void run() {
//        super.run();
//        tf.newCondition();
//    }
//}
// В РАЗРАБОТКЕ В РАЗРАБОТКЕ