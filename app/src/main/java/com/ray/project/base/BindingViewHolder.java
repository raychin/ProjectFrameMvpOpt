package com.ray.project.base;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

/**
 * @Description: BindingViewHolder
 * @Author: ray
 * @Date: 28/6/2024
 */
public class BindingViewHolder<VB extends ViewBinding> extends RecyclerView.ViewHolder {

    private VB mBinding;
    public BindingViewHolder(@NonNull VB binding) {
        super(binding.getRoot());
        mBinding = binding;
    }
    public VB getBinding() {
        return mBinding;
    }
}
