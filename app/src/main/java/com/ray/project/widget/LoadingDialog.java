package com.ray.project.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ray.project.R;
import com.ray.project.databinding.DialogLoadingBinding;

import java.util.Objects;

/**
 * @Description: 加载框
 * @Author: ray
 * @Date: 25/6/2024
 */
public class LoadingDialog extends Dialog {
    private DialogLoadingBinding viewBinding;
    private Context mContext;
    private String message = "加载中...";

    public LoadingDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    protected LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewBinding = DialogLoadingBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        // 设置背景透明
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(mContext.getDrawable(R.color.transparent));

        // 设置全屏
        getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
        );
        getWindow().setGravity(Gravity.CENTER);
        initView();
    }

    public LoadingDialog setMessage(String message) {
        this.message = message;
        if (null != viewBinding) {
            viewBinding.loadingText.setText(message);
        }
        return this;
    }

    private void initView () {
        viewBinding.loadingText.setText(message);
        RotateAnimation rotateAnimation = new RotateAnimation(
                0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnimation.setDuration(1500);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        viewBinding.loadingImg.startAnimation(rotateAnimation);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return false;
    }
}
