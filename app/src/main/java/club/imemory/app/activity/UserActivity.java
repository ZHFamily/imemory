package club.imemory.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;

import club.imemory.app.R;
import club.imemory.app.db.User;

/**
 * @Author: 张杭
 * @Date: 2017/3/9 23:07
 */

public class UserActivity extends BaseActivity {

    /**
     * 启动UserActivity
     */
    public static void actionStart(Context context, User user) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra("user",user);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra ("user");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("个人主页");
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView AvatarImg = (ImageView) findViewById(R.id.image_avatar);
        ImageView headImg = (ImageView) findViewById(R.id.image_head);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        collapsingToolbar.setTitle(user.getName());
        Glide.with(this).load(user.getHead()).into(headImg);
    }


}
