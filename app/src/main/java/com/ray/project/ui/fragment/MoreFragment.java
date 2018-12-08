package com.ray.project.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.ray.project.R;
import com.ray.project.base.BaseFragment;

/**
 * 更多功能界面fragment
 * @author ray
 * @date 2018/07/03
 */
public class MoreFragment extends BaseFragment {

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

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }
}
