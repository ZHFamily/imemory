package club.imemory.app.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import club.imemory.app.BuildConfig;
import club.imemory.app.R;
import club.imemory.app.util.AppManager;
import club.imemory.app.util.AppUtils;
import club.imemory.app.util.NavigatorUtil;
import club.imemory.app.util.SnackbarUtil;

/**
 * @Author: 张杭
 * @Date: 2017/4/22 26:11
 */

public class ThankActivity extends BaseActivity {

    /**
     * 启动ThankActivity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ThankActivity.class);
        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView tvVersion = (TextView) findViewById(R.id.tv_thank);
        try {
            tvVersion.setText(AppUtils.getRawString(this, R.raw.thank));
        } catch (IOException e) {
            tvVersion.setText(null);
            SnackbarUtil.ShortSnackbar(toolbar,"资源读取失败",0);
        }
    }
}
