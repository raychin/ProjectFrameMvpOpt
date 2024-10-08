package com.ray.project.ui.fragment.qrCode;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.VIBRATOR_SERVICE;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.photo.select.ImageConfig;
import com.photo.select.ImageSelector;
import com.photo.select.ImageSelectorActivity;
import com.ray.project.R;
import com.ray.project.base.BaseFragment;
import com.ray.project.base.BasePresenter;
import com.ray.project.commons.GlideLoader;
import com.ray.project.commons.Logger;
import com.ray.project.databinding.FragmentScanZxingBinding;
import com.ray.project.ui.WebViewActivity;

import java.util.ArrayList;

import cn.ray.qrcode.core.QRCodeView;

/**
 * zxing二维码扫描功能fragment
 * @author ray
 * @date 2024/06/27
 */
public class ScanFragment extends BaseFragment<FragmentScanZxingBinding, BasePresenter> implements QRCodeView.Delegate {

    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 2177;
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
        return R.layout.fragment_scan_zxing;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity.checkPermissions(
            new String[] {
                Manifest.permission.CAMERA
            }
        );
    }

    private ImageConfig imageConfig;
    private ArrayList<String> selected = new ArrayList<>();
    private final static int RESULT_PHOTO_CODE = 101;
    @Override
    protected void initView(View view) {
        setTitleText("扫一扫");
        mBinding.zXingView.setDelegate(this);

        imageConfig = new ImageConfig.Builder(
                new GlideLoader())
                .steepToolBarColor(getResources().getColor(R.color.actionbar))
                .titleBgColor(getResources().getColor(R.color.actionbar))
                .titleSubmitTextColor(getResources().getColor(R.color.material_white))
                .titleTextColor(getResources().getColor(R.color.material_white))
                // 单选
                .singleSelect()
                // 已选择的图片路径
                .pathList(selected)
                // 拍照后存放的图片路径（默认 /temp/picture）
                //.filePath("/temp")
                // 开启拍照功能（默认关闭）
                // .showCamera()
                .requestCode(RESULT_PHOTO_CODE)
                .build();

        mBinding.chooseQrCodeFromGallery.setOnClickListener(v -> {
            ImageSelector.open(mActivity, imageConfig);
        });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 显示扫描框，并开始识别
        mBinding.zXingView.startSpotAndShowRect();

        if (resultCode == RESULT_OK && requestCode == RESULT_PHOTO_CODE) {
            selected = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            if (null != selected && !selected.isEmpty()) {
                // 本来就用到 QRCodeView 时可直接调 QRCodeView 的方法，走通用的回调
                mBinding.zXingView.decodeQRCode(selected.get(0));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // 打开后置摄像头开始预览，但是并未开始识别
        mBinding.zXingView.startCamera();
        // 打开前置摄像头开始预览，但是并未开始识别
//        mBinding.zXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

        // 显示扫描框，并开始识别
        mBinding.zXingView.startSpotAndShowRect();
    }

    @Override
    public void onStop() {
        // 关闭摄像头预览，并且隐藏扫描框
        mBinding.zXingView.stopCamera();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        // 销毁二维码扫描控件
        mBinding.zXingView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) mActivity.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();

        if (TextUtils.isEmpty(result)) {
            return;
        }

        if (result.startsWith("http://") || result.startsWith("https://")) {
            mActivity.nextActivity(WebViewActivity.class, true, BaseFragment.WEB_VIEW_URL_KEY, result);
            return;
        }

        setTitleText("扫描结果为：" + result);
        // 开始识别
        mBinding.zXingView.startSpot();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mBinding.zXingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mBinding.zXingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mBinding.zXingView.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Logger.e(TAG, "打开相机出错");
    }
}