package club.imemory.app.util;

import android.app.Activity;
import android.content.Context;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author: 张杭
 * @Date: 2017/3/9 20:42
 */

public class AppManager {

    //App常量定义
    public static final String APP_NAME = "IMEMORY";
    //控制Log打印
    private static boolean debug = true;
    //关于Toast
    private static Toast toast;
    //关于Activity管理
    private static List<Activity> activities = new ArrayList<>();

    /**
     * 防止Toast重复弹出
     */
    public static void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(ApplicationUtil.getContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 错误Log信息
     */
    public static void logE(String TAG, String msg) {
        if (debug) {
            Log.e("memory|" + TAG, msg);
        }
    }

    /**
     * 正确Log信息
     */
    public static void logI(String TAG, String msg) {
        if (debug) {
            Log.i("memory|" + TAG, msg);
        }
    }

    /**
     * 添加activity
     */
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 移除activity
     */
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 关闭所有activity，退出程序
     */
    public static void finishAll() {
        AppManager.logI("AppManager", "程序成功关闭");
        for (Activity activity : activities) {
            activity.finish();
        }
        activities.clear();
        Process.killProcess(Process.myPid());
    }

}
