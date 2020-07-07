package com.math4.user.mathplace;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.math4.user.mathplace.R;

public class ClassAccount {
    public TextView getTextViewTitle(Context context,String text){
        final Context contextThemeWrapper = new ContextThemeWrapper(context, R.style.MyTextStyleTitle);
        TextView textView=new TextView(contextThemeWrapper);
        LinearLayout.LayoutParams myParams=new LinearLayout.LayoutParams(500,75);
        myParams.setMargins(10,10,10,0);
        textView.setLayoutParams(myParams);
//        textView.setTextSize(22);
//        textView.setTextColor(Color.parseColor("#000000"));
        textView.setText(text);
        return textView;
    }
    public TextView getTextViewDescription(Context context,String text){
        final Context contextThemeWrapper = new ContextThemeWrapper(context, R.style.MyTextStyleDescription);
        TextView textView=new TextView(contextThemeWrapper);
        LinearLayout.LayoutParams myParams=new LinearLayout.LayoutParams(500,145);
        myParams.setMargins(10,0,10,10);
        textView.setLayoutParams(myParams);
        textView.setText(text);
//        textView.setTextSize(20);
        return textView;
    }
    public ProgressBar getprogressBar(Context context,int progress){
        final Context contextThemeWrapper = new ContextThemeWrapper(context, R.style.CircularProgress);
        ProgressBar progressBar=new ProgressBar(contextThemeWrapper,null,android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams myParams=new LinearLayout.LayoutParams(600,18);
        myParams.setMargins(10,0,10,10);
        progressBar.setLayoutParams(myParams);
        progressBar.setProgress(progress);
        progressBar.setMax(100);
        return progressBar;
    }
    public ImageView getImageView(Context context){
        ImageView imageView=new ImageView(context);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(235,250);
        layoutParams.setMargins(10,10,10,10);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(R.drawable.achiv2);
        return imageView;
    }
    public LinearLayout getLinearLayoutBig(Context context){
        LinearLayout linearLayout=new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(1000, 260);
        layoutParams.setMargins(10,0,10,50);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        return linearLayout;
    }
    public LinearLayout getLinearLayoutSmall(Context context){
        LinearLayout linearLayout=new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(10,10,10,10);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }
}
