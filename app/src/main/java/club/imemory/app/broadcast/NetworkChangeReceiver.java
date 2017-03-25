package club.imemory.app.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import club.imemory.app.util.AppManager;

/**
 * 网络变化广播接收器
 *
 * @Author: 张杭
 * @Date: 2017/3/12 17:05
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    /**
     * 网络是否发生变化
     */
    private Boolean isChangeNetWork = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && isChangeNetWork) {
                AppManager.showToast("移动网络已连接");
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI && isChangeNetWork) {
                AppManager.showToast("WiFi已连接");
            }
        } else {
            isChangeNetWork = true;
            AppManager.showToast("网络已关闭");
        }

    }
}
