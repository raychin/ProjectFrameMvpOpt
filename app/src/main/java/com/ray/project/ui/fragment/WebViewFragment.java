package com.ray.project.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.ray.project.R;
import com.ray.project.base.BaseFragment;

import butterknife.BindView;

/**
 * 更多功能界面fragment
 * @author ray
 * @date 2018/07/03
 */
public class WebViewFragment extends BaseFragment {
    @BindView(R.id.webView)
    private WebView webView;

    @Override
    protected boolean isImmersiveStatusHeight() {
        return true;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_web_view;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }
}
