package club.imemory.app.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import club.imemory.app.R;
import club.imemory.app.adapter.ShowPhotoAdapter;
import club.imemory.app.db.Life;

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
    public static void actionStart(Context context, Life life) {
        Intent intent = new Intent(context, LifeActivity.class);
        intent.putExtra("life", life);
        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
    }

    private List<String> mList = new ArrayList<>();
    private ShowPhotoAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life);
        Intent intent = getIntent();
        Life life = (Life) intent.getSerializableExtra("life");
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
        collapsingToolbar.setTitle(life.getTitle());
        Glide.with(this).load(life.getAvatar()).into(lifeAvatarImg);
        ((TextView)findViewById(R.id.tv_subhead)).setText(life.getSubhead());
        mList.clear();
        String[] path = life.getPhoto().split("#cppy#");
        for (int i=0;i<path.length-1;i++){
            mList.add(path[i]);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ShowPhotoAdapter(mList);
        recyclerView.setAdapter(adapter);
    }
}
