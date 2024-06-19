package com.ray.project.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.ray.project.R;
import com.ray.project.base.BaseActivity;
import com.ray.project.base.BaseFragment;
import com.ray.project.base.ResultEvent;

import butterknife.OnClick;

/**
 * 应用主界面
 * @author ray
 * @date 2018/07/03
 */
public class WebViewActivity extends BaseActivity {
    private String webUrl = "";

    @Override
    protected boolean isImmersiveStatus() {
        return true;
    }

    @Override
    public int initLayout() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initView() {
        checkPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION});
    }

    @Override
    public void initData() {
        setSelect();
    }

    @Override
    public void updateView(ResultEvent event) {
        super.updateView(event);
    }

    @OnClick({})
    public void onClick(View view) {
    }

    @SuppressWarnings("rawtypes")
    private BaseFragment webViewFragment;
    private void setSelect() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);

        if (webViewFragment == null) {
            webViewFragment = BaseFragment.newInstance("com.ray.project.ui.fragment.WebViewFragment");
            Bundle args = new Bundle();
            if (!TextUtils.isEmpty(webUrl)) {
                args.putString(BaseFragment.WEB_VIEW_URL_KEY, webUrl);
            }
            webViewFragment.setArguments(args);
            transaction.add(R.id.fragment_container, webViewFragment);
        } else {
            transaction.show(webViewFragment);
        }
        transaction.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (webViewFragment != null) {
            transaction.hide(webViewFragment);
        }
    }
}
