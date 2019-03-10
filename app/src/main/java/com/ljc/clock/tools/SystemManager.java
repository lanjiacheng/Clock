package com.ljc.clock.tools;

import android.app.Activity;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 这是一个工具类，该类中提供一些系统级的操作
 */

public class SystemManager extends Activity {
    /*
    执行root用户的linux命令的方法，执行root用户命令需要root权限，如果没有该权限，那么命令执行失败，返回false
    如果命令成功执行，那么返回true
     */
        public static boolean RootCommand(String command){
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command+"\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        }finally {
            try {
                if (os!=null){
                    os.close();
                }
            } catch (Exception e) {
            }
        }
        Log.d("ljc","root suc");
        return true;
    }
}
