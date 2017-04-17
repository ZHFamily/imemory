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
import club.imemory.app.entity.Message;
import club.imemory.app.util.AppManager;
import club.imemory.app.util.AppUtils;

/**
 * @Author: 张杭
 * @Date: 2017/4/1 13:06
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context mContext;

    private List<Message> mMessageList;
    public MessageAdapter(List<Message> messageList) {
        mMessageList = messageList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;
        ImageView mAvatarView;
        TextView mTitleTV;
        TextView mSubheadTV;
        TextView mCreateTimeTV;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = (RelativeLayout) itemView;
            mAvatarView = (ImageView) itemView.findViewById(R.id.image_avatar);
            mTitleTV = (TextView) itemView.findViewById(R.id.tv_title);
            mSubheadTV = (TextView) itemView.findViewById(R.id.tv_subhead);
            mCreateTimeTV = (TextView) itemView.findViewById(R.id.tv_create_time);
        }
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_message, parent, false);

        final ViewHolder holder = new MessageAdapter.ViewHolder(view);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Message Message = mMessageList.get(position);
                AppManager.showToast("什么都没有");
                //MessageActivity.actionStart(mContext,Message.getAvatar(),Message.getTitle());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder holder, int position) {
        Message Message = mMessageList.get(position);
        holder.mTitleTV.setText(Message.getTitle());
        holder.mSubheadTV.setText(Message.getSubhead());
        holder.mCreateTimeTV.setText(AppUtils.getDataToString(Message.getCreatetime()));
        //使用Glide库加载图片
        //Glide.with(mContext).load(Message.getAvatar()).into(holder.mAvatarView);
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}
