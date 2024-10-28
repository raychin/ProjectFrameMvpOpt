package com.ray.project.ui.fragment;

import static android.app.Activity.RESULT_OK;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ray.project.R;
import com.ray.project.base.BaseActivity;
import com.ray.project.base.BaseAdapter;
import com.ray.project.base.BaseFragment;
import com.ray.project.base.BasePresenter;
import com.ray.project.commons.Loading;
import com.ray.project.commons.Logger;
import com.ray.project.commons.ToastUtils;
import com.ray.project.databinding.FragmentBluetoothBinding;
import com.ray.project.databinding.SampleAdapterHomeBinding;
import com.ray.project.ui.FragmentContainerActivity;
import com.ray.project.ui.WebViewActivity;
import com.tencent.upgrade.bean.UpgradeStrategy;
import com.tencent.upgrade.core.UpgradeManager;
import com.tencent.upgrade.core.UpgradeReqCallbackForUserManualCheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 蓝牙模块功能fragment
 * @author ray
 * @date 2024/10/24
 */
public class BluetoothFragment extends BaseFragment<FragmentBluetoothBinding, BasePresenter> {
    private final List<HashMap<String, Object>> originData = new ArrayList<HashMap<String, Object>>() {
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", "Ray浏览器");
            map.put("type", "intent");
            map.put("activity", WebViewActivity.class);
            add(map);
        }
    };

    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 3177;

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
        return R.layout.fragment_bluetooth;
    }

    private void initBluetooth () {
        if (null == mBluetoothAdapter) {
            // Initializes Bluetooth adapter.
            final BluetoothManager bluetoothManager =
                    (BluetoothManager) mActivity.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }

        scanLeDevice(true);
    }

    private boolean mScanning = false;
    private final static int SCAN_PERIOD = 2000;
    private void scanLeDevice(final boolean enable) {
        if (null == mBluetoothAdapter) {
            return;
        }
        if (enable) {
            mData.clear();
            mBinding.swipeRefreshLayout.setRefreshing(true);
            if (null == mHandler) {
                mHandler = new Handler();
            }
            // Stops scanning after a pre-defined scan period.
            // 预先定义停止蓝牙扫描的时间（因为蓝牙扫描需要消耗较多的电量）
            mHandler.postDelayed(mRunnable, SCAN_PERIOD);
            mScanning = true;

            // 定义一个回调接口供扫描结束处理
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBinding.swipeRefreshLayout.setRefreshing(false);
            // 传入的回调必须是开启蓝牙扫描时传入的回调，否则蓝牙扫描不会停止
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    /**
     * 指定只扫描含有特定 UUID Service 的蓝牙设备
     * boolean startLeScan(UUID[] serviceUuids, BluetoothAdapter.LeScanCallback callback)
     * 扫描全部蓝牙设备
     * boolean startLeScan(BluetoothAdapter.LeScanCallback callback)
     */
    final BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            Logger.d(TAG, "run: scanning..." + bluetoothDevice.getName());
            // TODO RAY 加入数据
            if (null != bluetoothDevice.getAddress()) {
                if (!mData.contains(bluetoothDevice)) {
                    mData.add(bluetoothDevice);
                }
            }
        }
    };

    @Override
    protected void initView(View view) {
        setTitleText("蓝牙功能");

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        deviceAdapter = new DeviceAdapter(mActivity, mData);
        mBinding.recyclerView.setAdapter(deviceAdapter);
        // 设置下拉刷新的监听器
        mBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据的逻辑
                scanLeDevice(true);
            }
        });

        initBluetooth();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (RESULT_OK == resultCode) {
            if (REQUEST_ENABLE_BT == requestCode) {
                initBluetooth();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private final List<BluetoothDevice> mData = new ArrayList<>();
    private DeviceAdapter deviceAdapter;

    @Override
    protected void initData(Bundle savedInstanceState) {
    }

    private Handler mHandler;
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            scanLeDevice(false);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        scanLeDevice(false);
        if (null != mHandler) {
            mHandler.removeCallbacks(mRunnable);
            mHandler = null;
        }
    }

    private static class DeviceAdapter extends BaseAdapter<BluetoothDevice, SampleAdapterHomeBinding> {

        public DeviceAdapter(BaseActivity activity, List<BluetoothDevice> data) {
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
            BluetoothDevice itemData = mData.get(position);
            binding.btJump.setTransformationMethod(null);
            String name = itemData.getName();
            if (null == name) {
                name = "UNKNOWN DEVICE";
            }
            binding.btJump.setText(name);

            binding.btJump.setOnClickListener(viewTitle -> {
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
