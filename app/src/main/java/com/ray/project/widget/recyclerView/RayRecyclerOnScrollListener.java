package com.ray.project.widget.recyclerView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.ray.project.commons.Logger;

/**
 * @Description: RecyclerView scroll监听类
 * @Author: ray
 * @Date: 2/8/2024
 */
public abstract class RayRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    //用来标记是否正在向上滑动
    private boolean isSlidingUpward = false;

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        Logger.e("onScrollStateChanged", "newState = " + newState);

        RecyclerView.LayoutManager rManager = recyclerView.getLayoutManager();
        if (rManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) rManager;
            // 当不滑动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                int lastItemPosition = -1;
                int[] lastVisibleItems = staggeredGridLayoutManager.findLastVisibleItemPositions(new int[staggeredGridLayoutManager.getSpanCount()]);
                // 获取最后一个完全显示的itemPosition
                if (staggeredGridLayoutManager.getSpanCount() == 1) {
                    lastItemPosition = lastVisibleItems[0];
                } else if (staggeredGridLayoutManager.getSpanCount() == 2) {
                    lastItemPosition = Math.max(lastVisibleItems[0], lastVisibleItems[1]);
                } else if (staggeredGridLayoutManager.getSpanCount() == 3) {
                    lastItemPosition = Math.max(Math.max(lastVisibleItems[0], lastVisibleItems[1]), lastVisibleItems[2]);
                }

                int itemCount = staggeredGridLayoutManager.getItemCount();

                // 判断是否滑动到了最后一个item，并且是向上滑动
                if (lastItemPosition == (itemCount - 1) && isSlidingUpward) {
                    // 加载更多
                    onLoadMore();
                }
            }
            return;
        }
        LinearLayoutManager manager = (LinearLayoutManager) rManager;
        // 当不滑动时
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            // 获取最后一个完全显示的itemPosition
            assert manager != null;
            int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
            int itemCount = manager.getItemCount();

            // 判断是否滑动到了最后一个item，并且是向上滑动
            if (lastItemPosition == (itemCount - 1) && isSlidingUpward) {
                // 加载更多
                onLoadMore();
            }
        }
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        Logger.e("onScrolled", "dx = " + dx);
        Logger.e("onScrolled", "dy = " + dy);
        // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
        isSlidingUpward = dy > 0;
    }

    /**
     * 加载更多回调
     */
    public abstract void onLoadMore();
}
