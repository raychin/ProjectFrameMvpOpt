package com.ray.project.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ray.project.R;
import com.ray.project.base.BaseActivity;
import com.ray.project.base.BaseAdapter;
import com.ray.project.base.BaseFragment;
import com.ray.project.base.BasePresenter;
import com.ray.project.commons.Loading;
import com.ray.project.commons.Logger;
import com.ray.project.commons.ToastUtils;
import com.ray.project.databinding.FragmentHomeBinding;
import com.ray.project.databinding.SampleAdapterHomeBinding;
import com.ray.project.ui.FragmentContainerActivity;
import com.ray.project.ui.WebViewActivity;
import com.ray.project.ui.sample.RFixDevActivity;
import com.ray.project.widget.PopupMenu;
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
    private final List<HashMap<String, Object>> originData = new ArrayList<HashMap<String, Object>>() {
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", "Ray浏览器");
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

            map = new HashMap<>();
            map.put("name", "地图");
            map.put("type", "intentFragment");
            map.put("activity", FragmentContainerActivity.class);
            map.put("fragment", "com.ray.project.ui.fragment.MapViewFragment");
            add(map);

            map = new HashMap<>();
            map.put("name", "RecyclerView下拉刷新&上拉加载");
            map.put("type", "intentFragment");
            map.put("activity", FragmentContainerActivity.class);
            map.put("fragment", "com.ray.project.ui.sample.fragment.RefreshRecyclerViewFragment");
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

        ImageButton rightBtn = (ImageButton) mActivity.getView(R.id.right);
        if (null != rightBtn) {
            rightBtn.setVisibility(View.VISIBLE);
            rightBtn.setImageResource(R.drawable.icon_tools);
            rightBtn.setOnClickListener(v -> {
                PopupMenu.getInstance(mActivity).showPopupWindow(rightBtn);
            });
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mData = new ArrayList<>();
        homeAdapter = new HomeAdapter(mActivity, mData);
        mBinding.recyclerView.setAdapter(homeAdapter);
        // 设置下拉刷新的监听器
        mBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据的逻辑
                refreshData();
            }
        });

    }

    private List<HashMap<String, Object>> mData;
    private HomeAdapter homeAdapter;

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.swipeRefreshLayout.setRefreshing(true);
        refreshData();
    }

    /**
     * 下拉刷新
     */
    private void refreshData() {
        if (mBinding == null) {
            return;
        }
        if (mData == null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }

        if (null == mHandler) {
            mHandler = new Handler();
        }

        mHandler.postDelayed(mRunnable, 1500);
    }

    private Handler mHandler;
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mData.addAll(originData);
            // 注意事项：当完成数据更新后一定要调用 setRefreshing(false)，不然刷新图标会一直转圈，不会消失
            mBinding.swipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (null != mHandler) {
            mHandler.removeCallbacks(mRunnable);
            mHandler = null;
        }
    }

    private static class HomeAdapter extends BaseAdapter<HashMap<String, Object>, SampleAdapterHomeBinding> {

        public HomeAdapter(BaseActivity activity, List<HashMap<String, Object>> data) {
            super(activity, data);
        }

        @Override
        protected Boolean showFooter() {
            return false;
        }

        @Override
        protected SampleAdapterHomeBinding onCreateViewBinding(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
            mBinding = SampleAdapterHomeBinding.inflate(inflater, parent, false);
            return (SampleAdapterHomeBinding) mBinding;
        }

        @Override
        protected void onConvert(SampleAdapterHomeBinding binding, int position) {
            HashMap<String, Object> itemData = mData.get(position);
            binding.btJump.setTransformationMethod(null);
            binding.btJump.setText(Objects.requireNonNull((String) itemData.get("name")));

            binding.btJump.setOnClickListener(viewTitle -> {
                if (itemData.get("type") != null) {
                    switch (Objects.requireNonNull(itemData.get("type")).toString()) {
                        case "intent":
                            mActivity.nextActivity((Class<?>) itemData.get("activity"));
                            break;
                        case "shiplyUpgrade":
                            Loading.getInstance().show(mActivity, "检查更新中...");
                            UpgradeManager.getInstance().checkUpgrade(true, null, new UpgradeReqCallbackForUserManualCheck() {
                                @Override
                                public void onReceiveStrategy(UpgradeStrategy upgradeStrategy) {
                                    super.onReceiveStrategy(upgradeStrategy);
                                    Logger.e("onReceiveStrategy", upgradeStrategy.toString());
                                    Loading.getInstance().dismiss();
                                }

                                @Override
                                public void onFail(int i, String s) {
                                    super.onFail(i, s);
                                    Logger.e("onReceiveStrategy", i + " " + s);
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
                                    Logger.e("onReceiveStrategy", upgradeStrategy.toString());
                                    Loading.getInstance().dismiss();
                                }
                            });
                            break;
                        case "intentFragment":
                            Logger.e("intentFragment", (String) itemData.get("fragment"));
                            mActivity.nextActivity((Class<?>) itemData.get("activity"), FragmentContainerActivity.FRAGMENT_PATH, (String) itemData.get("fragment"));
                            break;
                        default:
                            break;
                    }
                } else {
                    ToastUtils.showCustomToast(mActivity, "暂未实现", Toast.LENGTH_SHORT);
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
