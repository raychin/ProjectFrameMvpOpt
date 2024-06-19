package com.ray.project.ui.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.ray.project.R;
import com.ray.project.base.BaseFragment;
import com.ray.project.ui.WebViewActivity;
import com.ray.project.widget.titanic.TitanicTextView;

/**
 * 首页界面fragment
 * @author ray
 * @date 2018/07/03
 */
public class HomeFragment extends BaseFragment {

    private TitanicTextView mTvTitle;

    @Override
    protected boolean isImmersiveStatusHeight() {
        return true;
    }

    @Override
    public int statusColor() {
        return getResources().getColor(R.color.top_bar_background);
    }

    @Override
    protected boolean showTitleNavigation() {
        return true;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        setTitleText("首页");
        mTvTitle = view.findViewById(R.id.my_text_view);
        mTvTitle.setOnClickListener(viewTitle -> mActivity.nextActivity(WebViewActivity.class));
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
