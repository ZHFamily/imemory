package club.imemory.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import club.imemory.app.R;
import club.imemory.app.adapter.FindAdapter;
import club.imemory.app.entity.Find;
import club.imemory.app.entity.Life;

/**
 * @Author: 张杭
 * @Date: 2017/3/29 12:05
 */

public class FindFragment extends Fragment {

    /**
     * 实例化NearShareFragment
     * @return
     */
    public static FindFragment instanceFragment(){
        return new FindFragment();
    }

    private SwipeRefreshLayout swipeRefresh;
    private List<Find> mFindList = new ArrayList<>();
    private FindAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        //下拉刷新
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                refreshFind();
            }
        });
        //滚动内容
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FindAdapter(mFindList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void refreshFind(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initData(){
        mFindList.clear();
        for (int i=1; i<10; i++){
            Find find = new Find("123","123","123","123","123",123456,new Date());
            mFindList.add(find);
        }
    }
}
