package club.imemory.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.AppUtils;

import club.imemory.app.R;
import club.imemory.app.base.BaseActivity;
import club.imemory.app.util.AppManager;
import club.imemory.app.util.NavigatorUtil;

/**
 * @Author: 张杭
 * @Date: 2017/3/26 22:11
 */

public class AboutActivity extends BaseActivity{

    /**
     * 启动SettingsActivity
     */
    public static void actionStart(Context context, String... strings) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.about_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText(AppUtils.getAppVersionName(this)+"-build-"+AppUtils.getAppVersionCode(this));

        LinearLayout btnOpenSource = (LinearLayout) findViewById(R.id.btn_open_source_url);
        btnOpenSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigatorUtil.openInBrowser(AboutActivity.this,getString(R.string.open_source_url));
            }
        });

        LinearLayout btnAboutAuthor = (LinearLayout) findViewById(R.id.btn_about_author);
        btnAboutAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.showToast("作者很低调，张杭很低调");
            }
        });

        LinearLayout btnOpenInMarket = (LinearLayout) findViewById(R.id.btn_open_in_market);
        btnOpenInMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigatorUtil.openInMarket(AboutActivity.this);
            }
        });

        LinearLayout btnAdviceFeedback = (LinearLayout) findViewById(R.id.btn_advice_feedback);
        btnAdviceFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigatorUtil.openEmail(AboutActivity.this,
                        "zhcppy@qq.com",
                        "来自 imemory" + AppUtils.getAppVersionName(AboutActivity.this) + " 的客户端反馈",
                        "设备信息：Android " + Build.VERSION.RELEASE + " - " + Build.MANUFACTURER + " - " + Build.MODEL + "\n\n"
                );
            }
        });

    }
}
