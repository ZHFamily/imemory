package club.imemory.app.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import club.imemory.app.R;

/**
 * 显示记录的详细信息
 *
 * @Author: 张杭
 * @Date: 2017/3/31 15:34
 */

public class LifeActivity extends BaseActivity {

    /**
     * 启动LifeActivity
     */
    public static void actionStart(Context context, String... strings) {
        Intent intent = new Intent(context, LifeActivity.class);
        intent.putExtra("avatar", strings[0]);
        intent.putExtra("title", strings[1]);
        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setEnterTransition(new Explode().setDuration(500));
        getWindow().setExitTransition(new Explode().setDuration(500));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life);
        Intent intent = getIntent();
        String avatar = intent.getStringExtra("avatar");
        String title = intent.getStringExtra("title");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView lifeAvatarImg = (ImageView) findViewById(R.id.image_avatar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        collapsingToolbar.setTitle(title);
        Glide.with(this).load(avatar).into(lifeAvatarImg);
    }

}
