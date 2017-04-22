package club.imemory.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.List;

import club.imemory.app.R;
import club.imemory.app.entity.Meizi;
import club.imemory.app.util.AppManager;

/**
 * @Author: 张杭
 * @Date: 2017/3/31 21:51
 */

public class RelaxationAdapter extends RecyclerView.Adapter<RelaxationAdapter.ViewHolder> {

    private Context mContext;
    private List<Meizi> lists;//数据

    //适配器初始化
    public RelaxationAdapter(List<Meizi> lists) {
        this.lists = lists;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
            imageView = (ImageView) itemView.findViewById(R.id.image_photo);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_relaxation, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Meizi meizi = lists.get(position);
                AppManager.showToast("感谢 "+meizi.getWho()+" 提供");
            }
        });
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Meizi meizi = lists.get(position);
        int height = (int) (250 + Math.random() * 1000);
        Glide.with(mContext).load(meizi.getUrl())
                .thumbnail(0.1f)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}