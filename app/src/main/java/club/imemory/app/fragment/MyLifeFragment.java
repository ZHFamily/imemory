package club.imemory.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import club.imemory.app.R;
import club.imemory.app.activity.CreateLifeActivity;
import club.imemory.app.adapter.LifeAdapter;
import club.imemory.app.db.Life;
import club.imemory.app.ui.SideslipRecyclerView;
import club.imemory.app.util.AppManager;

/**
 * @Author: 张杭
 * @Date: 2017/3/29 12:06
 */

public class MyLifeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static MyLifeFragment mMyLifeFragment;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayoutManager mLayoutManager;
    private List<Life> mLifeList = new ArrayList<>();
    private LifeAdapter adapter;
    private ImageView mImageAdd;
    private int lastVisibleItem;

    /**
     * 实例化MyLifeFragment
     *
     * @return
     */
    public static MyLifeFragment instanceFragment() {
        if (mMyLifeFragment == null) {
            mMyLifeFragment = new MyLifeFragment();
        }
        return mMyLifeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_life, container, false);
        mImageAdd = (ImageView) view.findViewById(R.id.image_add);
        mImageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateLifeActivity.actionStart(getActivity());
            }
        });
        //悬浮按钮
        FloatingActionButton fabCreateLife = (FloatingActionButton) view.findViewById(R.id.fab_create_life);
        fabCreateLife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateLifeActivity.actionStart(getActivity());
            }
        });
        //下拉刷新
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(this);
        //滚动内容
        SideslipRecyclerView recyclerView = (SideslipRecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        initData();
        if (mLifeList.size() > 0) {
            mImageAdd.setVisibility(View.GONE);
        }
        adapter = new LifeAdapter(mLifeList);
        recyclerView.setAdapter(adapter);
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        return view;
    }

    //刷新时初始化数据
    private void initData() {
        mLifeList.clear();
        List<Life> list = DataSupport.order("createtime desc").find(Life.class);
        for (int i = 0; i < list.size(); i++) {
            mLifeList.add(list.get(i));
        }
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                initData();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mLifeList.size() > 0) {
                            mImageAdd.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                        AppManager.showToast("刷新成功");
                    }
                });
            }
        }).start();
    }

    //上拉加载风更多数据
    private void Load() {
        List<Life> list = DataSupport.order("createtime desc").limit(5).offset(5).find(Life.class);
        for (int i = 0; i < list.size(); i++) {
            mLifeList.add(list.get(i));
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLifeList.size() > 0) {
                    adapter.notifyDataSetChanged();
                    AppManager.showToast("加载成功");
                } else {
                    AppManager.showToast("什么都没有了");
                }
            }
        });
    }
}
