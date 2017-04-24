package club.imemory.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import club.imemory.app.R;
import club.imemory.app.adapter.NewsAdapter;
import club.imemory.app.db.News;
import club.imemory.app.http.HttpManager;
import club.imemory.app.json.JsonAnalyze;
import club.imemory.app.util.AppManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @Author: 张杭
 * @Date: 2017/4/24 11:27
 */

public class PlaceholderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_PAGE = "section_page";
    private static final String AUTHENTICATOR = "APPCODE f16b64f7ddf94e8596728d6e93392733";
    private static final String ALY_URl = "http://toutiao-ali.juheapi.com/toutiao/index?type=";
    private List<News> mNewsListOne = new ArrayList<>();
    private List<News> mNewsListTwo = new ArrayList<>();
    private List<News> mNewsListThree = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapterOne;
    private NewsAdapter mAdapterTwo;
    private NewsAdapter mAdapterThree;
    private int mPage;

    public PlaceholderFragment() {
    }

    public static PlaceholderFragment newInstance(int sectionPage) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, sectionPage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        //下拉刷新
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mSwipeRefresh.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        initAdapter();
        return view;
    }

    private void initAdapter() {
        switch (mPage) {
            case 1:
                AppManager.logI("news", "top");
                mAdapterOne = new NewsAdapter(mNewsListOne);
                mRecyclerView.setAdapter(mAdapterOne);
                initData("top");
                break;
            case 2:
                AppManager.logI("news", "guoji");
                mAdapterTwo = new NewsAdapter(mNewsListTwo);
                mRecyclerView.setAdapter(mAdapterTwo);
                initData("guoji");
                break;
            case 3:
                AppManager.logI("news", "keji");
                mAdapterThree = new NewsAdapter(mNewsListThree);
                mRecyclerView.setAdapter(mAdapterThree);
                initData("keji");
                break;
        }
    }

    private void initData(final String type) {
        mSwipeRefresh.setRefreshing(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<News> list = DataSupport.where("category=?", type).limit(20).find(News.class);
                if (list != null && list.size() > 0) {
                    updateNews(list);
                } else {
                    requestNews(type);
                }
            }
        }).start();

    }

    @Override
    public void onRefresh() {
        AppManager.logI("news", mPage + "");
        switch (mPage) {
            case 1:
                requestNews("top");
                break;
            case 2:
                requestNews("guoji");
                break;
            case 3:
                requestNews("keji");
                break;
        }
    }

    private void requestNews(String type) {
        HttpManager.sendOKHttpRequest(ALY_URl + type, AUTHENTICATOR, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                if (response.code() == 200) {
                    updateNews(JsonAnalyze.handleNewsResponse(responseText));
                } else {
                    AppManager.logI("News", responseText);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AppManager.showToast("获取失败，请检查网络");
                        mSwipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void updateNews(final List<News> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (list != null && list.size() > 0) {
                    String type = list.get(1).getCategory();
                    AppManager.logI("news", type);
                    if (type.equals("头条")) {
                        mNewsListOne.clear();
                        mNewsListOne.addAll(list);
                        mAdapterOne.notifyDataSetChanged();
                    }
                    if (type.equals("国际")) {
                        mNewsListTwo.clear();
                        mNewsListTwo.addAll(list);
                        mAdapterTwo.notifyDataSetChanged();
                    }
                    if (type.equals("科技")) {
                        mNewsListThree.clear();
                        mNewsListThree.addAll(list);
                        mAdapterThree.notifyDataSetChanged();
                    }
                    AppManager.showToast("数据获取成功");
                } else {
                    AppManager.showToast("数据解析失败");
                }
                mSwipeRefresh.setRefreshing(false);
            }
        });

    }
}
