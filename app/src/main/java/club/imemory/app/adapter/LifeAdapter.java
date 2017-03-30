package club.imemory.app.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import club.imemory.app.R;
import club.imemory.app.bean.Life;
import club.imemory.app.util.TimeUtils;

/**
 * @Author: 张杭
 * @Date: 2017/3/23 20:41
 */

public class LifeAdapter extends RecyclerView.Adapter<LifeAdapter.ViewHolder> {

    private Context mContext;

    private List<Life> mLifeList;
    public LifeAdapter(List<Life> lifeList) {
        mLifeList = lifeList;
    }

    /**
     * 内部静态类，初始化item
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView mAvatarView;
        TextView mTitleTV;
        //private TextView mSubheadTV;
        TextView mCreateTimeTV;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            mAvatarView = (ImageView) itemView.findViewById(R.id.image_avatar);
            mTitleTV = (TextView) itemView.findViewById(R.id.tv_title);
            //mSubheadTV = (TextView) itemView.findViewById(R.id.tv_subhead);
            mCreateTimeTV = (TextView) itemView.findViewById(R.id.tv_create_time);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_life, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Life life = mLifeList.get(position);
        holder.mTitleTV.setText(life.getTitle());
        //holder.mSubheadTV.setText(life.getSubhead());
        holder.mCreateTimeTV.setText(TimeUtils.getDataToString(life.getCreatetime()));
        //使用Glide库加载图片
        Glide.with(mContext).load(life.getAvatar()).into(holder.mAvatarView);
    }

    @Override
    public int getItemCount() {
        return mLifeList.size();
    }
}
