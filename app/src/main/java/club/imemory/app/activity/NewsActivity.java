package club.imemory.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import club.imemory.app.R;
import club.imemory.app.db.News;

/**
 * @Author: 张杭
 * @Date: 2017/3/31 22:17
 */

public class NewsActivity extends BaseActivity {

    /**
     * 启动NewsActivity
     */
    public static void actionStart(Context context, News news) {
        Intent intent = new Intent(context, NewsActivity.class);
        intent.putExtra("news", news);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        News news = (News) getIntent().getSerializableExtra("news");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(news.getTitle());
        toolbar.setTitleTextColor(0xffffffff);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl(news.getUrl());
    }
}
