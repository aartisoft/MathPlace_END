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

public class MultiPlayer extends AppCompatActivity {
    Button ButtonTop1,ButtonTop2,ButtonTop3,ButtonTop4,
            ButtonBottom1,ButtonBottom2,ButtonBottom3,ButtonBottom4;
    TextView textViewConditionTop,textViewConditionBottom, textViewPointTop,textViewPointBot,textViewTimerBot,textViewTimerTop,scoreView,finText,textViewScoreRecord,textViewScoreRecordMenu;
    static int plus = 0,minus = 0,divide = 0,multiply = 0;
    Random random;
    int answer;
    private InterstitialAd interstitialAd;

    private static final String AD_UNIT_ID = "ca-app-pub-3206990026084887/4324193868";
    int valueOfTime = TrainingParam.valueOfTime;
    boolean choiceBot = false, choiceTop = false;
    // int time = valueOfTime;
    int first = 0;
    int x = 100000;
    ConstraintLayout fnl;
    boolean top=true,bot=true;

    boolean pls=false,mns=false,mlp=false,dvd=false;

    int score1 = 0, score2 = 0;


    DoneTimer timer = new DoneTimer(1000000000, (valueOfTime+1)*1000);
    TimerTimer timerTimer = new TimerTimer((valueOfTime+1)*1000,1000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        random=new Random();
        setContentView(R.layout.activity_multi_player);
        ButtonTop1=findViewById(R.id.buttonTop1);
        ButtonTop2=findViewById(R.id.buttonTop4);
        ButtonTop3=findViewById(R.id.buttonTop3);
        ButtonTop4=findViewById(R.id.buttonTop2);
        ButtonBottom1=findViewById(R.id.buttonBottom3);
        ButtonBottom2=findViewById(R.id.buttonBottom2);
        ButtonBottom3=findViewById(R.id.buttonBottom1);
        ButtonBottom4=findViewById(R.id.buttonBottom4);
        textViewConditionBottom=findViewById(R.id.textViewConditionBottom);
        textViewConditionTop=findViewById(R.id.textViewConditionTop);
        textViewPointTop=findViewById(R.id.textViewPointsTop);
        textViewPointBot=findViewById(R.id.textViewPointsBot);
        textViewTimerBot=findViewById(R.id.textViewTimerBotMP) ;
//        textViewTimerTop=findViewById(R.id.textViewTimerTopMP);
//        fnlBack = findViewById(R.id.imageViewFinal);
//        scoreView = findViewById(R.id.textViewScoreMain);
        finText=findViewById(R.id.textViewFinalMP);
        fnl=findViewById(R.id.constraintLayoutFinalMP);
        fnl.setVisibility(View.INVISIBLE);
        pls = TrainingParam.classTrainingPlus.click;
        mns = TrainingParam.classTrainingMinus.click;
        mlp = TrainingParam.classTrainingMultiply.click;
        dvd = TrainingParam.classTrainingDivide.click;
        timer.start();
        timerTimer.start();
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


    @Override
    protected void onResume(){
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

        onButBot();
        onButTop();
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
    public void exsponent(){

    }
    public void offButTop(){
        ButtonTop1.setClickable(false);
        ButtonTop2.setClickable(false);
        ButtonTop3.setClickable(false);
        ButtonTop4.setClickable(false);
        top = true;
    }
    public void onButTop(){
        ButtonTop1.setClickable(true);
        ButtonTop2.setClickable(true);
        ButtonTop3.setClickable(true);
        ButtonTop4.setClickable(true);
        ButtonTop1.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training);
        ButtonTop2.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training);
        ButtonTop3.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training);
        ButtonTop4.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training);
        top = false;
    }

    public void offButBot(){
        ButtonBottom1.setClickable(false);
        ButtonBottom2.setClickable(false);
        ButtonBottom3.setClickable(false);
        ButtonBottom4.setClickable(false);
        bot = true;
    }

    public void onButBot(){
        ButtonBottom1.setClickable(true);
        ButtonBottom2.setClickable(true);
        ButtonBottom3.setClickable(true);
        ButtonBottom4.setClickable(true);
        ButtonBottom1.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training);
        ButtonBottom2.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training);
        ButtonBottom3.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training);
        ButtonBottom4.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training);
        bot = false;
    }

    public void setParam(int val1,int val2,int val3,int val4,int ans,int a2,int b2, String sign){

        ButtonBottom1.setText(String.valueOf(val1));
        ButtonTop1.setText(String.valueOf(val1));
        ButtonBottom2.setText(String.valueOf(val2));
        ButtonTop2.setText(String.valueOf(val2));
        ButtonBottom3.setText(String.valueOf(val3));
        ButtonTop3.setText(String.valueOf(val3));
        ButtonBottom4.setText(String.valueOf(val4));
        ButtonTop4.setText(String.valueOf(val4));

        textViewConditionBottom.setText(a2+" "+sign+" "+b2+" = ");
        textViewConditionTop.setText(a2+" "+sign+" "+b2+" = ");
        answer = ans;
    }
    public boolean check_answer(String ans){
        if (Integer.parseInt(ans) == answer) {
            return true;
        }
        else
            return false;
    }
    public void answerBottom1(View view){
        String s;
        /*if(check_answer(ButtonBottom1.getText().toString())) {
            s = "" + (Integer.parseInt(textViewPointBot.getText().toString()) + 1);
            textViewPointBot.setText("" + s);
        }else{*/
        ButtonBottom1.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training_stroked);
        offButBot();
        if(first == 0)
            first = 1;
        choiceBot = check_answer(ButtonBottom1.getText().toString());
        //if(top && bot){
        //    newCondition();
        //  }
        //}
    }
    public void answerBottom2(View view){
        ButtonBottom2.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training_stroked);
        offButBot();
        if(first == 0)
            first = 1;
        choiceBot = check_answer(ButtonBottom2.getText().toString());
    }
    public void answerBottom3(View view){
        ButtonBottom3.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training_stroked);
        offButBot();
        if(first == 0)
            first = 1;
        choiceBot = check_answer(ButtonBottom3.getText().toString());
    }public void answerBottom4(View view){
        ButtonBottom4.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training_stroked);
        offButBot();
        if(first == 0)
            first = 1;
        choiceBot = check_answer(ButtonBottom4.getText().toString());
    }public void answerTop1(View view){
        ButtonTop1.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training_stroked);
        offButTop();
        if(first == 0)
            first = 2;
        choiceTop = check_answer(ButtonTop1.getText().toString());
    }public void answerTop2(View view){
        ButtonTop2.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training_stroked);
        offButTop();
        if(first == 0)
            first = 2;
        choiceTop = check_answer(ButtonTop2.getText().toString());
    }public void answerTop3(View view){
        ButtonTop3.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training_stroked);
        offButTop();
        if(first == 0)
            first = 2;
        choiceTop = check_answer(ButtonTop3.getText().toString());
    }public void answerTop4(View view){
        ButtonTop4.setBackgroundResource(R.drawable.btn_rounded_corner_ans_training_stroked);
        offButTop();
        if(first == 0)
            first = 2;
        choiceTop = check_answer(ButtonTop4.getText().toString());
    }

    public void home(View view){
//        Intent intent=new Intent(MultiPlayer.this,MainActivity.class);
//        startActivity(intent);
//        showInterstitial();
        finish();
        overridePendingTransition(R.anim.slidein2, R.anim.slideout2);
    }

    public void checktm(){
        int s;
        if(choiceBot && first == 1){
            score1+=2;
            score2--;
            //s = (Integer.parseInt(textViewPointBot.getText().toString()) + 1);
            textViewPointTop.setText("" + score2);
            textViewPointBot.setText("" + score1);
        }
        else if (choiceTop && first == 2){
            score2+=2;
            score1--;
            //s = (Integer.parseInt(textViewPointTop.getText().toString()) + 1);
            textViewPointTop.setText("" + score2);
            textViewPointBot.setText("" + score1);
        }
        else if (choiceBot){
            score1+=2;
            score2--;
            //s = (Integer.parseInt(textViewPointBot.getText().toString()) + 1);
            textViewPointTop.setText("" + score2);
            textViewPointBot.setText("" + score1);
        }
        else if (choiceTop){
            score2+=2;
            score1--;
            //s = (Integer.parseInt(textViewPointTop.getText().toString()) + 1);
            textViewPointTop.setText("" + score2);
            textViewPointBot.setText("" + score1);
        }
        first = 0;
        choiceTop = false;
        choiceBot = false;
        timerTimer.cancel();
        timerTimer = new TimerTimer((valueOfTime+1)*1000,1000);
        time = valueOfTime;
        timerTimer.start();
        newCondition();
        if(score1 >= 15 || score2 >= 15){
            timer.cancel();
            timer.onFinish();
        }
    }

    int time = valueOfTime;

    public void homeTraining(View view){
//        showInterstitial();
        finish();
        overridePendingTransition(R.anim.slidein2, R.anim.slideout2);
    }

    public void timer(){
//        textViewTimerTop.setText("" + time);
        textViewTimerBot.setText("" + time);
        time--;
        if(!ButtonBottom4.isClickable() && !ButtonTop4.isClickable() || time == -1){
            checktm();
        }
    }

    public void replay(View view){
        onButBot();
        onButTop();
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
