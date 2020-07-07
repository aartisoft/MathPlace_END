package com.math4.user.mathplace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.math4.user.mathplace.R;

public class Draft extends AppCompatActivity {


    SurfaceView work;
    static public Boolean  isdrawing = true;
    TextView textViewTask;
    String text;
    ScrollView scrollView;
    LinearLayout linearLayout;
    ImageButton imageButtonPen,imageButtonErase,imageButtonBin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);
        Bundle bundle=getIntent().getExtras();
//        text=bundle.getString("text");
        Toolbar toolbar = findViewById(R.id.toolbar_taskDraft);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Черновик");
        work = findViewById(R.id.mySurfaceView);
//        scrollView=findViewById(R.id.scrollViewSolution);
//        textViewTask=findViewById(R.id.textViewTaskSol);
//        textViewTask.setText(text);
        linearLayout=findViewById(R.id.linearLayoutSol);
        imageButtonPen=findViewById(R.id.pen);
        imageButtonErase=findViewById(R.id.eraser);
        imageButtonBin=findViewById(R.id.Trash);
//        setContentView(R.layout.activity_draft);
//
////        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        View view=new SurfaceView(this);
//        LayoutInflater ltInflater = getLayoutInflater();
//        LinearLayout linearLayout=findViewById(R.id.linearLayoutDraft);
//        ltInflater.inflate(R.layout.activity_main_menu, linearLayout, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void share(MenuItem item){
        final Intent intent = new Intent(Intent.ACTION_SEND);
        Intent intent1=Something.share(intent);
        try {
            startActivity(Intent.createChooser(intent1,"Поделиться с помощью:" ));
        }
        catch (android.content.ActivityNotFoundException e)
        {
            Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
        }
    }


    public void eraser(View view) {
        imageButtonBin.setBackgroundColor(Color.parseColor("#ECE8E8"));
        imageButtonErase.setBackgroundColor(Color.parseColor("#ECE8E8"));
        imageButtonPen.setBackgroundColor(Color.parseColor("#ECE8E8"));
        imageButtonErase.setBackgroundColor(Color.parseColor("#763DCA"));
        Toast.makeText(getApplicationContext(),"Выбран ластик",Toast.LENGTH_SHORT).show();
        isdrawing = false;
    }

    public void pen(View view) {
        imageButtonBin.setBackgroundColor(Color.parseColor("#ECE8E8"));
        imageButtonErase.setBackgroundColor(Color.parseColor("#ECE8E8"));
        imageButtonPen.setBackgroundColor(Color.parseColor("#ECE8E8"));
        imageButtonPen.setBackgroundColor(Color.parseColor("#763DCA"));
        Toast.makeText(getApplicationContext(),"Выбран карандаш",Toast.LENGTH_SHORT).show();
        isdrawing = true;
    }

    public void trash(View view){
        imageButtonBin.setBackgroundColor(Color.parseColor("#ECE8E8"));
        imageButtonErase.setBackgroundColor(Color.parseColor("#ECE8E8"));
        imageButtonPen.setBackgroundColor(Color.parseColor("#ECE8E8"));
        imageButtonPen.setBackgroundColor(Color.parseColor("#763DCA"));
        isdrawing=true;
        Toast.makeText(getApplicationContext(),"Нажмите по экрану чтобы удалить",Toast.LENGTH_SHORT).show();;
        com.math4.user.mathplace.MySurfaceView.path.clear();
        com.math4.user.mathplace.MySurfaceView.pathC.clear();
//        com.example.user.mathplace.MySurfaceView.showLine();
//        MySurfaceView.pa
    }

    class MySurfaceView extends View {

        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Path path;

        public MySurfaceView(Context context) {
            super(context);
            path = new Path();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setColor(Color.WHITE);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                path.moveTo(event.getX(), event.getY());
            } else if (action == MotionEvent.ACTION_MOVE) {
                path.lineTo(event.getX(), event.getY());
            } else if (action == MotionEvent.ACTION_UP) {
                path.lineTo(event.getX(), event.getY());
            }
            invalidate();
            return true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (path != null) canvas.drawPath(path, paint);
        }
    }
}
