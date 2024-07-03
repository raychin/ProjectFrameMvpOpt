package com.ray.project.ui;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;

import com.ray.project.R;
import com.ray.project.base.BaseActivity;
import com.ray.project.base.BaseFragment;
import com.ray.project.databinding.ActivityMainBinding;
import com.ray.project.ui.fragment.HomeFragment;
import com.ray.project.ui.fragment.MoreFragment;
import com.ray.project.ui.login.LoginPresenter;

import butterknife.OnClick;

/**
 * 应用主界面
 * @author ray
 * @date 2018/07/03
 */
public class MainActivity extends BaseActivity<ActivityMainBinding, LoginPresenter> {

    @Override
    protected boolean isImmersiveStatus() {
        return true;
    }

    @Override
    public int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        checkPermissions(
            new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
            }
        );
    }

    @Override
    public void initData() {
        setSelect(0);
    }

    @Override
    protected boolean isMvp() {
        return true;
    }

    @OnClick({R.id.tabFirstButton, R.id.tabSecondButton, R.id.tabThirdButton, R.id.tabForthButton})
    public void onClick(View view) {
        int pos = -1;
        if (view.getId() == R.id.tabFirstButton) {
            pos = 0;
        } else if (view.getId() == R.id.tabSecondButton) {
            pos = 1;
        } else if (view.getId() == R.id.tabThirdButton) {
            pos = 2;
        } else if (view.getId() == R.id.tabForthButton) {
            pos = 3;
        }

        if (pos != -1) {
            setSelect(pos);
        }
    }

    @Override
    protected boolean exitApp () { return true; }

    @SuppressWarnings("rawtypes")
    private BaseFragment mTab01, mTab02, mTab03, mTab04;
    private void setSelect(int i) {
        if (null == mFragmentManager) {
            mFragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 0:
                if (mTab01 == null) {
                    mTab01 = BaseFragment.newInstance(HomeFragment.class);
                    transaction.add(R.id.fragment_container, mTab01);
                } else {
                    transaction.show(mTab01);
                }
                break;
            case 1:
                if (mTab02 == null) {
                    mTab02 = BaseFragment.newInstance("com.ray.project.ui.fragment.SecondFragment");
                    transaction.add(R.id.fragment_container, mTab02);
                } else {
                    transaction.show(mTab02);
                }
                break;
            case 2:
                if (mTab03 == null) {
                    Bundle args = new Bundle();
//                    args.putString(BaseFragment.WEB_VIEW_URL_KEY, "http://www.baidu.com/");
//                    args.putString(BaseFragment.WEB_VIEW_URL_KEY, "https://www.myd03.com");
                    mTab03 = BaseFragment.newInstance("com.ray.project.ui.fragment.WebViewFragment");
                    mTab03.setArguments(args);
                    transaction.add(R.id.fragment_container, mTab03);
                } else {
                    transaction.show(mTab03);
                }
                break;
            case 3:
                if (mTab04 == null) {
                    Bundle args = new Bundle();
                    args.putString(BaseFragment.WEB_VIEW_URL_KEY, "http://www.baidu.com/");
                    mTab04 = BaseFragment.newInstance(MoreFragment.class, args);
                    transaction.add(R.id.fragment_container, mTab04);
                } else {
                    transaction.show(mTab04);
                }
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mTab01 != null) {
            transaction.hide(mTab01);
        }
        if (mTab02 != null) {
            transaction.hide(mTab02);
        }
        if (mTab03 != null) {
            transaction.hide(mTab03);
        }
        if (mTab04 != null) {
            transaction.hide(mTab04);
        }
    }
}
