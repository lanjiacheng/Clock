package com.ljc.clock.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ljc.clock.MainActivity;
import com.ljc.clock.R;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public class WheelView extends FrameLayout {
    MainActivity mainActivity;
    private TextView text_time;
    private float lastX;
    private float lastY;
    private float previousX;
    private float previousY;
    private float currentX;
    private float currentY;
    private static float degrees;
    private long milliseconds;
    private ImageView view_littlewheel;
    private ImageView view_bigwheel;
    public WheelView(Context context, AttributeSet attrs) {
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.wheelview, this);
        mainActivity = MainActivity.mainActivity;
        view_bigwheel = (ImageView)findViewById(R.id.view_bigwheel);
        view_littlewheel = (ImageView)findViewById(R.id.view_littlewheel);
    }
    //   触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (text_time == null){
            text_time = (TextView)mainActivity.findViewById(R.id.text_time);
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (previousX == 0.0f && previousY == 0.0f){
                previousX = event.getRawX();
                previousY = event.getRawY();
            }
            currentX = event.getRawX();
            currentY = event.getRawY();
            if (lastY==0&&lastX==0){
                lastX = event.getRawX();
                lastY = event.getRawY();
            }
            if (event.getX()>(this.getLeft()+this.getRight())/2.0f){
                if (currentY>lastY){
                    degrees += Math.abs(currentY-lastY)*0.1f;
                }
                if (currentY<lastY){
                    degrees -= Math.abs(currentY-lastY)*0.1f;
                }
            }
            if (event.getX()<(this.getLeft()+this.getRight())/2.0f){
                if (currentY>lastY){
                    degrees -= Math.abs(currentY-lastY)*0.1f;
                }
                if (currentY<lastY){
                    degrees += Math.abs(currentY-lastY)*0.1f;
                }
            }
            if (event.getY()<(this.getTop()+this.getBottom())/2.0f){
                if (currentX>lastX){
                    degrees += Math.abs(currentX-lastX)*0.1f;
                }
                if (currentX<lastX){
                    degrees -= Math.abs(currentX-lastX)*0.1f;
                }
            }
            if (event.getY()>(this.getTop()+this.getBottom())/2.0f){
                if (currentX>lastX){
                    degrees -= Math.abs(currentX-lastX)*0.1f;
                }
                if (currentX<lastX){
                    degrees += Math.abs(currentX-lastX)*0.1f;
                }
            }
            view_bigwheel.setRotation(degrees);
            view_littlewheel.setRotation(-2*degrees);
            milliseconds = (long) (degrees*40);
            if (milliseconds<0){
                milliseconds = 0;
                degrees = 0;
            }
            setText_time(milliseconds);
            lastX = event.getRawX();
            lastY = event.getRawY();
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {      //中断的触摸事件，即点击的一瞬间
        previousX = 0.0f;
        previousY = 0.0f;
        lastX = 0.0f;
        lastY = 0.0f;
        return super.onInterceptTouchEvent(ev);
    }
    private void setText_time(long milliseconds){
        int minutes = (int)milliseconds/1000/60;
        int seconds = (int)milliseconds/1000%60;
        text_time.setText(switchStr(String.valueOf(minutes))+":"+switchStr(String.valueOf(seconds)+":"+"00"));
    }
    private String switchStr(String str) {
        if (str.length() < 2) {
            str = '0' + str;
        }
        return str;
    }
    public static float getDegrees(){
        return degrees%360.0f;
    }
    public static void setDegrees(float d){
        degrees = d%360.0f;
    }
}