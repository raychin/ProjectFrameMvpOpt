package com.ray.project.ui.sample.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ray.project.R;
import com.ray.project.base.BaseFragment;
import com.ray.project.base.BasePresenter;
import com.ray.project.commons.Loading;
import com.ray.project.commons.LocationUtils;
import com.ray.project.commons.Logger;
import com.ray.project.databinding.FragmentSampleBinding;

/**
 * 定位功能fragment
 * @author ray
 * @date 2024/06/27
 */
public class LocationFragment extends BaseFragment<FragmentSampleBinding, BasePresenter> {

    @Override
    protected boolean isImmersiveStatusHeight() {
        return true;
    }

    @Override
    public int statusColor() {
        return getResources().getColor(R.color.top_bar_background);
    }

    @Override
    protected boolean showTitleNavigation() {
        return true;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity.checkPermissions(
            new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            }
        );
    }

    @Override
    protected void initView(View view) {
        setTitleText("定位");

        TextView textView = new TextView(mActivity);
        textView.setBackgroundResource(R.drawable.bt_shape);
        textView.setId(View.generateViewId());
        ConstraintLayout.LayoutParams textParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 460);
        textParams.setMargins(20, 20, 20, 20);
        textView.setPadding(10, 10, 10, 10);
        textView.setText("定位文本");
        mBinding.con.addView(textView, textParams);

        Button button = new Button(mActivity);
        button.setBackgroundResource(R.drawable.bt_shape_blue);
        button.setId(View.generateViewId());
        button.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
        button.setText("定位");
        button.setAllCaps(false);
        ConstraintLayout.LayoutParams buttonParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(20, 20, 20, 20);
        buttonParams.topToBottom = textView.getId();
        button.setOnClickListener(viewTitle -> {
            // 添加权限判断
            if (ActivityCompat.checkSelfPermission(mActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(mActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                mActivity.checkPermissions(
                        new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        }
                );
                return;
            }

//            Loading.getInstance().show(mActivity, "定位中...");
            StringBuffer sb = new StringBuffer();
            sb.append("定位");
            textView.setText(sb.toString());
            // 注意6.0及以上版本需要在申请完权限后调用方法
            LocationUtils.getInstance(mActivity).setAddressCallback(new LocationUtils.AddressCallback() {
                @Override
                public void onGetAddress(Address address) {
                    Loading.getInstance().dismiss();
                    // 国家
                    String countryName = address.getCountryName();
                    // 省
                    String adminArea = address.getAdminArea();
                    // 市
                    String locality = address.getLocality();
                    // 区
                    String subLocality = address.getSubLocality();
                    // 街道，可能为空
                    String featureName = address.getFeatureName();
                    Logger.d("定位地址", countryName + adminArea + locality + subLocality + featureName);
                    sb.append("\n").append("定位地址: ").append(countryName).append(adminArea).append(locality).append(subLocality);
                    textView.setText(sb.toString());
                }

                @Override
                public void onGetLocation(double lat, double lng) {
                    Loading.getInstance().dismiss();
                    Logger.d("定位经纬度", lat + ", " + lng);
                    sb.append("\n").append("定位经纬度: ").append(lat).append(", ").append(lng);
                    textView.setText(sb.toString());
                }
            });

        });
        mBinding.con.addView(button, buttonParams);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocationUtils.getInstance(mActivity).clearAddressCallback();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
