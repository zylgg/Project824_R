package com.example.mr_zyl.project824.pro.essence;

import android.support.v7.widget.RecyclerView;

public class RvScrollListener extends RecyclerView.OnScrollListener {

    private static RvScrollListener rvScrollListener;
    private OnVisibilityTitleListener onVisibilityTitleListener;

    public RvScrollListener(OnVisibilityTitleListener listener) {
        this.onVisibilityTitleListener = listener;
    }

    public static RvScrollListener getInstance(OnVisibilityTitleListener listener) {
        if (rvScrollListener == null) {
            rvScrollListener = new RvScrollListener(listener);
        }
        return rvScrollListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy > 40) {//40，单次滚动的偏移（可自己定义）
            //向上滑，隐藏
            if (onVisibilityTitleListener != null) {
                onVisibilityTitleListener.hide();
            }

        } else if (dy < -40) {
            //向下滑，打开
            if (onVisibilityTitleListener != null) {
                onVisibilityTitleListener.open();
            }
        }
    }
}
