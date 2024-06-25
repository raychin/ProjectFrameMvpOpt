package com.ray.project.ui.login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.transition.Explode;
import android.widget.Toast;


import com.ray.project.R;
import com.ray.project.base.BaseActivity;
import com.ray.project.base.ResultEvent;
import com.ray.project.commons.Loading;
import com.ray.project.commons.ToastUtils;
import com.ray.project.config.MMKVManager;
import com.ray.project.databinding.ActivityLoginBinding;
import com.ray.project.model.LoginModel;
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
            String user = mBinding.etUsername.getText().toString().trim();
            String password = mBinding.etPassword.getText().toString().trim();
            if (user.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            Loading.show(this, "登录中...");
            //        presenter.doLogin("nsapp", "geostar999");
            presenter.doLogin("admin", "!Sh291623");
        });

    }

    @Override
    public void updateView(ResultEvent event) {
        super.updateView(event);

        if(event.getCode() == 0) {
            MMKVManager.getInstance().encode("token", ((LoginModel) event.getObj()).accessToken);
            MMKVManager.getInstance().encode("user", event.getObj());
            ToastUtils.showToast(this, getString(R.string.tip_login_success), Toast.LENGTH_SHORT);
            Explode explode = new Explode();
            explode.setDuration(500);

            getWindow().setExitTransition(explode);
            getWindow().setEnterTransition(explode);
//            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
//            Intent i2 = new Intent(this, MainActivity.class);
//            startActivity(i2, oc2.toBundle());
            finish();
        }
    }

    @Override
    public void initData() {

    }
}
