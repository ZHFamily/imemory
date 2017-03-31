package club.imemory.app.activity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import club.imemory.app.broadcast.ForceOfflineReceiver;
import club.imemory.app.broadcast.NetworkChangeReceiver;
import club.imemory.app.util.AppManager;

/**
 * @Author: 张杭
 * @Date: 2017/3/11 14:09
 */


public class BaseActivity extends AppCompatActivity {

    /**
     * 强制下线广播接收器
     */
    private ForceOfflineReceiver mFOReceiver;

    /**
     * 网络变化广播接收器
     */
    private NetworkChangeReceiver mNCReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.logI("BaseActivity", getClass().getSimpleName() + "创建");
        AppManager.addActivity(this);

    }

    /**
     * （1）只有当活动处于栈顶的时候（即处在运行状态）才能接收到强制下线广播
     */
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        //（1）注册强制下线广播
        intentFilter.addAction("club.imemory.life.broadcast.FORCE_OFFLINE");
        mFOReceiver = new ForceOfflineReceiver();
        registerReceiver(mFOReceiver, intentFilter);
        //（2）注册网络变化广播
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mNCReceiver = new NetworkChangeReceiver();
        registerReceiver(mNCReceiver, intentFilter);
    }

    /**
     * 当活动不在栈顶时取消动态注册的广播
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mFOReceiver != null) {
            unregisterReceiver(mFOReceiver);
        }
        if (mNCReceiver != null) {
            unregisterReceiver(mNCReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.logI("BaseActivity", getClass().getSimpleName() + "销毁");
        AppManager.removeActivity(this);
    }
}
