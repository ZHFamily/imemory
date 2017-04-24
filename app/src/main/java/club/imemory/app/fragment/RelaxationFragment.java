package club.imemory.app.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import club.imemory.app.R;
import club.imemory.app.adapter.RelaxationAdapter;
import club.imemory.app.entity.Meizi;
import club.imemory.app.http.HttpManager;
import club.imemory.app.json.JsonAnalyze;
import club.imemory.app.util.AppManager;

/**
 * @Author: 张杭
 * @Date: 2017/3/29 12:05
 */

public class RelaxationFragment extends Fragment {

    private static RelaxationFragment mRelaxationFragment = null;
    private GridLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private List<Meizi> mMeiziList = new ArrayList<>();
    private RelaxationAdapter adapter;
    private int page = (int)(Math.random()*25);
    private int lastVisibleItem;
    private GetData getData= null;

    /**
     * 实例化RelaxationFragment
     */
    public static RelaxationFragment instanceFragment() {
        if (mRelaxationFragment == null) {
            mRelaxationFragment = new RelaxationFragment();
        }
        return mRelaxationFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getData = new GetData();
        getData.execute("http://gank.io/api/data/福利/10/" + page);
        View view = inflater.inflate(R.layout.fragment_relaxation, container, false);

        //滚动内容
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new GridLayoutManager(container.getContext(),2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new RelaxationAdapter(mMeiziList);
        mRecyclerView.setAdapter(adapter);
        //recyclerview滚动监听
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //0：当前屏幕停止滚动
                //1：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上
                //2：随用户的操作，屏幕上产生的惯性滑动；
                // 滑动状态停止并且剩余少于两个item时，自动加载下一页
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 2 >= mLayoutManager.getItemCount()) {
                    if (getData!=null){
                        AppManager.showToast("正在加载，不要着急");
                    }else{
                        getData = new GetData();
                        getData.execute("http://gank.io/api/data/福利/10/" + (++page));
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //获取加载的最后一个可见视图在适配器的位置。
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        return view;
    }

    //异步加载数据
    private class GetData extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return HttpManager.sendOKHttpRequest(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            List<Meizi> temp = JsonAnalyze.handleMeiziResponse(result);
            if (temp == null || temp.size() == 0) {
                AppManager.showToast("获取失败，请检查网络");
            } else {
                mMeiziList.addAll(temp);
                if (adapter==null){
                    adapter = new RelaxationAdapter(mMeiziList);
                    mRecyclerView.setAdapter(adapter);
                }else{
                    //让适配器刷新数据
                    adapter.notifyDataSetChanged();
                }
            }
            //停止swipeRefreshLayout加载动画
            getData = null;
        }

        @Override
        protected void onCancelled() {
            getData = null;
        }
    }
}
