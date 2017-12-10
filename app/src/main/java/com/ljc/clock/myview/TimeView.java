package com.ljc.clock.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ljc.clock.MainActivity;
import com.ljc.clock.R;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class TimeView extends FrameLayout {
    private MainActivity mActivity;
    private ImageView view_littlewheel;
    private ImageView view_bigwheel;
    private TextView text_time;
    private long milliseconds;                //毫秒数
    private boolean isStop = true;     //当前秒表状态，true表示停止，false表示不停止
    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.count_time_view, this);
        view_bigwheel = (ImageView)findViewById(R.id.view_bigwheel);
        view_littlewheel = (ImageView)findViewById(R.id.view_littlewheel);
        text_time = (TextView)findViewById(R.id.text_time);
        mActivity = new MainActivity();
        start();
    }
    public void start(){
        if(isStop){
            isStop = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!isStop&&mActivity!=null) {             //如果不是停状态
                            milliseconds += 10;
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    text_time.setText((milliseconds/1000/60)+":"+(milliseconds/1000)+":"+(milliseconds%1000/10));
                                }
                            });
                            if(milliseconds%100==0) {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        view_littlewheel.setRotation(6*(milliseconds/10));
                                        view_bigwheel.setRotation(3*(milliseconds/20));
                                    }
                                });
                            }
                        }
                    }
                }
            }).start();
        }
    }
    private void stop(){
        if(!isStop){
            isStop = true;
        }
    }
}
