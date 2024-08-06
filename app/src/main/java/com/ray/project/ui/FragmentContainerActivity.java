package com.ray.project.ui;

import android.text.TextUtils;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

import com.ray.project.R;
import com.ray.project.base.BaseActivity;
import com.ray.project.base.BaseFragment;
import com.ray.project.base.BasePresenter;
import com.ray.project.base.ResultEvent;
import com.ray.project.commons.ToastUtils;
import com.ray.project.databinding.ActivityFragmentContainerBinding;

/**
 * fragment container activity
 * @author ray
 * @date 2018/07/03
 */
public class FragmentContainerActivity extends BaseActivity<ActivityFragmentContainerBinding, BasePresenter> {

    public static final String FRAGMENT_PATH = "fragmentPath";
    private String fragmentPath = "";

    @Override
    protected boolean isImmersiveStatus() {
        return true;
    }

    @Override
    public int initLayout() {
        return R.layout.activity_fragment_container;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        setSelect();
    }

    @Override
    public void updateView(ResultEvent event) {
        super.updateView(event);
    }


    @SuppressWarnings("rawtypes")
    private BaseFragment viewFragment;
    private void setSelect() {
        fragmentPath = getIntent().getStringExtra(FRAGMENT_PATH);
        if (TextUtils.isEmpty(fragmentPath)) {
            ToastUtils.showCustomToast(this, "请配置Fragment路径", Toast.LENGTH_SHORT);
            finish();
            return;
        }
        if (null == mFragmentManager) {
            mFragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragment(transaction);

        if (viewFragment == null) {
            viewFragment = BaseFragment.newInstance(fragmentPath);
            transaction.add(R.id.fragment_container, viewFragment);
        } else {
            transaction.show(viewFragment);
        }
        transaction.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (viewFragment != null) {
            transaction.hide(viewFragment);
        }
    }
}
