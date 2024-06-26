package com.ray.project.ui;

import android.view.LayoutInflater;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.ray.project.R;
import com.ray.project.base.BaseActivity;
import com.ray.project.base.BasePresenter;
import com.ray.project.commons.Typefaces;
import com.ray.project.databinding.ActivityStartBinding;
import com.ray.project.widget.titanic.Titanic;
import com.ray.project.widget.titanic.TitanicTextView;

/**
 * 应用加载屏
 * @author ray
 * @date 2018/07/03
 */
public class AppStart extends BaseActivity<ActivityStartBinding, BasePresenter> {

    @Override
    public int initLayout() {
        return R.layout.activity_start;
    }

    @Override
    public void initView() {
        TitanicTextView tv = findViewById(R.id.my_text_view);
        // set fancy typeface
        tv.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));
        // activity_start animation
        Titanic titanic = new Titanic();
        titanic.start(tv);
    }

    @Override
    public void initData() {
        // 渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.4f, 1.0f);
        aa.setDuration(2000);
        (getWindow().getDecorView()).findViewById(R.id.start_layout).startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                nextActivity(MainActivity.class);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

        });
    }
    @Override
    protected boolean isImmersiveStatus() {
        return true;
    }

}
