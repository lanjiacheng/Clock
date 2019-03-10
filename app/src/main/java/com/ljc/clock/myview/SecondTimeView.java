package com.ljc.clock.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ljc.clock.MainActivity;
import com.ljc.clock.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class SecondTimeView extends FrameLayout implements View.OnClickListener{
    private TextView text_left;
    private Button button_start;
    private Button button_reset;
    private ImageView img_left;
    private static boolean isRun;
    private static boolean isStop = true;
    private static Thread mThread;
    private long lastminutes;
    private long lastseconds;
    private long lastlittelseconds;
    private List<ImageView> imageViewList;
    private Class drawable;
    private Field field;
    private static ImageView img_minute_1;
    private static ImageView img_minute_2;
    private static ImageView img_second_1;
    private static ImageView img_second_2;
    private static ImageView img_millisecond_1;
    private static ImageView img_millisecond_2;
    private static TextView text_msg;
    private static long milliSeconds;
    private MainActivity mActivity;

    public SecondTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.second_time_view, this);
        mActivity = MainActivity.mainActivity;
        button_start = (Button)findViewById(R.id.button_second_start);
        button_reset = (Button)findViewById(R.id.button_second_reset);
        text_msg = (TextView)findViewById(R.id.text_msg);
        text_left = (TextView)findViewById(R.id.text_second_left);
        img_left = (ImageView)findViewById(R.id.img_second_left);
        img_minute_1 = (ImageView)findViewById(R.id.img_minute_1);
        img_minute_2 = (ImageView)findViewById(R.id.img_minute_2);
        img_second_1 = (ImageView)findViewById(R.id.img_second_1);
        img_second_2 = (ImageView)findViewById(R.id.img_second_2);
        img_millisecond_1 = (ImageView)findViewById(R.id.img_millisecond_1);
        img_millisecond_2 = (ImageView)findViewById(R.id.img_millisecond_2);
        imageViewList = new ArrayList();
        imageViewList.add(img_minute_1);
        imageViewList.add(img_minute_2);
        imageViewList.add(img_second_1);
        imageViewList.add(img_second_2);
        imageViewList.add(img_millisecond_1);
        imageViewList.add(img_millisecond_2);
        drawable = R.drawable.class;
        button_start.setOnClickListener(this);
        button_reset.setOnClickListener(this);
    }

    private void start() {
        isStop = false;
        isRun = true;
        if (mThread == null) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isRun) {
                        try {
                            Thread.sleep(52);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!isStop) {             //如果不是停状态
                            milliSeconds += 53;
                            setTimeImage(milliSeconds);
                        }
                    }
                }
            });
            mThread.start();
        }
    }

    private void setTimeImage(long allMilliSeconds) {
        long minutes = allMilliSeconds/1000/60;
        long seconds = allMilliSeconds/1000%60;
        long littelseconds = allMilliSeconds%1000/10;
        String resNameOfMinute1 = "num_"+minutes/10;
        String resNameOfMinute2 = "num_"+minutes%10;
        String resNameOfSecond1 = "num_"+seconds/10;
        String resNameOfSecond2 = "num_"+seconds%10;
        String resNameOfLittlesecond1 = "num_"+littelseconds/10;
        String resNameOfLittlesecond2 = "num_"+littelseconds%10;
        for (ImageView temp_img:imageViewList){
            if (temp_img == img_minute_1&&lastminutes!=minutes){
                setOneTimeImg(resNameOfMinute1,img_minute_1);
            }else if (temp_img == img_minute_2&&lastminutes!=minutes){
                setOneTimeImg(resNameOfMinute2,img_minute_2);
            }else if (temp_img == img_second_1&&lastseconds!=seconds){
                setOneTimeImg(resNameOfSecond1,img_second_1);
            }else if (temp_img == img_second_2&&lastseconds!=seconds){
                setOneTimeImg(resNameOfSecond2,img_second_2);
            }else if (temp_img == img_millisecond_1&&lastlittelseconds!=littelseconds){
                setOneTimeImg(resNameOfLittlesecond1,img_millisecond_1);
                Log.d("ljc",resNameOfLittlesecond1);
            }else if (temp_img == img_millisecond_2&&lastlittelseconds!=littelseconds){
                setOneTimeImg(resNameOfLittlesecond2,img_millisecond_2);
            }
        }
        lastminutes = minutes;
        lastseconds = seconds;
        lastlittelseconds = littelseconds;
    }
    private void setOneTimeImg(String nameInDrawable, final ImageView currentImg){
        int res_ID = 0;
        try {
            field = drawable.getField(nameInDrawable);  //通过drawable类中的字段名获取字段
            res_ID = field.getInt(field.getName());     //通过字段获取字段值
            if (res_ID!=0) {
                final int finalRes_ID = res_ID;
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentImg.setImageResource(finalRes_ID);      //将字段值（即id）设置给指定图片控件
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    停止方法，调用此方法后，子线程还在执行，但是不会使时间增加，也不会更新UI
     */
    private void stop(){
        isStop = true;
    }
    /*
    退出应用时调用此方法，结束本视图内的进程
     */
    public static void onExit(){
        isStop = true;
        isRun = false;
        mThread = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_second_start:{
                if (isStop){
                    start();
                    img_left.setImageResource(R.drawable.pause);
                    text_msg.setText("已开始");
                    text_left.setText("暂停");
                }else {
                    stop();
                    img_left.setImageResource(R.drawable.start);
                    text_msg.setText("已暂停");
                    text_left.setText("开始");
                }
            }
            break;
            case R.id.button_second_reset:{
                onExit();
                milliSeconds = 0;
                setTimeImage(0);
                img_left.setImageResource(R.drawable.start);
                text_msg.setText("准备计时");
                text_left.setText("开始");
            }
            break;
        }
    }
}