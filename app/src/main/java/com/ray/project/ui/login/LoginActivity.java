package com.ray.project.ui.login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.transition.Explode;

import androidx.core.app.ActivityOptionsCompat;

import com.ray.project.R;
import com.ray.project.base.BaseActivity;
import com.ray.project.databinding.ActivityLoginBinding;
import com.ray.project.ui.MainActivity;
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
        mBinding.fab.setOnClickListener(v -> {
            getWindow().setExitTransition(null);
            getWindow().setEnterTransition(null);

            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(this, mBinding.fab, mBinding.fab.getTransitionName());
            startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
        });

        mBinding.btGo.setOnClickListener(v -> {
            Explode explode = new Explode();
            explode.setDuration(500);

            getWindow().setExitTransition(explode);
            getWindow().setEnterTransition(explode);
//            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
//            Intent i2 = new Intent(this, MainActivity.class);
//            startActivity(i2, oc2.toBundle());
            finish();
        });

    }

    @Override
    public void initData() {

    }
}
