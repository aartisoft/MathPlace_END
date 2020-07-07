package com.math4.user.mathplace;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.math4.user.mathplace.R;

import java.util.Date;

public class Hello extends AppCompatActivity {

  Fragment[] m={new Hello1(),new Hello2(),new Hello3(),new Auth()};



  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.fragment_chetnost);
      Adapter2 adapter=new Adapter2(getSupportFragmentManager(),m.length,m);
      ViewPager viewPager = findViewById(R.id.viewpagerhello);
      viewPager.setAdapter(adapter); // устанавливаем адаптер
      viewPager.setCurrentItem(0); // выводим второй экран
      final ImageView imageView1=findViewById(R.id.imageView1);
      final ImageView imageView2=findViewById(R.id.imageView22);
      final ImageView imageView3=findViewById(R.id.imageView3);
      final ImageView imageView4=findViewById(R.id.imageView4);
      imageView1.setImageResource(R.drawable.roundedbutton_red);
      imageView2.setImageResource(R.drawable.roundedbutton_white);
      imageView3.setImageResource(R.drawable.roundedbutton_white);
      viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
          @Override
          public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
          @Override
          public void onPageSelected(int position) {
              if(position==0){
                  imageView1.setImageResource(R.drawable.roundedbutton_red);
                  imageView2.setImageResource(R.drawable.roundedbutton_white);
                  imageView3.setImageResource(R.drawable.roundedbutton_white);
                  imageView4.setImageResource(R.drawable.roundedbutton_white);
              }
              else if(position==1){
                  imageView1.setImageResource(R.drawable.roundedbutton_white);
                  imageView2.setImageResource(R.drawable.roundedbutton_red);
                  imageView3.setImageResource(R.drawable.roundedbutton_white);
                  imageView4.setImageResource(R.drawable.roundedbutton_white);
              }else if(position==2){
                  imageView1.setImageResource(R.drawable.roundedbutton_white);
                  imageView2.setImageResource(R.drawable.roundedbutton_white);
                  imageView3.setImageResource(R.drawable.roundedbutton_red);
                  imageView4.setImageResource(R.drawable.roundedbutton_white);
              }else{
                  imageView1.setImageResource(R.drawable.roundedbutton_white);
                  imageView2.setImageResource(R.drawable.roundedbutton_white);
                  imageView3.setImageResource(R.drawable.roundedbutton_white);
                  imageView4.setImageResource(R.drawable.roundedbutton_red);
              }
          }

          @Override
          public void onPageScrollStateChanged(int state) { }
      });
  }
    @Override
    public void onBackPressed(){
//        Toast.makeText(getApplicationContext(),"You Are Not Allowed to Exit the App",     Toast.LENGTH_SHORT).show();
    }







    public void back(View view){
        Intent intent=new Intent(Hello.this,MainActivity.class);
        SharedPreferences sPref = getSharedPreferences("data_base",MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt("use",1);
        ed.commit();
        startActivity(intent);
  }
    public  void ClickLog(View view){
        Intent intent=new Intent(Hello.this, LogIn.class);
        startActivity(intent);
    }


}