package com.ljc.clock.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ljc.clock.MainActivity;
import com.ljc.clock.R;
import com.ljc.clock.mytool.ToolsOfImg;

/**
 * 这是一个继承了帧布局的时钟视图，可以通过类名直接在布局文件中调用
 * 他同时包含了各种方法和属性，用于对时钟进行各种操作
 */

public class ClockView extends FrameLayout {
    private MainActivity mActivity;
    private Time time = new Time();
    private ImageView view_second;
    private ImageView view_minute;
    private ImageView view_hour;
    private int currentSeconds;         //当前秒钟数，当秒钟数超过59时，回到0
    private int currentMinutes;         //当前分钟数，当分钟数超过59时，回到0
    private int currentHours;           //当前小时数，当分钟数超过12时，回到1
    private boolean isStop = true;     //当前时钟状态，true表示停止，false表示不停止

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clockview, this);
        view_second = (ImageView)findViewById(R.id.view_second);
        view_minute = (ImageView)findViewById(R.id.view_minute);
        view_hour = (ImageView)findViewById(R.id.view_hour);
        mActivity = new MainActivity();
        start();
    }
    /*
    开始旋转方法，启动一个子线程，来获取系统时间并更新指针UI
     */
    public void start(){
        if(isStop){
            isStop = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!isStop) {             //如果不是停状态
                            time.setToNow();
                            currentHours = time.hour;
                            currentMinutes = time.minute;
                            currentSeconds = time.second;
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    view_second.setRotation(currentSeconds*6);
                                    view_minute.setRotation(currentMinutes*6);
                                    view_hour.setRotation(currentHours*30+30*currentMinutes/60.0f);
                                }
                            });
                        }
                    }
                }
            }).start();
        }
    }
    /*
    停止旋转方法，调用此方法，即使子线程还在运行，但是并不会执行更新Ui的代码
     */
    public void stop(){
        if(!isStop){
            isStop = true;
        }
    }
}
