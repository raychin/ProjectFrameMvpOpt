package com.ray.project.ui.sample.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ray.project.R;
import com.ray.project.base.BaseActivity;
import com.ray.project.base.BaseAdapter;
import com.ray.project.base.BaseFragment;
import com.ray.project.base.BasePresenter;
import com.ray.project.commons.Logger;
import com.ray.project.databinding.FragmentRefreshRecyclerViewBinding;
import com.ray.project.databinding.SampleAdapterRecyclerViewItemBinding;
import com.ray.project.widget.recyclerView.RayRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * RecyclerView下拉刷新&上拉加载fragment
 * @author ray
 * @date 2024/08/02
 */
public class RefreshRecyclerViewFragment extends BaseFragment<FragmentRefreshRecyclerViewBinding, BasePresenter> {

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
        return R.layout.fragment_refresh_recycler_view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void initView(View view) {
        setTitleText("RecyclerView下拉刷新&下拉加载");

        mData = new ArrayList<>();
        mAdapter = new SampleAdapter(mActivity, mData);
        mBinding.recyclerView.setAdapter(mAdapter);

        // 设置下拉刷新的监听器
        mBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据的逻辑
                refreshData();
            }
        });

        mBinding.recyclerView.addOnScrollListener(new RayRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                Logger.e("onLoadMore", "onLoadMore");
                if (mData.size() >= 100) {
                    // 刷新数据时，如果数据大于100条，就不再刷新了
                    return;
                }
                mAdapter.setLoadState(mAdapter.LOADING);
                loadMoreData();
            }
        });

        mBinding.radioGroupRecyclerView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Logger.e("onCheckedChanged", radioGroup.getId() + ", " + i);
                if (i == mBinding.rbLinearLayoutManager.getId()) {
                    setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
                } else if (i == mBinding.rbGridLayoutManager.getId()) {
                    setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
                } else if (i == mBinding.rbStaggeredGridLayoutManager.getId()) {
                    setRecyclerViewLayoutManager(LayoutManagerType.STAGGERED_GRID_LAYOUT_MANAGER);
                }
            }
        });
    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER,
        STAGGERED_GRID_LAYOUT_MANAGER,
    }
    private final static int SPAN_COUNT = 2;

    private void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(mActivity, SPAN_COUNT);
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(mActivity);
                ((LinearLayoutManager) mLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
                break;
            case STAGGERED_GRID_LAYOUT_MANAGER:
                mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
                break;
        }

        mBinding.recyclerView.setLayoutManager(mLayoutManager);
        mAdapter.onAttachedToRecyclerView(mBinding.recyclerView);

//        if (null == mData || mData.size() == 0) {
            mBinding.swipeRefreshLayout.setRefreshing(true);
            refreshData();
//        }
    }

    private List<String> mData;
    private SampleAdapter mAdapter;

    /**
     * 下拉刷新
     */
    private void refreshData() {
        if (mData == null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    mData.add("第" + (i + 1) + "个元素");
                }
                // 注意事项：当完成数据更新后一定要调用 setRefreshing(false)，不然刷新图标会一直转圈，不会消失
                mBinding.swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }
    private Handler mHandler = new Handler();

    /**
     * 上拉加载更多
     */
    private void loadMoreData() {
        int pos = mData.size();
        Logger.e("loadMoreData", "loadMoreData");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    mData.add("第" + (i + pos + 1) + "个元素");
                }
                mAdapter.notifyDataSetChanged();

                if (mData.size() >= 100) {
                    mAdapter.setLoadState(mAdapter.LOADING_END);
                } else {
                    mAdapter.setLoadState(mAdapter.LOADING_COMPLETE);
                }
            }
        }, 3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private static class SampleAdapter extends BaseAdapter<String, SampleAdapterRecyclerViewItemBinding> {

        public SampleAdapter(BaseActivity activity, List<String> data) {
            super(activity, data);
        }

        @Override
        protected SampleAdapterRecyclerViewItemBinding onCreateViewBinding(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
            mBinding = SampleAdapterRecyclerViewItemBinding.inflate(inflater, parent, false);
            return (SampleAdapterRecyclerViewItemBinding) mBinding;
        }

        @Override
        protected void onConvert(SampleAdapterRecyclerViewItemBinding binding, int position) {
//            String itemData = mData.get(position);
            binding.con.removeAllViews();
            TextView textView = new TextView(mActivity);
            textView.setBackgroundResource(R.drawable.bt_shape);
            textView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_black));
            textView.setText("第" + (position + 1) + "个元素");
            textView.setAllCaps(false);
            textView.setPadding(20, 60, 20, 60);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (manager instanceof StaggeredGridLayoutManager) {
                // 初始化随机数生成器
                Random rand = new Random();

                // 定义5个数字
                int[] numbers = {360, 490, 1000, 1200, 1350};

                // 从数组中随机选择一个数字
                int randomIndex = rand.nextInt(numbers.length);
                params.height = numbers[randomIndex];
            }
            binding.con.addView(textView, params);

        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.rbLinearLayoutManager.setChecked(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

}
