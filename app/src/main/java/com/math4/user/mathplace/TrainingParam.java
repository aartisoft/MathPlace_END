package com.math4.user.mathplace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.math4.user.mathplace.R;

//import static com.math4.user.mathplace.MainActivity.searchItem;
import static com.math4.user.mathplace.MainActivity.searchItem;
import static com.math4.user.mathplace.MainActivity.settingsItem;
import static com.math4.user.mathplace.MainActivity.textLoad;
import static com.math4.user.mathplace.MainActivity.toolbar;

public class TrainingParam extends Fragment {
    SeekBar mSeekBar;
    TextView seekTime;
    View view;
    private AppCompatActivity appCompatActivity;
    static ClassTraining classTraining1,classTraining2,classTrainingPlus,classTrainingMinus,classTrainingMultiply,classTrainingDivide,classTrainingVvod,classTrainingTF;
    static ImageButton button1,button2,buttonV,buttonTF;
    int[][] image1={{R.drawable.plus, R.drawable.plus2},{R.drawable.minus, R.drawable.minus2},{R.drawable.multiply, R.drawable.multiply2},
            {R.drawable.del, R.drawable.del2}};
    int[][] oneTwo={{R.drawable.one, R.drawable.one2},{R.drawable.two, R.drawable.two2}};
    int[][] trueFalse={{R.drawable.vvod, R.drawable.vvod2},{R.drawable.tr, R.drawable.tr2}};
    static int valueOfTime = 5;

    @Override
    // Переопределяем метод onCreateView
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.activity_training_param,container,false);
        searchItem.setVisible(false);
        settingsItem.setVisible(false);
        textLoad.setVisibility(View.INVISIBLE);
        seekTime=view.findViewById(R.id.textViewTimeTrainingParam);
        mSeekBar = view.findViewById(R.id.seekBarTimeTraining);

        LinearLayout linearLayout1=view.findViewById(R.id.linerLayoutPlayers);
        LinearLayout linearLayout2=view.findViewById(R.id.linerLayoutPl);
        LinearLayout linearLayout3=view.findViewById(R.id.linerLayoutMode);

        classTrainingPlus=new ClassTraining(false);
        ImageButton buttonP=classTrainingPlus.customTask(getActivity(),image1[0][0],image1[0][1],30,30);
        classTrainingPlus.click = true;
        linearLayout1.addView(buttonP);

        classTrainingMinus=new ClassTraining(false);
        ImageButton buttonM=classTrainingMinus.customTask(getActivity(),image1[1][0],image1[1][1],30,30);
        classTrainingMinus.click=true;
        linearLayout1.addView(buttonM);

        classTrainingMultiply=new ClassTraining(false);
        ImageButton buttonMu=classTrainingMultiply.customTask(getActivity(),image1[2][0],image1[2][1],30,30);
        classTrainingMultiply.click = true;
        linearLayout1.addView(buttonMu);

        classTrainingDivide=new ClassTraining(false);
        ImageButton buttonDiv=classTrainingDivide.customTask(getActivity(),image1[3][0],image1[3][1],30,30);
        classTrainingDivide.click = true;
        linearLayout1.addView(buttonDiv);
        ///
        classTraining1=new ClassTraining(false);
        button1=classTraining1.customTask(getActivity(),oneTwo[0][0],oneTwo[0][1],170,170);
        linearLayout2.addView(button1);

        ///

        classTraining2=new ClassTraining(true);
        button2=classTraining2.customTask(getActivity(),oneTwo[1][0],oneTwo[1][1],170,170);
        linearLayout2.addView(button2);


        classTraining1.choice = true;
        classTraining2.choice = true;
        classTraining2.number = true;
        update(classTraining1,button1);


        classTrainingVvod=new ClassTraining(false);
        buttonV=classTrainingVvod.customTask(getActivity(),trueFalse[0][0],trueFalse[0][1],170,170);
        linearLayout3.addView(buttonV);

        classTrainingTF=new ClassTraining(true);
        buttonTF=classTrainingTF.customTask(getActivity(),trueFalse[1][0],trueFalse[1][1],170,170);
        linearLayout3.addView(buttonTF);

        classTrainingVvod.mode = true;
        classTrainingTF.mode = true;
        classTrainingVvod.number = true;
        updateMode(classTrainingVvod,buttonV);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                valueOfTime = Integer.parseInt(String.valueOf(progress))/10+1;
                seekTime.setText(String.valueOf(valueOfTime));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    static public void update(ClassTraining classTraining3,ImageButton button3){
        if(classTraining3.number){
            button2.setImageResource(R.drawable.two);
            classTraining2.click = true;
            button1.setImageResource(R.drawable.one2);
            classTraining1.click = false;
        }
        else{
            button1.setImageResource(R.drawable.one);
            classTraining1.click = true;
            button2.setImageResource(R.drawable.two2);
            classTraining2.click = false;
        }
    }

    static public void updateMode(ClassTraining classTraining3,ImageButton button3){
        if(classTraining3.number){
            buttonV.setImageResource(R.drawable.vvod);
            classTrainingVvod.click = true;
            buttonTF.setImageResource(R.drawable.tr2);
            classTrainingTF.click = false;
        }
        else{
            buttonV.setImageResource(R.drawable.vvod2);
            classTrainingVvod.click = false;
            buttonTF.setImageResource(R.drawable.tr);
            classTraining2.click = true;
        }
    }


}

