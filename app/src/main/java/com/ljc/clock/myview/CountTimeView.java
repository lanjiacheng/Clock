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

public class CountTimeView extends FrameLayout implements View.OnClickListener {
    private static Thread mThread;
    private static boolean isRun = false;
    private static boolean isStop = true;     //当前状态计时器，true表示停止，false表示不停止
    private TextView text_count_left;
    private ImageView img_count_left;
    private MainActivity mActivity;
    private static float degrees;
    private Button button_start;
    private Button button_reset;
    private static ImageView view_littlewheel;
    private static ImageView view_bigwheel;
    private static TextView text_time;
    private static long milliseconds;         //毫秒数

    public CountTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.count_time_view, this);
        mActivity = new MainActivity();
        view_bigwheel = (ImageView) findViewById(R.id.view_bigwheel);
        view_littlewheel = (ImageView) findViewById(R.id.view_littlewheel);
        text_time = (TextView) findViewById(R.id.text_time);
        button_start = (Button) findViewById(R.id.button_count_start);
        button_reset = (Button) findViewById(R.id.button_count_reset);
        text_count_left = (TextView) findViewById(R.id.text_count_left);
        img_count_left = (ImageView) findViewById(R.id.img_count_left);
        button_start.setOnClickListener(this);
        button_reset.setOnClickListener(this);
        if (isStop==false){
            img_count_left.setImageResource(R.drawable.pause);
            text_count_left.setText("暂停");
        }
    }

    public void start() {
        isRun = true;
        isStop = false;
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
                            if (milliseconds == 0) {
                                milliseconds = getMillisecondsFromStr(text_time.getText().toString());
                            }
                            if (milliseconds > 0) {
                                milliseconds -= 53;
                                degrees -= 10;
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String minutes = switchStr(String.valueOf((milliseconds / 1000 / 60)));
                                        String seconds = switchStr(String.valueOf((milliseconds / 1000 % 60)));
                                        String littleSeconds = switchStr(String.valueOf((milliseconds % 1000 / 10)));
                                        text_time.setText(minutes + ":" + seconds + ":" + littleSeconds);
                                        view_littlewheel.setRotation(-2 * degrees);
                                        view_bigwheel.setRotation(degrees);
                                    }
                                });
                            } else {
                                milliseconds = 0;
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        text_time.setText("00:00:00");
                                        text_count_left.setText("开始");
                                        img_count_left.setImageResource(R.drawable.start);
                                        isRun = false;
                                    }
                                });
                                stop();
                                WheelView.setDegrees(0);
                            }
                        }
                    }
                }
            });
            mThread.start();
        }
    }

    /*
    暂停倒计时，暂停后，既不会更新UI也不会计时
     */
    private void stop() {
        isStop = true;
    }

    /*
    退出应用时调用此方法，结束本视图内的进程
     */
    public static void onExit() {
        isStop = true;
        isRun = false;
        mThread = null;
    }

    private String switchStr(String str) {
        if (str.length() < 2) {
            str = '0' + str;
        }
        return str;
    }

    private long getMillisecondsFromStr(String str) {
        if (str.split(":").length < 2) {
            return 0;
        }
        String minuteStr = str.split(":")[0];
        String secondStr = str.split(":")[1];
        return Long.valueOf(minuteStr) * 60 * 1000 + Long.valueOf(secondStr) * 1000;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_count_start: {
                if (isStop == true) {
                    degrees = WheelView.getDegrees();
                    start();
                    text_count_left.setText("暂停");
                    img_count_left.setImageResource(R.drawable.pause);
                } else {
                    WheelView.setDegrees(degrees);
                    stop();
                    text_count_left.setText("继续");
                    img_count_left.setImageResource(R.drawable.start);
                }
            }
            break;
            case R.id.button_count_reset: {
                stop();
                WheelView.setDegrees(0);
                degrees = 0;
                view_littlewheel.setRotation(0);
                view_bigwheel.setRotation(0);
                milliseconds = 0;
                text_time.setText("转动齿轮设置时间");
                text_count_left.setText("开始");
                img_count_left.setImageResource(R.drawable.start);
                onExit();
            }
            break;
        }
    }
}