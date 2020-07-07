package com.math4.user.mathplace;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.math4.user.mathplace.R;

public class ClassSearch {
    Button button;
    LinearLayout linearLayout;
    public Button getButton(Context context,String text){
        Button button=new Button(context);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200);
        layoutParams.topMargin=20;
        button.setLayoutParams(layoutParams);
        button.setText(text);
        button.setTextSize(22);
        button.setBackgroundResource(R.drawable.btn_rounded_corner);
        return button;
    }
    public ClassSearch(Context context,LinearLayout linearLayout,String text){
        this.button=getButton(context,text);
        this.linearLayout=linearLayout;
        linearLayout.addView(this.button);
    }
    public void delete(){
        (this.linearLayout).removeView(this.button);
    }

}
