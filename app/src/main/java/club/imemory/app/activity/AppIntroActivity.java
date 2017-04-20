package club.imemory.app.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import club.imemory.app.R;

/**
 * @Author: 张杭
 * @Date: 2017/4/18 22:10
 */

public class AppIntroActivity extends AppIntro {

    /**
     * 启动AppIntroActivity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, AppIntroActivity.class);
        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("简洁界面",
                "完全按照google的Material Design规范设计开发\n还有NB的转场动画",
                R.drawable.screenshot_imemory1, Color.parseColor("#00bcd4")));
        addSlide(AppIntroFragment.newInstance("路径记录",
                "将每次出行的路径保留\n回忆起来更立体",
                R.drawable.screenshot_imemory2, Color.parseColor("#5c6bc0")));
        addSlide(AppIntroFragment.newInstance("实时天气",
                "全国天气信息想查那就能查那\n壁纸每天自动更换，每天都是不一样的美",
                R.drawable.screenshot_imemory3, Color.parseColor("#4caf50")));

        setBarColor(Color.parseColor("#222222"));
        setSeparatorColor(Color.parseColor("#AAAAAA"));

        setDoneText("完成");
        setSkipText("跳过");
        showSkipButton(true);

        //进度按钮
        setProgressButtonEnabled(true);
        // 关闭震动
        setVibrate(false);

        //setFadeAnimation();
        setZoomAnimation();
        //setFlowAnimation();
        //setSlideOverAnimation();
        //setDepthAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // 点击跳过关闭
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // 完成关闭
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
