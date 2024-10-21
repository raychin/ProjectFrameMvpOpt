package com.ray.project.ui.fragment.qrCode;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.ray.project.R;
import com.ray.project.base.BaseFragment;
import com.ray.project.base.BasePresenter;
import com.ray.project.config.MMKVManager;
import com.ray.project.databinding.FragmentConstraintEmptyBinding;

/**
 * 非网页地址二维码扫描结果fragment
 * @author ray
 * @date 2024/10/12
 */
public class ScanResultFragment extends BaseFragment<FragmentConstraintEmptyBinding, BasePresenter> {

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
    }

    private TextView textView;
    @Override
    protected void initView(View view) {
        setTitleText("扫描结果");

        String result = MMKVManager.getInstance().decodeString("scanResult");
        MMKVManager.getInstance().removeKey("scanResult");

        textView = new TextView(mActivity);
        textView.setBackgroundResource(R.drawable.bt_shape);
        textView.setId(View.generateViewId());
        ConstraintLayout.LayoutParams textParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(20, 20, 20, 20);
        textView.setPadding(15, 15, 15, 15);
        textView.setMinHeight(320);
        textView.setText(result);
        textView.setLongClickable(true);
        textView.setTextIsSelectable(true);
        mBinding.con.addView(textView, textParams);

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
}
