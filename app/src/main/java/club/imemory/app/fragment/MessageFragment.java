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
import club.imemory.app.adapter.MessageAdapter;
import club.imemory.app.entity.Message;

/**
 * @Author: 张杭
 * @Date: 2017/3/29 12:04
 */

public class MessageFragment extends Fragment {

    private static MessageFragment mMessageFragment = null;
    private SwipeRefreshLayout swipeRefresh;
    private List<Message> mMessageList = new ArrayList<>();
    private MessageAdapter adapter;

    /**
     * 实例化MessageFragment
     * @return
     */
    public static MessageFragment instanceFragment(){
        if (mMessageFragment==null){
            mMessageFragment = new MessageFragment();
        }
        return mMessageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        //下拉刷新
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                refreshMessage();
            }
        });
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MessageAdapter(mMessageList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void initData(){
        for (int i=0;i<20;i++){
            Message message = new Message();
            message.setTitle("");
            message.setSubhead("");
            message.setAvatar("");
            message.setCreatetime(new Date());
            mMessageList.add(message);
        }
    }

    private void refreshMessage(){
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
}
