package com.ray.project.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.ray.project.databinding.RecyclerFooterViewBinding;

import java.util.List;

/**
 * @Description: RecyclerView数据适配器
 * @Author: ray
 * @Date: 28/6/2024
 */
public abstract class BaseAdapter<T, VB extends ViewBinding> extends RecyclerView.Adapter<BindingViewHolder<VB>> {

    protected List<T> mData;

    protected BaseActivity mActivity;
    protected ViewBinding mBinding;
    public BaseAdapter(BaseActivity activity, List<T> data) {
        this.mData = data;
        this.mActivity = activity;
    }

    @Override
    public int getItemCount() {
        if (null == mData) {
            return 1;
        }
        return mData.size() + 1;
    }

    private final static int TYPE_FOOTER = 1000000;
    private final static int TYPE_NORMAL = 1000001;
    @NonNull
    @Override
//    public BindingViewHolder<VB> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_FOOTER == viewType) {
            return new BindingViewHolder<>(onCreateFooterViewBinding(LayoutInflater.from(parent.getContext()), parent));
        }
        return new BindingViewHolder<>(onCreateViewBinding(LayoutInflater.from(parent.getContext()), parent));
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() - 1 == position) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    protected RecyclerFooterViewBinding onCreateFooterViewBinding(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return RecyclerFooterViewBinding.inflate(inflater, parent, false);
    }

    protected abstract VB onCreateViewBinding(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder<VB> holder, int position) {
        if (getItemCount() - 1 == position) {
            // 设置底部显示文本内容
            RecyclerFooterViewBinding footerBinding = (RecyclerFooterViewBinding) holder.getBinding();
            switch (loadState) {
                case LOADING:
                    footerBinding.footerTv.setText("正在加载...");
                    footerBinding.footerPb.setVisibility(View.VISIBLE);
                    break;
                case LOADING_COMPLETE:
                    footerBinding.footerTv.setText("加载更多");
                    footerBinding.footerPb.setVisibility(View.GONE);
                    break;
                case LOADING_END:
                    footerBinding.footerTv.setText("没有更多了~~");
                    footerBinding.footerPb.setVisibility(View.GONE);
                    break;
            }
            return;
        }
        onConvert(holder.getBinding(), position);
    }

    protected abstract void onConvert(VB binding, int position);


    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    // 正在加载
    public final int LOADING = 1;
    // 加载完成
    public final int LOADING_COMPLETE = 2;
    // 加载到底
    public final int LOADING_END = 3;
    /**
     * 设置上拉加载状态
     *
     * @param loadState 0.正在加载 1.加载完成 2.加载到底
     */
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyItemChanged(getItemCount() - 1);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 如果当前是footer的位置，那么该item占据2个单元格，正常情况下占据1个单元格
                    return getItemViewType(position) == TYPE_FOOTER ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }
}
