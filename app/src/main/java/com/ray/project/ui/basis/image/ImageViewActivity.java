package com.ray.project.ui.basis.image;

import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.ray.project.R;
import com.ray.project.base.BaseActivity;
import com.ray.project.base.BasePresenter;
import com.ray.project.commons.ToastUtils;
import com.ray.project.databinding.ActivityImageBinding;
import com.ray.project.widget.CustomViewPager;

import java.util.List;

import butterknife.BindView;

/**
 * 图片预览界面
 * 使用方法:
     Intent intent = new Intent(this, ImageViewActivity.class);
     intent.putStringArrayListExtra("url", selected);    // ArrayList集合
     intent.putExtra("index", index + 1);    // 第几张开始
     startActivity(intent);
 */
public class ImageViewActivity extends BaseActivity<ActivityImageBinding, BasePresenter> {

    List<String> urlList;
    @BindView(R.id.viewpager)
    CustomViewPager viewpager;
    @BindView(R.id.title)
    TextView titleTv;
    int index = 1;
    private Fragment[] fragments;

    private FragmentTransaction ft = null;


    @Override
    protected boolean isImmersiveStatus() {
        return true;
    }

    @Override
    protected boolean isImmersiveStatusHeight() {
        return true;
    }

    @Override
    protected ActivityImageBinding inflateViewBinding(LayoutInflater layoutInflater) {
        return ActivityImageBinding.inflate(layoutInflater);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_image;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        urlList = intent.getStringArrayListExtra("url");
        index = intent.getIntExtra("index",1);
        if (null == urlList || urlList.size() == 0) {
            ToastUtils.showToast(ImageViewActivity.this, "数据有误", 1);
            finish();
        }

        setTitle("图片查看（" + index + "/" + urlList.size() + "）");
        init();
    }

    protected void init() {
        fragments = new Fragment[urlList.size()];
        for (int i = 0; i < urlList.size(); i ++){
            fragments[i] = new ImageViewFragment();
            ((ImageViewFragment)fragments[i]).setUrl(urlList.get(i));
        }
        viewpager.setPagingEnabled(true);
        viewpager.setAdapter(adapter);
        // 指定预先加载的页数
        viewpager.setOffscreenPageLimit(1);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position + 1;
                titleTv.setText("图片查看（" + index + "/" + urlList.size() + "）");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setCurrentItem(index-1);
    }

    private FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    };
}
