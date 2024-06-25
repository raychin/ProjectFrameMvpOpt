package com.ray.project.ui.register;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import com.ray.project.R;
import com.ray.project.base.BaseActivity;
import com.ray.project.base.BasePresenter;
import com.ray.project.databinding.ActivityRegisterBinding;

import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;

/**
 * @Description: 用户注册
 * @Author: ray
 * @Date: 24/6/2024
 */
public class RegisterActivity extends BaseActivity<ActivityRegisterBinding, BasePresenter> {
    @Override
    public int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected boolean isImmersiveStatus() {
        return true;
    }

    @Override
    public void initView() {
        ShowEnterAnimation();
        mBinding.fab.setOnClickListener(v -> {
            animateRevealClose();
        });
    }

    @Override
    public void initData() {

    }

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fab_transition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                mBinding.cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(mBinding.cvAdd, mBinding.cvAdd.getWidth()/2,0, mBinding.fab.getWidth() / 2, mBinding.cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mBinding.cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(mBinding.cvAdd,mBinding.cvAdd.getWidth()/2,0, mBinding.cvAdd.getHeight(), mBinding.fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mBinding.cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                mBinding.fab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }
    @Override
    public void onBackPressed() {
        animateRevealClose();
    }
}
