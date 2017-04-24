package club.imemory.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import club.imemory.app.R;
import club.imemory.app.db.Message;
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
        View itemView;
        LinearLayout leftMsgLayout;
        ImageView leftAvatarImage;
        TextView leftText;
        LinearLayout rightMsgLayout;
        ImageView rightAvatarImage;
        TextView rightText;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView = itemView;
            leftMsgLayout = (LinearLayout) itemView.findViewById(R.id.layout_msg_left);
            leftAvatarImage = (ImageView) itemView.findViewById(R.id.image_left);
            leftText = (TextView) itemView.findViewById(R.id.tv_msg_left);
            rightMsgLayout = (LinearLayout) itemView.findViewById(R.id.layout_msg_right);
            rightAvatarImage = (ImageView) itemView.findViewById(R.id.image_right);
            rightText = (TextView) itemView.findViewById(R.id.tv_msg_right);
            view = itemView.findViewById(R.id.view);
        }
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_message, parent, false);
        ViewHolder holder = new MessageAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder holder, int position) {
        Message msg = mMessageList.get(position);
        if (msg.getCode()==100000){
            holder.leftMsgLayout.setVisibility(View.VISIBLE);
            holder.rightMsgLayout.setVisibility(View.GONE);
            holder.leftText.setText(msg.getText());
        }else if (msg.getCode()==718){
            holder.rightMsgLayout.setVisibility(View.VISIBLE);
            holder.leftMsgLayout.setVisibility(View.GONE);
            holder.rightText.setText(msg.getText());
            //Glide.with(mContext).load(msg.getAvatar()).thumbnail(0.1f).into(holder.rightAvatarImage);
        }
        /*if (mMessageList.size()-1==position){
            holder.view.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}
