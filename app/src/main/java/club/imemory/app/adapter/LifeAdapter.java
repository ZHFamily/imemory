package club.imemory.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import club.imemory.app.R;
import club.imemory.app.activity.LifeActivity;
import club.imemory.app.db.Life;
import club.imemory.app.util.AppUtils;

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

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout Layout;
        ImageView mAvatarView;
        TextView mTitleTV;
        TextView mLocationTV;
        TextView mCreateTimeTV;

        public ViewHolder(View itemView) {
            super(itemView);
            Layout = (LinearLayout) itemView;
            mAvatarView = (ImageView) itemView.findViewById(R.id.image_avatar);
            mTitleTV = (TextView) itemView.findViewById(R.id.tv_title);
            mLocationTV = (TextView) itemView.findViewById(R.id.tv_location);
            mCreateTimeTV = (TextView) itemView.findViewById(R.id.tv_create_time);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_life, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        holder.Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Life life = mLifeList.get(position);
                LifeActivity.actionStart(mContext, life.getAvatar(), life.getTitle());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Life life = mLifeList.get(position);
        holder.mTitleTV.setText(life.getTitle());
        holder.mLocationTV.setText(life.getLocation());
        holder.mCreateTimeTV.setText("创建于" + AppUtils.getDataToString(life.getCreatetime()));
        //使用Glide库加载图片
        Glide.with(mContext).load(life.getAvatar())
                .thumbnail(0.1f)
                .into(holder.mAvatarView);
    }

    @Override
    public int getItemCount() {
        return mLifeList.size();
    }
}
