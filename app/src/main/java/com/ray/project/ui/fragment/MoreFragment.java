package com.ray.project.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.ray.project.R;
import com.ray.project.base.BaseFragment;
import com.ray.project.base.BasePresenter;
import com.ray.project.commons.ToastUtils;
import com.ray.project.config.MMKVManager;
import com.ray.project.databinding.FragmentMoreBinding;
import com.ray.project.model.LoginModel;
import com.ray.project.ui.MainActivity;
import com.ray.project.ui.login.LoginActivity;
import com.ray.project.widget.CommonDialog;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 更多功能界面fragment
 * @author ray
 * @date 2018/07/03
 */
public class MoreFragment extends BaseFragment<FragmentMoreBinding, BasePresenter> {

//    @Override
//    protected boolean isImmersiveStatusHeight() {
//        return true;
//    }


    @Override
    protected int initLayout() {
        return R.layout.fragment_more;
    }

    @Override
    protected void initView(View view) {
        mBinding.btToLogin.setOnClickListener(v -> {
            mActivity.nextActivity(LoginActivity.class);
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        // 应用圆角转换，10是圆角的半径 .apply(options)
        RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(10));
        Glide.with(this).load(R.mipmap.ic_launcher).circleCrop().into(mBinding.ivHead);

        // 使用Glide加载图片并应用创建的工厂实例
        Glide.with(this)
                .asBitmap()
                .load(R.drawable.bg_gradient)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25)))
                .into(mBinding.ivBgHead);
    }

    @Override
    public void onResume() {
        super.onResume();
        ToastUtils.showToast(mActivity, MMKVManager.getInstance().decodeString("token"), Toast.LENGTH_SHORT);
        new CommonDialog(
                mActivity,
                "您的秘钥是" + MMKVManager.getInstance().decodeString("token"),
                new CommonDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if(confirm){
                            dialog.dismiss();
                        }
                    }
                }).setTitle("提示").show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
