package com.ray.project.ui.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.ray.project.R;
import com.ray.project.base.BaseFragment;
import com.ray.project.base.BasePresenter;
import com.ray.project.commons.Loading;
import com.ray.project.commons.Logger;
import com.ray.project.commons.ToastUtils;
import com.ray.project.databinding.FragmentHomeBinding;
import com.ray.project.ui.FragmentContainerActivity;
import com.ray.project.ui.WebViewActivity;
import com.ray.project.ui.sample.RFixDevActivity;
import com.tencent.upgrade.bean.UpgradeStrategy;
import com.tencent.upgrade.core.UpgradeManager;
import com.tencent.upgrade.core.UpgradeReqCallbackForUserManualCheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 首页界面fragment
 * @author ray
 * @date 2018/07/03
 */
public class HomeFragment extends BaseFragment<FragmentHomeBinding, BasePresenter> {
    private final List<HashMap<String, Object>> mData = new ArrayList<HashMap<String, Object>>() {
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", "WebView");
            map.put("type", "intent");
            map.put("activity", WebViewActivity.class);
            add(map);

            map = new HashMap<>();
            map.put("name", "Shiply检查更新");
            map.put("type", "shiplyUpgrade");
            add(map);

            map = new HashMap<>();
            map.put("name", "Shiply热更新");
            map.put("type", "intent");
            map.put("activity", RFixDevActivity.class);
            add(map);

            map = new HashMap<>();
            map.put("name", "定位");
            map.put("type", "intentFragment");
            map.put("activity", FragmentContainerActivity.class);
            map.put("fragment", "com.ray.project.ui.sample.fragment.LocationFragment");
            add(map);

            map = new HashMap<>();
            map.put("name", "本地持久化");
            map.put("type", "intentFragment");
            map.put("activity", FragmentContainerActivity.class);
            map.put("fragment", "com.ray.project.ui.sample.fragment.PersistenceFragment");
            add(map);
        }
    };

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
    protected void initView(View view) {
        setTitleText(getString(R.string.app_name));
        for (int i = 0; i < mData.size(); i ++) {
            HashMap<String, Object> map = mData.get(i);
            Button button = new Button(mActivity);
            button.setBackgroundResource(R.drawable.bt_shape_blue);
            button.setId(Integer.parseInt("100") + i);
            button.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
            button.setText((String) map.get("name"));
            button.setAllCaps(false);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 20, 20, 20);
            if (i != 0) {
                params.topToBottom = Integer.parseInt("100") + (i - 1);
            }
            button.setOnClickListener(viewTitle -> {
                if (map.get("type") != null) {
                    switch (Objects.requireNonNull(map.get("type")).toString()) {
                        case "intent":
                            mActivity.nextActivity((Class<?>) map.get("activity"));
                            break;
                        case "shiplyUpgrade":
                            Loading.getInstance().show(mActivity, "检查更新中...");
                            UpgradeManager.getInstance().checkUpgrade(true, null, new UpgradeReqCallbackForUserManualCheck() {
                                @Override
                                public void onReceiveStrategy(UpgradeStrategy upgradeStrategy) {
                                    super.onReceiveStrategy(upgradeStrategy);
                                    Logger.e(TAG, upgradeStrategy.toString());
                                    Loading.getInstance().dismiss();
                                }

                                @Override
                                public void onFail(int i, String s) {
                                    super.onFail(i, s);
                                    Logger.e(TAG, i + " " + s);
                                    Loading.getInstance().dismiss();
                                }

                                @Override
                                public void onReceivedNoStrategy() {
                                    super.onReceivedNoStrategy();
                                    Loading.getInstance().dismiss();
                                    ToastUtils.showCustomToast(mActivity, "已经是最新版本了", Toast.LENGTH_SHORT);
                                }

                                @Override
                                public void tryPopUpgradeDialog(UpgradeStrategy upgradeStrategy) {
                                    super.tryPopUpgradeDialog(upgradeStrategy);
                                    Logger.e(TAG, upgradeStrategy.toString());
                                    Loading.getInstance().dismiss();
                                }
                            });
                            break;
                        case "intentFragment":
                            Logger.e(TAG, (String) map.get("fragment"));
                            mActivity.nextActivity((Class<?>) map.get("activity"), FragmentContainerActivity.FRAGMENT_PATH, (String) map.get("fragment"));
                            break;
                        default:
                            break;
                    }
                } else {
                    ToastUtils.showCustomToast(mActivity, "暂未实现", Toast.LENGTH_SHORT);
                }
            });
            mBinding.con.addView(button, params);
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
