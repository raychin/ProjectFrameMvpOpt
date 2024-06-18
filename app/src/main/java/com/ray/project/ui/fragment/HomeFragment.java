package com.ray.project.ui.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.ray.project.R;
import com.ray.project.base.BaseFragment;

/**
 * 首页界面fragment
 * @author ray
 * @date 2018/07/03
 */
public class HomeFragment extends BaseFragment {

    @Override
    protected boolean isImmersiveStatusHeight() {
        return true;
    }


    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        mActivity.setTitleText("首页");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
