package com.ray.project.ui.basis.image;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ray.project.R;
import com.ray.project.base.BaseFragment;
import com.ray.project.base.BasePresenter;
import com.ray.project.databinding.FragmentImageViewBinding;

public class ImageViewFragment extends BaseFragment<FragmentImageViewBinding, BasePresenter> {

    String url;

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_image_view;
    }

    @Override
    protected void initView(View view) {

    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        // 判断数据是否需要添加网络前缀
//        String baseUrl = "";
//        if (url.startsWith(Constants.FILTER_EVENT) ||url.startsWith(Constants.FILTER_TASK)) {
//            url = baseUrl + url;
//        }
        RequestOptions options = new RequestOptions();
        options.centerCrop()
                .error(R.drawable.icon_loadfail);
        Glide.with(mActivity).load(url).apply(options).into(mBinding.imageView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
