package club.imemory.app.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import club.imemory.app.R;
import club.imemory.app.db.User;
import club.imemory.app.fragment.RelaxationFragment;
import club.imemory.app.util.AppManager;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * @Author: 张杭
 * @Date: 2017/3/9 23:07
 */

public class UserActivity extends BaseActivity {

    /**
     * 启动UserActivity
     */
    public static void actionStart(Context context, User user ,Pair<View, String>... sharedElements) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra("user", user);
        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context,sharedElements).toBundle());
    }

    private User user;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbar;
    private ButtonBarLayout playButton;
    private CollapsingToolbarLayoutState state;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView BgImage = (ImageView) findViewById(R.id.image_bg);
        ImageView headImage = (ImageView) findViewById(R.id.image_head);
        setSupportActionBar(toolbar);
        playButton = (ButtonBarLayout) toolbar.findViewById(R.id.btn_play);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        collapsingToolbar.setTitle(user.getName());
        Glide.with(this)
                .load(R.drawable.bg)
                .bitmapTransform(new BlurTransformation(this, 5))
                .into(BgImage);
        if (user.getHead() != null) {
            Glide.with(this).load(user.getHead()).error(R.drawable.ic_user_head).into(headImage);
            Glide.with(this)
                    .load(user.getHead())
                    .bitmapTransform(new BlurTransformation(this, 5))
                    .into(BgImage);
        }
        myAppBar();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        RelaxationFragment mRelaxationFragment = RelaxationFragment.instanceFragment();
        fragmentTransaction.add(R.id.content_frame, mRelaxationFragment, "RelaxationFragment");
        fragmentTransaction.commit();

        FloatingActionButton fabCreateLife = (FloatingActionButton) findViewById(R.id.fab);
        fabCreateLife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.showToast("用户信息暂时无法修改");
            }
        });
    }

    private void myAppBar() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                AppManager.logI("user",verticalOffset+"");
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        collapsingToolbar.setTitle(user.getName());
                    }
                } else if (Math.abs(verticalOffset)+100 >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        collapsingToolbar.setTitle("");//设置title不显示
                        playButton.setVisibility(View.VISIBLE);//隐藏播放按钮
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (state == CollapsingToolbarLayoutState.COLLAPSED) {
                            playButton.setVisibility(View.GONE);//由折叠变为中间状态时隐藏播放按钮
                        }
                        collapsingToolbar.setTitle(user.getName());
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });
    }
}
