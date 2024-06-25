package com.ray.project.ui.register;

import com.ray.project.R;
import com.ray.project.base.BaseActivity;
import com.ray.project.base.BasePresenter;
import com.ray.project.databinding.ActivityRegisterBinding;

/**
 * @Description: 用户注册
 * @Author: ray
 * @Date: 24/6/2024
 */
public class RegisterActivity extends BaseActivity<ActivityRegisterBinding, BasePresenter> {
    @Override
    public int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected boolean isImmersiveStatus() {
        return true;
    }

    @Override
    public void initView() {
        mBinding.fab.setOnClickListener(v -> finish());
    }

    @Override
    public void initData() {

    }
}
