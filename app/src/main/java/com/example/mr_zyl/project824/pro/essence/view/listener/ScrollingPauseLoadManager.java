package com.example.mr_zyl.project824.pro.essence.view.listener;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;
import com.example.mr_zyl.project824.utils.Settings;

import me.xiaopan.sketch.Sketch;

/**
 * 滚动中暂停暂停加载新图片管理器支持RecyclerView和AbsListView
 */
public class ScrollingPauseLoadManager extends RecyclerView.OnScrollListener implements AbsListView.OnScrollListener {
    private Sketch sketch;
    private AbsListView.OnScrollListener absListScrollListener;
    private RecyclerView.OnScrollListener recyclerScrollListener;

    public ScrollingPauseLoadManager(Context context) {
        this.sketch = Sketch.with(context);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (recyclerScrollListener != null) {
            recyclerScrollListener.onScrolled(recyclerView, dx, dy);
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (Settings.getBoolean(recyclerView.getContext(), Settings.PREFERENCE_SCROLLING_PAUSE_LOAD) && recyclerView.getAdapter() != null) {
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                sketch.getConfiguration().setGlobalPauseLoad(true);
            } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (sketch.getConfiguration().isGlobalPauseLoad()) {
                    sketch.getConfiguration().setGlobalPauseLoad(false);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        }

        if (recyclerScrollListener != null) {
            recyclerScrollListener.onScrollStateChanged(recyclerView, newState);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (Settings.getBoolean(view.getContext(), Settings.PREFERENCE_SCROLLING_PAUSE_LOAD) && view.getAdapter() != null) {
            ListAdapter listAdapter = view.getAdapter();
            if (listAdapter instanceof WrapperListAdapter) {
                listAdapter = ((WrapperListAdapter) listAdapter).getWrappedAdapter();
            }
            if (listAdapter instanceof BaseAdapter) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    if (!sketch.getConfiguration().isGlobalPauseLoad()) {
                        sketch.getConfiguration().setGlobalPauseLoad(true);
                    }
                } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (sketch.getConfiguration().isGlobalPauseLoad()) {
                        sketch.getConfiguration().setGlobalPauseLoad(false);
                        ((BaseAdapter) listAdapter).notifyDataSetChanged();
                    }
                }
            }
        }

        if (absListScrollListener != null) {
            absListScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (absListScrollListener != null) {
            absListScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public void setOnScrollListener(AbsListView.OnScrollListener absListViewScrollListener) {
        this.absListScrollListener = absListViewScrollListener;
    }

    public void setOnScrollListener(RecyclerView.OnScrollListener recyclerScrollListener) {
        this.recyclerScrollListener = recyclerScrollListener;
    }
}
