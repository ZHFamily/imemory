package club.imemory.app.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Author: 张杭
 * @Date: 2017/4/21 21:51
 */

public class PhotoRecyclerView extends RecyclerView {

    public PhotoRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private View mView;

    /**
     * 滚动时回调的接口
     */
    private OnItemScrollChangeListener mItemScrollChangeListener;

    public void setOnItemScrollChangeListener(
            OnItemScrollChangeListener mItemScrollChangeListener) {
        this.mItemScrollChangeListener = mItemScrollChangeListener;
    }

    public interface OnItemScrollChangeListener {
        void onChange(View view, int position);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mView = getChildAt(0);
        if (mItemScrollChangeListener != null) {
            mItemScrollChangeListener.onChange(mView, getChildAdapterPosition(mView));
        }
    }

    @Override
    public void onScrollStateChanged(int arg0) {
    }

    /**
     * 滚动时，判断当前第一个View是否发生变化，发生才回调
     */
    @Override
    public void onScrolled(int arg0, int arg1) {
        View newView = getChildAt(0);
        if (mItemScrollChangeListener != null) {
            if (newView != null && newView != mView) {
                mView = newView;
                mItemScrollChangeListener.onChange(mView, getChildAdapterPosition(mView));
            }
        }
    }
}
