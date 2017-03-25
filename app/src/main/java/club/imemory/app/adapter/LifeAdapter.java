package club.imemory.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import club.imemory.app.R;
import club.imemory.app.bean.Life;

/**
 * @Author: 张杭
 * @Date: 2017/3/23 20:41
 */

public class LifeAdapter extends RecyclerView.Adapter<LifeAdapter.ViewHolder> {

    private List<Life> mLifeList;

    public LifeAdapter(List<Life> lifeList) {
        mLifeList = lifeList;
    }

    /**
     * 内部静态类，初始化item
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mAvatarView;
        private TextView mTitleTV;
        private TextView mSubheadTV;
        private TextView mCreateTimeTV;

        public ViewHolder(View itemView) {
            super(itemView);
            mAvatarView = (ImageView) itemView.findViewById(R.id.img_avatar);
            mTitleTV = (TextView) itemView.findViewById(R.id.tv_title);
            mSubheadTV = (TextView) itemView.findViewById(R.id.tv_subhead);
            mCreateTimeTV = (TextView) itemView.findViewById(R.id.tv_create_time);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_item_life, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Life life = mLifeList.get(position);
        holder.mAvatarView.setImageResource(R.drawable.ic_screenshot06);
        holder.mTitleTV.setText(life.getTitle());
        holder.mSubheadTV.setText(life.getSubhead());
        holder.mCreateTimeTV.setText(life.getCreatetime());
    }

    @Override
    public int getItemCount() {
        return mLifeList.size();
    }
}
