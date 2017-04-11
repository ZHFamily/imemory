package club.imemory.app.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import club.imemory.app.R;
import club.imemory.app.util.AppManager;
import club.imemory.app.util.DataCleanManager;

import static club.imemory.app.util.CrashHandler.CRASH_LOG_PATH;


/**
 * APP设置中心
 */
public class SettingsActivity extends BaseActivity implements View.OnClickListener{
    /**
     * 启动SettingsActivity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

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
        isNotification = prefs.getBoolean("isNotification",true);
        isOnlyWiFi = prefs.getBoolean("isOnlyWiFi",true);
        isOpenWeather = prefs.getBoolean("isOpenWeather",true);
        initView();
    }

    private void initView(){
        findViewById(R.id.btn_notification).setOnClickListener(this);
        findViewById(R.id.btn_only_wifi).setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_open_weather).setOnClickListener(this);
        findViewById(R.id.btn_check_update).setOnClickListener(this);
        findViewById(R.id.btn_help).setOnClickListener(this);
        findViewById(R.id.btn_user_agreement).setOnClickListener(this);

        switchNotification = (SwitchCompat) findViewById(R.id.switch_notification);
        switchNotification.setChecked(isNotification);
        switchOnlyWiFi = (SwitchCompat) findViewById(R.id.switch_only_wifi);
        switchOnlyWiFi.setChecked(isOnlyWiFi);
        switchOpenWeather = (SwitchCompat) findViewById(R.id.switch_open_weather);
        switchOpenWeather.setChecked(isOpenWeather);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_notification:
                isNotification = isNotification?false:true;
                switchNotification.setChecked(isNotification);
                AppManager.showToast("说了没这个功能，点了也没用");
                break;
            case R.id.btn_only_wifi:
                isOnlyWiFi = isOnlyWiFi?false:true;
                switchOnlyWiFi.setChecked(isOnlyWiFi);
                AppManager.showToast("其实这个功能也没实现");
                break;
            case R.id.btn_clear:
                DataCleanManager.cleanApplicationData(this,CRASH_LOG_PATH);
                AppManager.showToast("缓存已清除，请重启应用");
                break;
            case R.id.btn_open_weather:
                isOpenWeather = isOpenWeather?false:true;
                switchOpenWeather.setChecked(isOpenWeather);
                AppManager.showToast("操作成功");
                break;
            case R.id.btn_check_update:
                AppManager.showToast("最近应该不会更新");
                break;
            case R.id.btn_help:
                AppManager.showToast("自己慢慢摸索把");
                break;
            case R.id.btn_user_agreement:
                AppManager.showToast("遵纪守法就好啦");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("isNotification",isNotification);
        editor.putBoolean("isOnlyWiFi",isOnlyWiFi);
        editor.putBoolean("isOpenWeather",isOpenWeather);
        editor.apply();
    }
}
