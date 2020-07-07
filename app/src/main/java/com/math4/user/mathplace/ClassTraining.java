package com.math4.user.mathplace;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.math4.user.mathplace.R;

public class ClassTraining {
    Boolean click;
    Boolean number = false;
    Boolean choice = false;
    Boolean mode = false;
    ClassTraining(Boolean click){
        this.click=click;
    }
    public ImageButton customTask(final Context context,final int image1, final int image2, final int left, final int right) {
        final ImageButton button = new ImageButton(context);
        LinearLayout.LayoutParams MyParams = new LinearLayout.LayoutParams(150, 200);
        MyParams.leftMargin = left;
        MyParams.rightMargin = right;
        button.setLayoutParams(MyParams);
        button.setImageResource(image1);
        button.setScaleType(ImageView.ScaleType.FIT_XY);
        button.setBackgroundResource(R.color.background);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(choice == false && mode == false) {
                    if (click) {
                        button.setImageResource(image2);
                        click = false;
                    } else {
                        button.setImageResource(image1);
                        click = true;
                    }
                }
                else if(choice && mode == false){
                    TrainingParam.update(ClassTraining.this, button);
                }
                else {
                    TrainingParam.updateMode(ClassTraining.this, button);
                }
            }
        });
        return button;

    }

}