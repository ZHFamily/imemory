package club.imemory.app.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import club.imemory.app.R;
import club.imemory.app.base.BaseActivity;
import club.imemory.app.util.AppManager;


/**
 * APP设置中心
 */
public class SettingsActivity extends BaseActivity implements View.OnClickListener{
    /**
     * 启动SettingsActivity
     */
    public static void actionStart(Context context, String... strings) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    private SwitchCompat switchNotification;
    private SwitchCompat switchOnlyWiFi;
    private SwitchCompat switchOpenWeather;


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
        initView();
    }

    private void initView(){
        LinearLayout mNotificationBtn = (LinearLayout) findViewById(R.id.btn_notification);
        mNotificationBtn.setOnClickListener(this);
        LinearLayout mOnlyWiFiBtn = (LinearLayout) findViewById(R.id.btn_only_wifi);
        mOnlyWiFiBtn.setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);
        LinearLayout mOpenWeatherBtn = (LinearLayout) findViewById(R.id.btn_open_weather);
        mOpenWeatherBtn.setOnClickListener(this);
        findViewById(R.id.btn_check_update).setOnClickListener(this);
        findViewById(R.id.btn_help_feedback).setOnClickListener(this);
        findViewById(R.id.btn_user_agreement).setOnClickListener(this);

        switchNotification = (SwitchCompat) findViewById(R.id.switch_notification);
        switchNotification.setChecked(true);
        switchOnlyWiFi = (SwitchCompat) findViewById(R.id.switch_only_wifi);
        switchOnlyWiFi.setChecked(true);
        switchOpenWeather = (SwitchCompat) findViewById(R.id.switch_open_weather);
        switchOpenWeather.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_notification:
                switchNotification.setChecked(false);
                AppManager.showToast("说了没这个功能，点了也没用");
                break;
            case R.id.btn_only_wifi:
                switchOnlyWiFi.setChecked(false);
                AppManager.showToast("WiFi");
                break;
            case R.id.btn_clear:
                AppManager.showToast("缓存已清除");
                break;
            case R.id.btn_open_weather:
                switchOpenWeather.setChecked(false);
                AppManager.showToast("天气");
                break;
            case R.id.btn_check_update:
                AppManager.showToast("更新");
                break;
            case R.id.btn_help_feedback:
                AppManager.showToast("帮助与反馈");
                break;
            case R.id.btn_user_agreement:
                AppManager.showToast("用户协议");
                break;
            default:
                break;
        }

    }
}
