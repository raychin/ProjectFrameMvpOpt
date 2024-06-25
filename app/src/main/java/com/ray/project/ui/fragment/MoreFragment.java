package com.ray.project.ui.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.ray.project.R;
import com.ray.project.base.BaseFragment;
import com.ray.project.base.BasePresenter;
import com.ray.project.databinding.FragmentMoreBinding;
import com.ray.project.ui.login.LoginActivity;

/**
 * 更多功能界面fragment
 * @author ray
 * @date 2018/07/03
 */
public class MoreFragment extends BaseFragment<FragmentMoreBinding, BasePresenter> {

    @Override
    protected boolean isImmersiveStatusHeight() {
        return true;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_more;
    }

    @Override
    protected void initView(View view) {
        mBinding.myTextView.setOnClickListener(v -> {
            mActivity.nextActivity(LoginActivity.class);
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
