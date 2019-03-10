package com.ljc.clock.myview;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ljc.clock.MainActivity;
import com.ljc.clock.R;
import com.ljc.clock.tools.NetConnectManager;
import com.ljc.clock.tools.SystemManager;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;


/**
 * 这是一个继承了帧布局的时钟视图，可以通过类名直接在布局文件中调用
 * 它同时包含了各种方法和属性，用于对时钟进行各种操作
 * 同时时钟视图下的所有按钮等相关操作的响应事件都在这个类中完成注册
 */

public class ClockView extends FrameLayout implements View.OnClickListener {
    private EditText editText_year;
    private EditText editText_month;
    private EditText editText_day;
    private EditText editText_hour;
    private EditText editText_minute;
    private EditText editText_second;
    private Button button_ok;
    private Button button_cancel;
    private static Thread mThread;
    private static boolean isRun =true;
    private static boolean isStop = true;     //当前时钟状态，true表示停止，false表示不停止
    private Button button_setByHand;
    private Button button_setByNet;
    private Dialog dialog;
    public static MainActivity mActivity;
    private Time time = new Time();
    /*
    为什么要将这四个控件定义为静态的？
    因为这个视图（也就是这个当前的这个类）是被加载到碎片（fragment）中的，碎片会有生命周期
    这些控件会随着碎片的生命周期的改变而被销毁或被创建，就会产生新的视图，旧的视图里面的控件引用
    就会指向一个无效的（不是当前碎片加载）的视图，同时因为是在旧的（已无效）视图中开启的子线程，
    所以子线程不断更新的就是一个无效的视图中的控件，这样没有任何意义，导致试图被销毁再被创建后，
    子线程无效地（用户看不到）更新控件。
    如果设置为静态，那么无论视图被重新创建多少次，新旧视图中的控件引用都是指向同一个最新的在碎片中加载
    的控件。
     */
    private static ImageView view_second;
    private static ImageView view_minute;
    private static ImageView view_hour;
    private static TextView text_date;
    private int currentSeconds;         //当前秒钟数，当秒钟数超过59时，回到0
    private int currentMinutes;         //当前分钟数，当分钟数超过59时，回到0
    private int currentHours;           //当前小时数，当分钟数超过12时，回到1


    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clockview, this);
        view_second = (ImageView) findViewById(R.id.view_second);
        view_minute = (ImageView) findViewById(R.id.view_minute);
        view_hour = (ImageView) findViewById(R.id.view_hour);
        mActivity = MainActivity.mainActivity;
        button_setByHand = (Button) findViewById(R.id.button_settime_byhand);
        button_setByHand.setOnClickListener(this);
        button_setByNet = (Button) findViewById(R.id.button_settime_bynet);
        button_setByNet.setOnClickListener(this);
        text_date = (TextView) findViewById(R.id.text_date);
        displayDate(text_date);
        start();
    }

    /*
    开始旋转方法，启动一个子线程，来获取系统时间并不断更新指针UI
     */
    public void start() {
        isRun = true;
        isStop = false;
        if (mThread==null) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isRun) {
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
                                    view_second.setRotation(currentSeconds * 6);
                                    view_minute.setRotation(currentMinutes * 6);
                                    view_hour.setRotation(currentHours * 30 + 30 * currentMinutes / 60.0f);
                                    displayDate(text_date);
                                }
                            });
                        }
                    }
                }
            });
            mThread.start();        //当一个线程已经处于执行状态，那么不能再次调用它的start()方法，否则报错
        }
    }

    /*
    停止旋转方法，调用此方法，子线程结束，并将线程对象回收，变为null
     */
    public static void onExit() {
        if (!isStop) {
            isStop = true;
            isRun = false;
            mThread = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_settime_byhand: {
                showDialog();
            }
            break;
            case R.id.button_settime_bynet: {
                ProgressDialog progressDialog = new ProgressDialog(mActivity);
                progressDialog.setTitle("正在获取网络时间");
                progressDialog.setMessage("请等待...");
                progressDialog.show();
                NetConnectManager.getNetTime(progressDialog,this);
            }
            break;
            case R.id.button_settime_ok:{
                String year = editText_year.getText().toString();
                String month = editText_month.getText().toString();
                String day = editText_day.getText().toString();
                String hour = editText_hour.getText().toString();
                String minute = editText_minute.getText().toString();
                String second = editText_second.getText().toString();
                setSysTime(year,month,day,hour,minute,second);
            }
            break;
            case R.id.button_settime_cancel:{
                dialog.cancel();
            }
        }
    }

    private void displayDate(TextView text_date) {
        time.setToNow();
        String strTime = time.year + "年" + (time.month+1) + "月" + time.monthDay + "日" + " " + "周" + switchWeek(time.weekDay);
        text_date.setText(strTime);
    }

    /*
    这个方法用来弹出设置时间的对话框（现已改为跳转系统设置界面进行时间设置，此方法作废）
     */
    public void showDialog() {
        dialog = new Dialog(mActivity, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View dialog_view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_settime, null);
        //将布局设置给Dialog
        dialog.setContentView(dialog_view);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
        //想要获得已存在的控件的实例，必须要使用该空件所在的活动或者所在的视图的findView()方法
        editText_year = (EditText)dialog.findViewById(R.id.textedit_year);
        editText_month= (EditText)dialog.findViewById(R.id.textedit_month);
        editText_day= (EditText)dialog.findViewById(R.id.textedit_day);
        editText_hour= (EditText)dialog.findViewById(R.id.textedit_hour);
        editText_minute= (EditText)dialog.findViewById(R.id.textedit_minute);
        editText_second= (EditText)dialog.findViewById(R.id.textedit_second);
        button_ok = (Button)dialog.findViewById(R.id.button_settime_ok);
        button_cancel= (Button)dialog.findViewById(R.id.button_settime_cancel);
        button_ok.setOnClickListener(this);
        button_cancel.setOnClickListener(this);
    }

    /*
    设置系统时间，这个方法需要两条件之一：一是本应用是系统应用，二是本应用获取root权限
    因为这两个条件都比较难以满足，所以设置时间直接改为跳转到系统设置界面进行设置，此方法作废
     */
    public void setSysTime(String year, String month, String day, String hour, String minute, String second) {
        year = switchStr(year);
        month = switchStr(month);
        day = switchStr(day);
        hour = switchStr(hour);
        minute = switchStr(minute);
        second = switchStr(second);
        String command = "date -s " +"\""+year+month+day + " " + hour +":"+ minute +":"+ second +"\""+"\n";
        if (!SystemManager.RootCommand(command)){
            Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
            mActivity.startActivity(intent);
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mActivity,"设备未获取root权限\n无法修改系统时间\n已跳转到系统设置",Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private String switchStr(String str) {
        if (str.length() == 1) {
            str = '0' + str;
        }
        return str;
    }

    private String switchWeek(int n) {
        if (n==0){
            return " ";
        }
        String[] strs = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
        return strs[n - 1];
    }
}