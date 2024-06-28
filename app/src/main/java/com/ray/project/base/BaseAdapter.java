package com.ray.project.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

/**
 * @Description: RecyclerView数据适配器
 * @Author: ray
 * @Date: 28/6/2024
 */
public abstract class BaseAdapter<VB extends ViewBinding> extends RecyclerView.Adapter<BindingViewHolder<VB>> {

    @NonNull
    @Override
    public BindingViewHolder<VB> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BindingViewHolder<>(onCreateViewBinding(LayoutInflater.from(parent.getContext()), parent));
    }

    protected abstract VB onCreateViewBinding(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder<VB> holder, int position) {
        onConvert(holder.getBinding(), position);
    }

    protected abstract void onConvert(VB binding, int position);

}
