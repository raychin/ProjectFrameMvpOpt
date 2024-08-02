package com.ray.project.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
            // TODO RAY 可以设置底部显示文本内容
            RecyclerFooterViewBinding footerBinding = (RecyclerFooterViewBinding) holder.getBinding();
//            footerBinding.footerTv.setText("加载更多...");
            return;
        }
        onConvert(holder.getBinding(), position);
    }

    protected abstract void onConvert(VB binding, int position);

}
