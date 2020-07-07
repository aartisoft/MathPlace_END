package com.math4.user.mathplace.olymp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.math4.user.mathplace.R;
import com.math4.user.mathplace.Something;

import java.util.ArrayList;

public class MainOlymp extends AppCompatActivity {


    static ArrayList<ArrayList<String>> AllTask=new ArrayList<ArrayList<String>>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_olymp);

    }




    public void share(MenuItem item){
        final Intent intent = new Intent(Intent.ACTION_SEND);
        Intent intent1= Something.share(intent);
        try {
            startActivity(Intent.createChooser(intent1,"Поделиться с помощью:" ));
        }
        catch (android.content.ActivityNotFoundException e)
        {
            Toast.makeText(this, "Что-то пошло не так", Toast.LENGTH_SHORT).show();
        }
    }


}
