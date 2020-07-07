package com.math4.user.mathplace.olymp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.math4.user.mathplace.R;

import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.math4.user.mathplace.olymp.MainOlymp.AllTask;

public class AddMaterialTask extends AppCompatActivity {

    TextInputLayout textInputLayoutText,textInputLayoutAns,textInputLayoutHard;
    TextInputEditText addAns, addText;
    AutoCompleteTextView autoCompleteTextViewHard;
    Boolean checkSolution=false;
    LinearLayout linearLayoutNew;
    View view;
    Boolean edit;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_material);
        Bundle bundle=getIntent().getExtras();
        edit=bundle.getBoolean("edit",false);
        position=bundle.getInt("position",0);
        Toolbar toolbar = findViewById(R.id.toolbarAddMaterial);
        setSupportActionBar(toolbar);
        checkSolution=false;
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        if(edit){
            setTitle("Редактировать задачу");
        }else {
            setTitle("Добавить задачу");
        }


        textInputLayoutText=findViewById(R.id.textFieldText);
        textInputLayoutAns=findViewById(R.id.textFieldAns);
        textInputLayoutHard=findViewById(R.id.textFieldHard);
        addAns=findViewById(R.id.AddAns);
        addText=findViewById(R.id.AddTextTask);
        final LinearLayout linearLayout = findViewById(R.id.linearLayoutSolution);
        TextView textViewAddSolution = findViewById(R.id.addSolution);


        autoCompleteTextViewHard = findViewById(R.id.hardAddMaterial);

        String[] cats = getResources().getStringArray(R.array.hardOlymp);
        List<String> catList = Arrays.asList(cats);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.list_item, catList);
        autoCompleteTextViewHard.setAdapter(adapter);
        boolean checkSol=true;
        if(edit){
            addText.setText(AllTask.get(position).get(0));
            addAns.setText(AllTask.get(position).get(1));
            autoCompleteTextViewHard.setText(AllTask.get(position).get(2));
            if(!AllTask.get(position).get(3).equals("null")){
                checkSol=false;
                Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.olymp2);
                linearLayout.removeAllViews();
                LinearLayout linearLayoutMain = findViewById(R.id.linearLayoutMain);
                LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                linearLayoutNew = newLinearLayout();
                ((TextView)view.findViewById(R.id.textView2)).setText(AllTask.get(position).get(3));
                linearLayoutNew.startAnimation(animation2);
                checkSolution = true;
                linearLayoutMain.addView(linearLayoutNew, layoutParam);            }
        }

        if(checkSol) {
            textViewAddSolution.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.olymp1);
                    linearLayout.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.olymp2);
                            linearLayout.removeAllViews();
                            LinearLayout linearLayoutMain = findViewById(R.id.linearLayoutMain);
                            LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            linearLayoutNew = newLinearLayout();
                            linearLayoutNew.startAnimation(animation2);
                            checkSolution = true;
                            linearLayoutMain.addView(linearLayoutNew, layoutParam);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            });
        }

    }


    public LinearLayout newLinearLayout(){
// create a layout
        view = LayoutInflater.from(this).inflate(R.layout.textviewlayout, null);
// create a layout param for the layout
        return (LinearLayout)view;
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_make_olymp,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                showInterstitial();
                this.finish();
                return true;
            case R.id.action_ok:
                boolean r1=false,r2=false,r3=false,r4=true;
                if(addAns.getText().length()>0){
                    r2=true;
                }else{
                    addAns.setError("Условие задачи пустое");
                }
                if(autoCompleteTextViewHard.getText().length()>0){
                    r3=true;
                }else{
                    textInputLayoutHard.setError("Выберите вариант");
                }
                if(addText.getText().length()>0){
                    r1=true;
                }else{
                    addText.setError("Выберите вариант");
                }

                if(checkSolution){
                    if(((TextInputEditText)view.findViewById(R.id.textView2)).getText().toString().length()==0){
                        r4=false;
                    }
                }

                if(r1&&r2&&r3){
                    ArrayList arrayList=new ArrayList();
                    arrayList.add(addText.getText().toString());
                    arrayList.add(addAns.getText().toString());
                    arrayList.add(autoCompleteTextViewHard.getText().toString());
                    if(checkSolution){
                        arrayList.add(((TextInputEditText)view.findViewById(R.id.textView2)).getText().toString());
                    }else{
                        arrayList.add("null");
                    }
                    if(edit){
                        AllTask.set(position,arrayList);
                    }else {
                        AllTask.add(arrayList);
                    }
                    this.finish();
                }
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}