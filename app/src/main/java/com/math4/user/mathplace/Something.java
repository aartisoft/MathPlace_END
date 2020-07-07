package com.math4.user.mathplace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.math4.user.mathplace.R;

import java.util.ArrayList;
import java.util.Random;


public class Something {
    static ArrayList<String> answer;
    static Boolean check_search;
    static View itemViewPrev;
    public static Intent share(Intent intent){
        intent.setType("text/plain");
        String textToSend="Поделиться с помощью:";
        final Random random = new Random();
        int a = random.nextInt(2);
//        if (a == 1) {
            textToSend = "Пошли решать со мной? Скачивай приложение MathPlace по ссылке "+String.valueOf("https://play.google.com/store/apps/details?id=com.math4.user.mathplace&hl=ru");
//        }else {
//            textToSend = "Часто cмотришь мемы ? Поверь,в MathPlace гораздо интереснее. Пошли решать со мной)";
//        }
        intent.putExtra(Intent.EXTRA_TEXT, textToSend);
        return intent;
    }

//    public static Intent share2(Intent intent, String text){
//        intent.setType("text/plain");
//        String textToSend="Поделиться с помощью:";
//        final Random random = new Random();
//        int a = random.nextInt(2);
////        if (a == 1) {
//        textToSend = "Пошли решать со мной? Скачивай приложение MathPlace по ссылке "+text;
////        }else {
////            textToSend = "Часто cмотришь мемы ? Поверь,в MathPlace гораздо интереснее. Пошли решать со мной)";
////        }
//        intent.putExtra(Intent.EXTRA_TEXT, textToSend);
//        return intent;
//    }

    public static Button addBookmarkButton(final String name,final Context context){
        final Button button=new Button(context);
        RelativeLayout.LayoutParams MyParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,200);
        MyParams.topMargin=16;
        MyParams.leftMargin=8;
        MyParams.rightMargin=8;
        button.setLayoutParams(MyParams);
        button.setText(name);
        button.setTextColor(Color.parseColor("#FFFFFF"));
        button.setTextSize(20);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, Topic.class);
                intent.putExtra("thisTheme",button.getText().toString());
                context.startActivity(intent);
            }
        });
        return button;
    }
    public static Button addButton(final String name, final Context context,final int i){
        final Button button=new Button(context);
        RelativeLayout.LayoutParams MyParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        MyParams.topMargin=16;
        MyParams.leftMargin=8;
        MyParams.rightMargin=8;
        button.setLayoutParams(MyParams);
        button.setText(name);
        button.setTextColor(Color.parseColor("#FFFFFF"));
        button.setTextSize(20);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Topic.class);
                intent.putExtra("thisTheme",button.getText().toString());
                context.startActivity(intent);
            }
        });
        button.setBackgroundResource(R.drawable.btn_rounded_corner);
        return button;
    }
    public static ProgressBar addProgressBar(Context context,String name,int i){
        ContextThemeWrapper newContext = new ContextThemeWrapper(context, R.style.CircularProgress);
        ProgressBar progressBar=new ProgressBar(newContext,null,android.R.attr.progressBarStyleHorizontal);
        RelativeLayout.LayoutParams MyParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20);
        MyParams.leftMargin=16;
        MyParams.rightMargin=16;
        MyParams.topMargin=16;
        progressBar.setLayoutParams(MyParams);
        progressBar.setProgress(Integer.parseInt(Distrib.resultTask.get(String.valueOf("0")))*10);
        progressBar.setMax(100);
        progressBar.setTag(String.valueOf(i));
        return  progressBar;
    }


}
