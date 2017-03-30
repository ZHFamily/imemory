package club.imemory.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import club.imemory.app.R;
import club.imemory.app.adapter.LifeAdapter;
import club.imemory.app.bean.Life;

/**
 * @Author: 张杭
 * @Date: 2017/3/29 12:04
 */

public class MessageFragment extends Fragment {

    /**
     * 实例化MessageFragment
     * @return
     */
    public static MessageFragment instanceFragment(){
        return new MessageFragment();
    }

    private List<Life> mLifeList = new ArrayList<>();
    private LifeAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initData();
        View view = inflater.inflate(R.layout.fragment_life, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new LifeAdapter(mLifeList);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
