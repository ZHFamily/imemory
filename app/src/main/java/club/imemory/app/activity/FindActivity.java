package club.imemory.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import club.imemory.app.R;
import club.imemory.app.entity.Find;

/**
 * @Author: 张杭
 * @Date: 2017/3/31 22:17
 */

public class FindActivity extends BaseActivity {

    /**
     * 启动FindActivity
     */
    public static void actionStart(Context context, Find find) {
        Intent intent = new Intent(context, FindActivity.class);
        intent.putExtra("title", find.getTitle());
        intent.putExtra("avatar", find.getAvatar());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
