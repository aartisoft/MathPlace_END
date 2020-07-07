package com.math4.user.mathplace;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint ePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    static ArrayList<Path> path;
    static ArrayList<Integer> pathC;
    Rect rect;
    Path p;

    public MySurfaceView(Context context) {
        super(context);
        path = new ArrayList<>();
        pathC = new ArrayList<>();
        p = new Path();
        rect = new Rect();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK);
        ePaint.setStyle(Paint.Style.STROKE);
        ePaint.setStrokeWidth(50);
        ePaint.setColor(Color.WHITE);
    }

    public MySurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getHolder().addCallback(this);
        path = new ArrayList<Path>();
        pathC = new ArrayList<>();
        p = new Path();
        rect = new Rect();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK);
        ePaint.setStyle(Paint.Style.STROKE);
        ePaint.setStrokeWidth(50);
        ePaint.setColor(Color.WHITE);
        Log.e("DRAFT","CREATED");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(Draft.isdrawing) {
            if (action == MotionEvent.ACTION_DOWN) {
                p.moveTo(event.getX(), event.getY());
            } else if (action == MotionEvent.ACTION_MOVE) {
                p.lineTo(event.getX(), event.getY());
                path.add(p);
                p = new Path();
                p.moveTo(event.getX(), event.getY());
                pathC.add(0);
            } else if (action == MotionEvent.ACTION_UP) {
                p.lineTo(event.getX(), event.getY());
                path.add(p);
                p = new Path();
                pathC.add(0);
            }
        }
        else{
            if (action == MotionEvent.ACTION_DOWN) {
                p.moveTo(event.getX(), event.getY());
            } else if (action == MotionEvent.ACTION_MOVE) {
                p.lineTo(event.getX(), event.getY());
                path.add(p);
                p = new Path();
                p.moveTo(event.getX(), event.getY());
                pathC.add(1);
            } else if (action == MotionEvent.ACTION_UP) {
                p.lineTo(event.getX(), event.getY());
                path.add(p);
                p = new Path();
                pathC.add(1);
            }
//            Toast.makeText(getContext(),"Draft",Toast.LENGTH_SHORT).show();
//            if (action == MotionEvent.ACTION_DOWN) {
//                p.moveTo(event.getX(), event.getY());
//            } else if (action == MotionEvent.ACTION_MOVE) {
//                p.lineTo(event.getX(), event.getY());
////                path.add(p);
//                for(int i = 0;i < path.size();i++){
//                    if(path.get(i) == p){
//                        path.remove(i);
//                    }
//                }
//                p = new Path();
//                p.moveTo(event.getX(), event.getY());
//                pathC.add(1);
//            } else if (action == MotionEvent.ACTION_UP) {
//                p.lineTo(event.getX(), event.getY());
////                path.add(p);
//                for(int i = 0;i < path.size();i++){
//                    if(path.get(i) == p){
//                        path.remove(i);
//                    }
//                }
//                p = new Path();
//                pathC.add(1);
//            }
        }
//        for(int i = 0;i < path.size()-1;i++){
//            for(int j = i+1;j < path.size();j++){
//                if(path.get(i) == path.get(j) && pathC.get(j) == 1){
//                    path.remove(i);
//                    path.remove(j);
//                }
//            }
//        }

        invalidate();
        Log.e("DRAFT","TOUCHED");
        return true;
    }


    public void showLine(){
        for(int i = 0;i < path.size()-1;i++){
            for(int j = i+1;j < path.size();j++){
                if(path.get(i) == path.get(j) && pathC.get(j) == 1){
                    path.remove(i);
                    path.remove(j);
                }
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false);
    }
    static Canvas thisCanvas;
    @Override
    public void onDraw(Canvas canvas) {
        for(int i = 0;i < path.size();i++) {
            if (path.get(i) != null){
                Log.e("checkcheckPath",path.get(i).toString());
                if(pathC.get(i) == 0) {
                    canvas.drawPath(path.get(i), paint);
//                    path.remove(i);
                }
                else if (pathC.get(i) == 1){
                    canvas.drawPath(path.get(i), ePaint);
//                    path.remove(i);
                }
            }
        }
//        path.clear();
//        canvas.drawPath(thisCanvas,paint);
//        thisCanvas=canvas;
        Log.e("DRAFT","DRAWN: " + path.size() + " Paths");
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}

