package com.example.lab8;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class MainActivity extends AppCompatActivity{
    int screen = 1;
    float firstFingerX,firstFingerY,secondFingerX,secondFingerY, diameter;
    int randomX1, randomX2, randomY1, randomY2, x, y;
    boolean isPinch;
    boolean isPinchDone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View myView = findViewById(android.R.id.content);
        RandomCoordinates();
        setContentView(new Rectangle(MainActivity.this));
        myView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                if (screen>1 && screen<=3) screen--;
                else return;
                RandomCoordinates();
                if(screen==1){
                    setContentView(new Rectangle(MainActivity.this));
                }
                if(screen==2){
                    setContentView(new Line(MainActivity.this));
                }
                if(screen==3){
                    setContentView(new Oval(MainActivity.this));
                }
            }
            public void onSwipeLeft() {
                if(screen>=1 && screen<3) screen++;
                else return;
                RandomCoordinates();
                if(screen==1){
                    setContentView(new Rectangle(MainActivity.this));
                }
                if(screen==2){
                    setContentView(new Line(MainActivity.this));

                }
                if(screen==3){
                    setContentView(new Oval(MainActivity.this));
                }
            }
        });
    }

    private int RandomNumber(int min,int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }
    private void RandomCoordinates(){
        randomX1 = RandomNumber(50,1000);
        randomX2 = RandomNumber(50,1000);
        randomY1 = RandomNumber(50,1800);
        randomY2 = RandomNumber(50,1800);
    }

    public class Rectangle extends View {
        public Rectangle(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            paint.setColor(Color.parseColor("#CD5C5C"));
            canvas.drawRect(randomX1, randomY1, randomX2, randomY2, paint);
        }
    }

    public class Line extends View {
        public Line(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            paint.setColor(Color.parseColor("#CD5C5C"));
            canvas.drawLine(randomX1, randomY1, randomX2, randomY2, paint);
        }
    }

    public class Oval extends View {
        public Oval(Context context) {
            super(context);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            paint.setColor(Color.parseColor("#CD5C5C"));
            canvas.drawOval(randomX1, randomY1, randomX2, randomY2, paint);
        }
    }

    public class Circle extends View {
        public Circle(Context context) {
            super(context);
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            paint.setColor(Color.parseColor("#7CFC00"));
            x = (int)((firstFingerX+secondFingerX)/2);
            y = (int)((firstFingerY+secondFingerY)/2);
            diameter = (float) Math.hypot(firstFingerX-secondFingerX, firstFingerY-secondFingerY);
            canvas.drawCircle(x, y, diameter/2, paint);
        }
    }

    private boolean checkIfObjectFits(){
        int dx = max(abs(x - randomX1), abs(randomX2 - x));
        int dy = max(abs(y - randomY1), abs(randomY2 - y));
        if (((diameter/2) * (diameter/2)) >= (dx * dx) + (dy * dy)) return true;
        else return false;
    }

    public class OnSwipeTouchListener  implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener (Context ctx){
            gestureDetector = new GestureDetector(ctx, new GestureListener());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int touchCount = event.getPointerCount();
            isPinchDone=false;
            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                {
                    if(touchCount == 2)
                    {
                        return false;
                    }
                    else if(touchCount==1)
                    {
                        gestureDetector.onTouchEvent(event);
                        setContentView(R.layout.activity_main);
                        return true;
                    }
                }
                break;

                case MotionEvent.ACTION_MOVE:
                {
                    for(int i = 0; i < touchCount; ++i)
                    {
                        int pointerIndex = i;
                        int pointerId = event.getPointerId(pointerIndex);
                        if(pointerId == 0)
                        {
                            firstFingerX = event.getX(pointerIndex);
                            firstFingerY = event.getY(pointerIndex);
                        }
                        if(pointerId == 1)
                        {
                            secondFingerX = event.getX(pointerIndex);
                            secondFingerY = event.getY(pointerIndex);
                        }
                    }
                    if(touchCount==2)
                    {
                        isPinch=true;
                        setContentView(new Circle(MainActivity.this));
                        return true;
                    }
                    else if (touchCount==1)
                    {
                        gestureDetector.onTouchEvent(event);
                        return true;
                    }
                }
                break;

                case MotionEvent.ACTION_UP:
                {
                    if(isPinch)
                    {
                        if (checkIfObjectFits()) {
                            ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 200);
                            toneGenerator.startTone(ToneGenerator.TONE_CDMA_CONFIRM, 3000);
                        }
                        else{
                            ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 200);
                            toneGenerator.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 3000);

                        }

                        isPinch=false;
                        return true;
                    }
                    else
                    {
                        gestureDetector.onTouchEvent(event);
                        if(screen==1){
                            setContentView(new Rectangle(MainActivity.this));
                        }
                        if(screen==2){
                            setContentView(new Line(MainActivity.this));
                        }
                        if(screen==3){
                            setContentView(new Oval(MainActivity.this));
                        }
                        return true;
                    }
                }

            }
            return false;
        }


        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (abs(diffX) > abs(diffY)) {
                        if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }
    }
}