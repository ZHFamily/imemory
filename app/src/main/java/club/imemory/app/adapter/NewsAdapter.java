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
import java.util.Random;

import club.imemory.app.R;
import club.imemory.app.activity.NewsActivity;
import club.imemory.app.db.News;

/**
 * @Author: 张杭
 * @Date: 2017/3/31 21:51
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context mContext;

    private List<News> mList;

    public NewsAdapter(List<News> list) {
        mList = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;
        TextView mTitleTV;
        TextView mSubheadTV;
        ImageView mThumbnailImg;
        ImageView mAuthorImg;
        TextView mAuthorTV;
        TextView mCreateTimeTV;
        TextView mHitsTV;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = (RelativeLayout) itemView;
            mTitleTV = (TextView) itemView.findViewById(R.id.tv_title);
            mSubheadTV = (TextView) itemView.findViewById(R.id.tv_subhead);
            mThumbnailImg = (ImageView) itemView.findViewById(R.id.image_thumbnail);
            mAuthorImg = (ImageView) itemView.findViewById(R.id.image_author);
            mAuthorTV = (TextView) itemView.findViewById(R.id.tv_author);
            mCreateTimeTV = (TextView) itemView.findViewById(R.id.tv_create_time);
            mHitsTV = (TextView) itemView.findViewById(R.id.tv_hits);
        }
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_news, parent, false);

        final NewsAdapter.ViewHolder holder = new NewsAdapter.ViewHolder(view);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                News news = mList.get(position);
                NewsActivity.actionStart(mContext, news);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        News news = mList.get(position);
        holder.mTitleTV.setText(news.getTitle());
        //holder.mSubheadTV.setText(news.getSubhead());
        holder.mAuthorTV.setText(news.getAuthor_name());
        holder.mCreateTimeTV.setText(news.getDate());
        holder.mHitsTV.setText(String.valueOf((int)(Math.random()*10000+100)));
        //使用Glide库加载图片
        Glide.with(mContext).load(news.getThumbnail_pic_s()).into(holder.mAuthorImg);
        Glide.with(mContext).load(news.getThumbnail_pic_s()).into(holder.mThumbnailImg);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}