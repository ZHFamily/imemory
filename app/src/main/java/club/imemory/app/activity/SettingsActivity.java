package club.imemory.app.activity;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.litepal.crud.DataSupport;

import club.imemory.app.R;
import club.imemory.app.db.User;
import club.imemory.app.util.AppManager;
import club.imemory.app.util.DataCleanManager;
import club.imemory.app.util.SnackbarUtil;

import static club.imemory.app.util.CrashHandler.CRASH_LOG_PATH;

/**
 * APP设置中心
 *
 * @Author: 张杭
 * @Date: 2017/3/25 12:12
 */

public class SettingsActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 启动SettingsActivity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
    }

    private CoordinatorLayout coordinator;
    private SwitchCompat switchNotification;
    private SwitchCompat switchOnlyWiFi;
    private SwitchCompat switchOpenWeather;

    private Boolean isNotification;
    private Boolean isOnlyWiFi;
    private Boolean isOpenWeather;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.set_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isNotification = prefs.getBoolean("isNotification", true);
        isOnlyWiFi = prefs.getBoolean("isOnlyWiFi", true);
        isOpenWeather = prefs.getBoolean("isOpenWeather", true);
        initView();
    }

    private void initView() {
        coordinator = (CoordinatorLayout) findViewById(R.id.coordinator);
        findViewById(R.id.btn_notification).setOnClickListener(this);
        findViewById(R.id.btn_only_wifi).setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_open_weather).setOnClickListener(this);
        findViewById(R.id.btn_check_update).setOnClickListener(this);
        findViewById(R.id.btn_help).setOnClickListener(this);
        findViewById(R.id.btn_user_agreement).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);
        LinearLayout logoutBtn = (LinearLayout) findViewById(R.id.logout);
        User user = DataSupport.findLast(User.class);
        if (user == null) {
            logoutBtn.setVisibility(View.GONE);
        }

        switchNotification = (SwitchCompat) findViewById(R.id.switch_notification);
        switchNotification.setChecked(isNotification);
        switchOnlyWiFi = (SwitchCompat) findViewById(R.id.switch_only_wifi);
        switchOnlyWiFi.setChecked(isOnlyWiFi);
        switchOpenWeather = (SwitchCompat) findViewById(R.id.switch_open_weather);
        switchOpenWeather.setChecked(isOpenWeather);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_notification:
                isNotification = isNotification ? false : true;
                switchNotification.setChecked(isNotification);
                AppManager.showToast("说了没这个功能，点了也没用");
                break;
            case R.id.btn_only_wifi:
                isOnlyWiFi = isOnlyWiFi ? false : true;
                switchOnlyWiFi.setChecked(isOnlyWiFi);
                AppManager.showToast("其实这个功能也没实现");
                break;
            case R.id.btn_clear:
                clear();
                break;
            case R.id.btn_open_weather:
                isOpenWeather = isOpenWeather ? false : true;
                switchOpenWeather.setChecked(isOpenWeather);
                AppManager.showToast("操作成功");
                break;
            case R.id.btn_check_update:
                SnackbarUtil.ShortSnackbar(coordinator, "最近应该不会更新", 0).show();
                //AppManager.showToast("最近应该不会更新");
                break;
            case R.id.btn_help:
                //AppManager.showToast("自己慢慢摸索把");
                SnackbarUtil.ShortSnackbar(coordinator, "自己慢慢摸索吧", 0).show();
                break;
            case R.id.btn_user_agreement:
                //AppManager.showToast("遵纪守法就好啦");
                SnackbarUtil.ShortSnackbar(coordinator, "遵纪守法就好啦", 0).show();
                break;
            case R.id.btn_logout:
                AppManager.showToast("注销成功");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataSupport.deleteAll(User.class);
                    }
                }).start();
                finish();
                break;
            default:
                break;
        }
    }

    private void clear() {
        Snackbar snackbar = Snackbar.make(coordinator, "确定要清除所有数据么！！！", Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        if (view != null) {
            view.setBackgroundColor(0xfff44336);
            ((Button) view.findViewById(R.id.snackbar_action)).setTextColor(0xffffffff);
        }
        snackbar.setAction("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataCleanManager.cleanApplicationData(SettingsActivity.this, CRASH_LOG_PATH);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AppManager.showToast("缓存已清除");
                            }
                        });
                    }
                }).start();
            }
        }).show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("isNotification", isNotification);
        editor.putBoolean("isOnlyWiFi", isOnlyWiFi);
        editor.putBoolean("isOpenWeather", isOpenWeather);
        editor.apply();
    }
}
