package club.imemory.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import club.imemory.app.R;
import club.imemory.app.adapter.LifeAdapter;
import club.imemory.app.entity.Life;

/**
 * @Author: 张杭
 * @Date: 2017/3/29 12:06
 */

public class MyLifeFragment extends Fragment {

    /**
     * 实例化MyLifeFragment
     * @return
     */
    public static MyLifeFragment instanceFragment(){
        return new MyLifeFragment();
    }


    private List<Life> mLifeList = new ArrayList<>();
    private LifeAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();
        View view = inflater.inflate(R.layout.fragment_life, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(container.getContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new LifeAdapter(mLifeList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void initData(){
        mLifeList.clear();
        for (int i=1; i<10; i++){
            Life life = new Life();
            life.setTitle("无bug行自在");
            life.setAvatar("http://imemory.club/imemory/image/"+i+".jpg");
            life.setCreatetime(new Date());
            mLifeList.add(life);
        }
    }



    //悬浮按钮
    /*FloatingActionButton fabCreateLife = (FloatingActionButton) findViewById(R.id.fab);
    fabCreateLife.hide();
    fabCreateLife.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            *//*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*//*
        }
    });*/
}
