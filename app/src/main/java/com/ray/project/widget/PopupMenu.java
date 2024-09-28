package com.ray.project.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import androidx.core.widget.PopupWindowCompat;

import com.ray.project.R;
import com.ray.project.base.BaseActivity;
import com.ray.project.config.MMKVManager;
import com.ray.project.databinding.PopupMenuBinding;
import com.ray.project.ui.FragmentContainerActivity;

/**
 * @Description: 自定义下拉菜单
 * @Author: ray
 * @Date: 26/9/2024
 */
public class PopupMenu {
    private PopupMenuBinding menuBinding;

    private PopupWindow popupWindow;
    private BaseActivity mActivity;

    private static volatile PopupMenu instance;

    public static PopupMenu getInstance(BaseActivity context) {
        if (instance == null) {
            synchronized (MMKVManager.class) {
                if (instance == null) {
                    instance = new PopupMenu(context);
                }
            }
        }
        return instance;
    }
    public PopupMenu (BaseActivity context) {
        mActivity = context;
        popupWindow = new PopupWindow(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuBinding = PopupMenuBinding.inflate(inflater);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        popupWindow.setContentView(menuBinding.getRoot());
        // 设置SelectPicPopupWindow弹出窗体的宽
        popupWindow.setWidth(w / 3 + 40);
        // 设置SelectPicPopupWindow弹出窗体的高
        popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        popupWindow.setFocusable(true);
        // 设置外部可点击
        popupWindow.setOutsideTouchable(true);
        // 刷新状态
        popupWindow.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        popupWindow.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        popupWindow.setAnimationStyle(R.style.PopupMenuAnimationPreview);

        menuBinding.scanText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // do something you need here

                mActivity.nextActivity(FragmentContainerActivity.class, FragmentContainerActivity.FRAGMENT_PATH, "com.ray.project.ui.fragment.qrCode.ScanFragment");
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!popupWindow.isShowing()) {
            // 以下拉方式显示popupwindow
//            popupWindow.showAsDropDown(parent, parent.getLayoutParams().width / 3, 5);
            PopupWindowCompat.showAsDropDown(popupWindow, parent, parent.getLayoutParams().width / 3, -40, Gravity.START);
        } else {
            popupWindow.dismiss();
        }
    }
}