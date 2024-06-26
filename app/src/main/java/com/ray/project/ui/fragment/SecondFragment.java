package com.ray.project.ui.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.ray.project.R;
import com.ray.project.base.BaseFragment;
import com.ray.project.base.BasePresenter;
import com.ray.project.databinding.FragmentSecondBinding;
import com.ray.project.ui.sample.RFixDevActivity;

/**
 * 第二个界面fragment
 * @author ray
 * @date 2018/07/03
 */
public class SecondFragment extends BaseFragment<FragmentSecondBinding, BasePresenter> {

    @Override
    protected boolean isImmersiveStatusHeight() {
        return true;
    }

    @Override
    public int statusColor() {
        return getResources().getColor(R.color.green);
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_second;
    }

    @Override
    protected void initView(View view) {
        mBinding.myTextView.setOnClickListener(v -> mActivity.nextActivity(RFixDevActivity.class));
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
