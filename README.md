# ProjectFrameMvpOpt



## 1. 说明

基于android平台，采用mvp架构，实现androidx项目基本框架。

集成包括jetpack功能、基于ZXing二维码扫描功能、retrofit2、glide4.5、Luban1.8、mmkv1.3.5、room2.3.0等开源框架，
[主下载地址](https://common.shiply-cdn.qq.com/gray/c6097cfbf6/prod/1719479273/app1.0.4_4_ray_2024-06-27.apk)

<div>
    <img src="https://gitee.com/KingsRay/gitee-image-host/raw/master/image/image-20240625171135839.png" alt="image-20240625171135839" width="15%" height="15%" />
    <img src="https://gitee.com/KingsRay/gitee-image-host/raw/master/image/image-20240627153650231.png" alt="image-20240625171606099" width="15%" height="15%" />
    <img src="https://gitee.com/KingsRay/gitee-image-host/raw/master/image/image-20240625171457865.png" alt="image-20240625171457865" width="15%" height="15%" />
	<img src="https://gitee.com/KingsRay/gitee-image-host/raw/master/image/image-20240625171227927.png" alt="image-20240625171227927" width="15%" height="15%" />
    <img src="https://gitee.com/KingsRay/gitee-image-host/raw/master/image/image-20240625171418584.png" alt="image-20240625171418584" width="15%" height="15%" />
    <img src="https://gitee.com/KingsRay/gitee-image-host/raw/master/image/image-20240625171606099.png" alt="image-20240625171606099" width="15%" height="15%" />
</div>






## 2. 集成功能



### 初始版本

-   网络请求封装

-   持久化MMKV，替换SharePreferences方案
    MMKV 是基于 mmap 内存映射的 key-value 组件，底层序列化/反序列化使用 protobuf 实现，性能高，稳定性强。也是腾讯微信团队使用的技术。MMKV使用了一些技术手段，如mmap文件映射和跨进程通信的共享内存，以实现更高效的数据存取操作。MMKV的性能比SharedPreferences快数十倍，尤其在读写大量数据时效果更加明显。
    
    ```java
    // 详细使用参照com.ray.project.ui.sample.fragment.PersistenceFragment
    
    // 获取所有key集合
    String[] keys = MMKVManager.getInstance().keys();
    
    // 根据key获取存储内容，示例为string字符串
    MMKVManager.getInstance().decodeString("xxx");
    
    // 存储
    MMKVManager.getInstance().encode("xxx", value);
    
    // 删除
    MMKVManager.getInstance().removeKey("xxx");
    ```
    
    
    
-   数据库room操作封装

    ```java
    // 详细使用参照com.ray.project.ui.sample.fragment.PersistenceFragment，已设置不可在主线程使用
    
    // 获取所有数据
    new Thread(() -> {
        List<User> list = AppDatabase.getInstance(mActivity).userDao().getAll();
    }).start();
    
    // 插入
    new Thread(() -> {
    	User user = new User();
        user.firstName = key;
        user.lastName = value;
        long insertResult = AppDatabase.getInstance(mActivity).userDao().insertUser(user);
        mActivity.runOnUiThread(() -> {
            if (insertResult > 0) {
            	ToastUtils.showCustomToast(mActivity, "保存成功", Toast.LENGTH_SHORT);
                user.uid = insertResult;
                roomDbData.set(position, user);
                roomDbAdapter.notifyItemChanged(position);
            } else {
                ToastUtils.showCustomToast(mActivity, "保存失败", Toast.LENGTH_SHORT);
            }
        });
    }).start();
    
    // 删除
    int deleteResult = AppDatabase.getInstance(mActivity).userDao().delete(itemData);
    if (deleteResult <= 0) {
        mActivity.runOnUiThread(() -> {
            ToastUtils.showCustomToast(mActivity, "删除失败", Toast.LENGTH_SHORT);
        });
        return;
    }
    ```

    

-   WebView封装，支持原生应用同vue、uni小程序交互；支持图片选取及压缩



### 2024年06月26日集成更新功能

-   集成腾讯bugly错误日志功能框架
    https://bugly.qq.com/
-   集成腾讯热修复shiply框架
    https://shiply.tds.qq.com/



### 2024年06月27日集成定位功能

-   LocationUtils使用

    ```java
    // 详细使用参照com.ray.project.ui.sample.fragment.LocationFragment
    LocationUtils.getInstance(mActivity).setAddressCallback(new LocationUtils.AddressCallback() {
        @Override
        public void onGetAddress(Address address) {
            String countryName = address.getCountryName();//国家
            String adminArea = address.getAdminArea();//省
            String locality = address.getLocality();//市
            String subLocality = address.getSubLocality();//区
            String featureName = address.getFeatureName();//街道
            Logger.d("定位地址", countryName + adminArea + locality + subLocality + featureName);
        }
    
        @Override
        public void onGetLocation(double lat, double lng) {
            Logger.d("定位经纬度", lat + ", " + lng);
        }
    });
    
    
    @Override
    public void onStop() {
        super.onStop();
        // 销毁定位，避免持续定位耗电
        LocationUtils.getInstance(mActivity).clearAddressCallback();
    }
    ```



### 2024年07月24日集成osmdroid开源地图加载功能

实现定位显示，后续添加轨迹绘制、面绘制等功能
<img src="https://gitee.com/KingsRay/gitee-image-host/raw/master/image/ca1a03708239b517849150467a54837a_720.jpg" alt="ca1a03708239b517849150467a54837a_720" style="zoom: 25%;" />



其他功能陆续集成开发中...



### 2024年08月05日SwipeRefreshView和RecyclerView实现上拉加载和下拉刷新

<div>
<div>
    <img src="https://gitee.com/KingsRay/gitee-image-host/raw/master/image/image-20240805171234992.png" alt="image-20240805171234992" style="zoom: 25%;" />
    <img src="https://gitee.com/KingsRay/gitee-image-host/raw/master/image/image-20240805171313760.png" alt="image-20240805171313760" style="zoom:25%;" />
</div>


xml添加布局文件
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/con"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="10dp"
    android:background="@color/app_background">

    <androidx.swiperefreshlayout.widget.SwipeRefreshLayout
        android:id="@+id/swipeRefreshLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

    </androidx.swiperefreshlayout.widget.SwipeRefreshLayout>
</androidx.constraintlayout.widget.ConstraintLayout>
```

采用ViewBinding实现，参见com.ray.project.ui.sample.fragment.RefreshRecyclerViewFragment文件
```java
	protected void initView(View view) {
		LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.recyclerView.setLayoutManager(layoutManager);
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

		mBinding.swipeRefreshLayout.setRefreshing(true);
        refreshData();
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

            TextView textView = new TextView(mActivity);
            textView.setBackgroundResource(R.drawable.bt_shape_blue);
            textView.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
            textView.setText("第" + (position + 1) + "个元素");
            textView.setAllCaps(false);
            textView.setPadding(20, 60, 20, 60);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            binding.con.addView(textView, params);

        }
    }
```



### 2024年10月8日集成基于ZXxing二维码扫描功能

<img src="https://gitee.com/KingsRay/gitee-image-host/raw/master/image/2e9e4631e1464e1f43810d5c3e528216_720.jpg" alt="2e9e4631e1464e1f43810d5c3e528216_720" style="zoom:25%;" />
