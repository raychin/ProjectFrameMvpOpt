package com.ray.project.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.FragmentTransaction;

import com.ray.project.R;
import com.ray.project.base.BaseActivity;
import com.ray.project.base.BaseFragment;
import com.ray.project.base.BasePresenter;
import com.ray.project.base.ResultEvent;
import com.ray.project.databinding.ActivityWebViewBinding;
import com.ray.project.ui.fragment.WebViewFragment;

/**
 * WebView主界面
 * @author ray
 * @date 2018/07/03
 */
public class WebViewActivity extends BaseActivity<ActivityWebViewBinding, BasePresenter> {
    private String webUrl = "";
//    private String webUrl = "file:///android_asset/web/ray-template/index.html";
//    private String webUrl = "https://www.czzy.top";
//    private String webUrl = "https://cz01.vip";

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
        Intent intent = getIntent();
        if (null != intent && !TextUtils.isEmpty(intent.getStringExtra(BaseFragment.WEB_VIEW_URL_KEY))) {
            webUrl = intent.getStringExtra(BaseFragment.WEB_VIEW_URL_KEY);
        }
        setSelect();
    }

    @Override
    public void updateView(ResultEvent event) {
        super.updateView(event);
    }

    @SuppressWarnings("rawtypes")
    private BaseFragment webViewFragment;
    private void setSelect() {
        if (null == mFragmentManager) {
            mFragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragment(transaction);

        if (webViewFragment == null) {
            webViewFragment = BaseFragment.newInstance("com.ray.project.ui.fragment.WebViewFragment");
            Bundle args = new Bundle();
            if (!TextUtils.isEmpty(webUrl)) {
                args.putString(BaseFragment.WEB_VIEW_URL_KEY, webUrl);
            }

            if (webUrl.startsWith("file:///")) {
                // 设置不显示浏览器顶部导航，可以根据实际情况设置
                args.putInt(WebViewFragment.WEB_VIEW_SHOW_NAVI_KEY, WebViewFragment.WEB_VIEW_SHOW_NAVI_GONE);
            } else {
                args.putInt(WebViewFragment.WEB_VIEW_SHOW_NAVI_KEY, WebViewFragment.WEB_VIEW_SHOW_NAVI_SHOW);
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
