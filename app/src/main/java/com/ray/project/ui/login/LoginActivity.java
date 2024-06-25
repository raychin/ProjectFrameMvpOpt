package com.ray.project.ui.login;

import com.ray.project.R;
import com.ray.project.base.BaseActivity;
import com.ray.project.databinding.ActivityLoginBinding;
import com.ray.project.ui.register.RegisterActivity;

/**
 * @Description: 用户登录
 * @Author: ray
 * @Date: 24/6/2024
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginPresenter> {
    @Override
    public int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected boolean isImmersiveStatus() {
        return true;
    }

    @Override
    protected boolean isMvp() {
        return true;
    }

    @Override
    public void initView() {
        mBinding.fab.setOnClickListener(v -> nextActivity(RegisterActivity.class));
    }

    @Override
    public void initData() {

    }
}
