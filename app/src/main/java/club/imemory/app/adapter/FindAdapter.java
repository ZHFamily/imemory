package club.imemory.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import club.imemory.app.R;
import club.imemory.app.activity.FindActivity;
import club.imemory.app.db.Find;
import club.imemory.app.util.AppUtils;

/**
 * @Author: 张杭
 * @Date: 2017/3/31 21:51
 */

public class FindAdapter extends RecyclerView.Adapter<FindAdapter.ViewHolder> {

    private Context mContext;

    private List<Find> mFindList;

    public FindAdapter(List<Find> findList) {
        mFindList = findList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;
        TextView mTitleTV;
        TextView mSubheadTV;
        ImageView mAvatarImg;
        ImageView mUserHeadImg;
        TextView mUserNameTV;
        TextView mCreateTimeTV;
        TextView mHitsTV;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = (RelativeLayout) itemView;
            mTitleTV = (TextView) itemView.findViewById(R.id.tv_title);
            mSubheadTV = (TextView) itemView.findViewById(R.id.tv_subhead);
            mAvatarImg = (ImageView) itemView.findViewById(R.id.image_avatar);
            mUserHeadImg = (ImageView) itemView.findViewById(R.id.image_user_head);
            mUserNameTV = (TextView) itemView.findViewById(R.id.tv_user_name);
            mCreateTimeTV = (TextView) itemView.findViewById(R.id.tv_create_time);
            mHitsTV = (TextView) itemView.findViewById(R.id.tv_hits);
        }
    }

    @Override
    public FindAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_find, parent, false);

        final FindAdapter.ViewHolder holder = new FindAdapter.ViewHolder(view);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Find find = mFindList.get(position);
                FindActivity.actionStart(mContext, find);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(FindAdapter.ViewHolder holder, int position) {
        Find find = mFindList.get(position);
        holder.mTitleTV.setText(find.getTitle());
        holder.mSubheadTV.setText(find.getSubhead());
        holder.mUserNameTV.setText(find.getUserName());
        holder.mCreateTimeTV.setText(AppUtils.getDataToString(find.getCreatetime()));
        holder.mHitsTV.setText(String.valueOf(find.getHits()));
        //使用Glide库加载图片
        Glide.with(mContext).load(R.drawable.bg_imemory).into(holder.mAvatarImg);
        Glide.with(mContext).load(R.drawable.bg_imemory).into(holder.mUserHeadImg);
    }

    @Override
    public int getItemCount() {
        return mFindList.size();
    }
}