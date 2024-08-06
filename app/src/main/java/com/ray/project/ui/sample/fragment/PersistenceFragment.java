package com.ray.project.ui.sample.fragment;

import android.Manifest;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ray.project.R;
import com.ray.project.base.BaseActivity;
import com.ray.project.base.BaseAdapter;
import com.ray.project.base.BaseFragment;
import com.ray.project.base.BasePresenter;
import com.ray.project.commons.ToastUtils;
import com.ray.project.config.MMKVManager;
import com.ray.project.databinding.FragmentConstraintEmptyBinding;
import com.ray.project.databinding.SampleAdapterMmkvBinding;
import com.ray.project.db.AppDatabase;
import com.ray.project.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 本地持久化fragment
 * @author ray
 * @date 2024/06/27
 */
public class PersistenceFragment extends BaseFragment<FragmentConstraintEmptyBinding, BasePresenter> {

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
        return R.layout.fragment_constraint_empty;
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
        setTitleText("本地持久化");

        TextView textView = new TextView(mActivity);
        textView.setId(View.generateViewId());
        ConstraintLayout.LayoutParams textParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(20, 20, 20, 20);
        textView.setText("MMKV本地持久化「演示使用String字符串」");
        textView.setTextColor(ContextCompat.getColor(mActivity, R.color.text_black));
        textView.setTypeface(null, Typeface.BOLD);
        mBinding.con.addView(textView, textParams);

        recyclerView = new RecyclerView(mActivity);
        recyclerView.setId(View.generateViewId());
        ConstraintLayout.LayoutParams recyclerViewParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 480);
        recyclerViewParams.setMargins(20, 20, 20, 20);
        recyclerViewParams.topToBottom = textView.getId();
        mBinding.con.addView(recyclerView, recyclerViewParams);

        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mmkvAdapter = new MMKVAdapter(mActivity, mMKVData);
        recyclerView.setAdapter(mmkvAdapter);
        mMKVQuery();


        TextView textView1 = new TextView(mActivity);
        textView1.setId(View.generateViewId());
        ConstraintLayout.LayoutParams textParams1 = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams1.setMargins(20, 20, 20, 20);
        textView1.setText("Jetpack Room数据库");
        textView1.setTextColor(ContextCompat.getColor(mActivity, R.color.text_black));
        textView1.setTypeface(null, Typeface.BOLD);
        textParams1.topToBottom = recyclerView.getId();
        mBinding.con.addView(textView1, textParams1);

        recyclerView1 = new RecyclerView(mActivity);
        recyclerView1.setId(View.generateViewId());
        ConstraintLayout.LayoutParams recyclerViewParams1 = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 480);
        recyclerViewParams1.setMargins(20, 20, 20, 20);
        recyclerViewParams1.topToBottom = textView1.getId();
        mBinding.con.addView(recyclerView1, recyclerViewParams1);

        // 设置布局管理器
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(mActivity);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager1);
        roomDbAdapter = new RoomDbAdapter(mActivity, roomDbData);
        recyclerView1.setAdapter(roomDbAdapter);
        dbQuery();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    private static RecyclerView recyclerView;
    private MMKVAdapter mmkvAdapter;
    private List<HashMap<String, String>> mMKVData = new ArrayList<>();
    private void mMKVQuery () {
        mMKVData.clear();
        String[] keys = MMKVManager.getInstance().keys();
        for (String key : keys) {
            HashMap<String, String> map = new HashMap<>();
            map.put("key", key);
            map.put("value", MMKVManager.getInstance().decodeString(key));
            mMKVData.add(map);
        }
        if (mMKVData.isEmpty()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("key", "");
            map.put("value", "");
            mMKVData.add(map);
        }
        mmkvAdapter.notifyDataSetChanged();
    }
    private static class MMKVAdapter extends BaseAdapter<HashMap<String, String>, SampleAdapterMmkvBinding> {

        public MMKVAdapter(BaseActivity activity, List<HashMap<String, String>> data) {
            super(activity, data);
        }

        @Override
        protected SampleAdapterMmkvBinding onCreateViewBinding(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
            mBinding = SampleAdapterMmkvBinding.inflate(inflater, parent, false);
            return (SampleAdapterMmkvBinding) mBinding;
        }

        @Override
        protected void onConvert(SampleAdapterMmkvBinding binding, int position) {
            HashMap<String, String> itemData = mData.get(position);
            binding.etKey.setText(itemData.get("key"));
            binding.etValue.setText(itemData.get("value"));

            binding.btSave.setOnClickListener(v -> {
                String key = binding.etKey.getText().toString().trim();
                String value = binding.etValue.getText().toString().trim();
                if (!TextUtils.isEmpty(key)) {
                    MMKVManager.getInstance().encode(key, value);
                }
            });

            binding.btDelete.setOnClickListener(v -> {
                mData.remove(position);
                notifyItemRemoved(position);
                MMKVManager.getInstance().removeKey(itemData.get("key"));

                if (mData.isEmpty()) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("key", "");
                    map.put("value", "");
                    mData.add(map);
                }
                notifyItemInserted(mData.size() - 1);
            });

            binding.btAdd.setOnClickListener(v -> {
                HashMap<String, String> map = new HashMap<>();
                map.put("key", "");
                map.put("value", "");
                mData.add(map);
                notifyItemInserted(mData.size() - 1);
                recyclerView.scrollToPosition(mData.size() - 1);
            });
        }
    }

    private static RecyclerView recyclerView1;
    private static RoomDbAdapter roomDbAdapter;
    private static List<User> roomDbData = new ArrayList<>();
    private void dbQuery () {
        roomDbData.clear();

        new Thread(() -> {
            roomDbData.addAll(AppDatabase.getInstance(mActivity).userDao().getAll());

            if (roomDbData.isEmpty()) {
                User user = new User();
                user.firstName = "";
                user.lastName = "";
                roomDbData.add(user);
            }
            roomDbAdapter.notifyDataSetChanged();
        }).start();
    }
    private static class RoomDbAdapter extends BaseAdapter<User, SampleAdapterMmkvBinding> {

        public RoomDbAdapter(BaseActivity activity, List<User> data) {
            super(activity, data);
        }

        @Override
        protected SampleAdapterMmkvBinding onCreateViewBinding(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
            mBinding = SampleAdapterMmkvBinding.inflate(inflater, parent, false);
            return (SampleAdapterMmkvBinding) mBinding;
        }

        @Override
        protected void onConvert(SampleAdapterMmkvBinding binding, int position) {
            User itemData = mData.get(position);
            binding.etKey.setHint("请输入姓氏");
            binding.etValue.setHint("请输入名字");

            binding.etKey.setText(itemData.firstName);
            binding.etValue.setText(itemData.lastName);

            binding.btSave.setOnClickListener(v -> {
                String key = binding.etKey.getText().toString().trim();
                String value = binding.etValue.getText().toString().trim();

                if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                    return;
                }

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
            });

            binding.btDelete.setOnClickListener(v -> {
                new Thread(() -> {
                    if (null != AppDatabase.getInstance(mActivity).userDao().findById(itemData.uid)) {
                        int deleteResult = AppDatabase.getInstance(mActivity).userDao().delete(itemData);
                        if (deleteResult <= 0) {
                            mActivity.runOnUiThread(() -> {
                                ToastUtils.showCustomToast(mActivity, "删除失败", Toast.LENGTH_SHORT);
                            });
                            return;
                        }
                    }
                    mActivity.runOnUiThread(() -> {
                        roomDbData.remove(position);
                        roomDbAdapter.notifyItemRemoved(position);
                        ToastUtils.showCustomToast(mActivity, "已删除", Toast.LENGTH_SHORT);
                    });

                    mActivity.runOnUiThread(() -> {
                        if (mData.isEmpty()) {
                            User user = new User();
                            user.firstName = "";
                            user.lastName = "";
                            mData.add(user);
                            notifyItemInserted(mData.size() - 1);
                        }
                    });
                }).start();
            });

            binding.btAdd.setOnClickListener(v -> {
                User user = new User();
                user.firstName = "";
                user.lastName = "";
                mData.add(user);
                notifyItemInserted(mData.size() - 1);
                recyclerView1.scrollToPosition(mData.size() - 1);
            });
        }
    }
}
