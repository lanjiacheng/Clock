package com.ljc.clock.tools;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.ljc.clock.myview.ClockView;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 这是一个工具类，勒种提供各种方法用于网络连接的操作
 */

public class NetConnectManager {
    public static void getNetTime(final ProgressDialog progressDialog, final ClockView clockView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;//取得资源对象
                URLConnection uc = null;
                try {
                    url = new URL("http://www.baidu.com");
                    uc = url.openConnection();//生成连接对象
                    uc.setConnectTimeout(1000);
                    uc.connect(); //发出连接
                    TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
                    cal.setTimeInMillis(uc.getDate());
                    int yearOfYear = cal.get(Calendar.YEAR);
                    int monthOfYear = cal.get(Calendar.MONTH)+1;
                    int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                    int hourOfDay=cal.get(Calendar.HOUR_OF_DAY);
                    int minute=cal.get(Calendar.MINUTE);
                    int second=cal.get(Calendar.SECOND);
                    final String timeStr = yearOfYear+"年"+monthOfYear+"月"+dayOfMonth+"日"+"\n"+
                            hourOfDay+"时"+minute+"分"+second+"秒";
                    Thread.sleep(1000);     //稍作等待
                    //如果获取网络时间成功，直接打印并设置
                    ClockView.mActivity.runOnUiThread(new Runnable() {  //打印
                        @Override
                        public void run() {
                            Toast.makeText(ClockView.mActivity,"当前网络时间为：\n"+ timeStr,Toast.LENGTH_LONG).show();
                        }
                    });
                    //设置
                     clockView.setSysTime(yearOfYear+"",monthOfYear+"",dayOfMonth+"",hourOfDay+"",minute+"",+second+"");
                } catch (Exception e) {
                    //如果失败，打印错误
                    e.printStackTrace();
                    ClockView.mActivity.runOnUiThread(new Runnable() {  //打印
                        @Override
                        public void run() {
                            Toast.makeText(ClockView.mActivity,"获取网络时间失败\n请检查你的网络",Toast.LENGTH_LONG).show();
                        }
                    });
                }finally {
                    //最后关掉提示
                    progressDialog.cancel();
                }
            }
        }).start();
    }
}
