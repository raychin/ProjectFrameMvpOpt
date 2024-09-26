package com.ray.project.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.ray.project.R;
import com.ray.project.base.BaseActivity;
import com.ray.project.databinding.PopupMenuBinding;

/**
 * @Description: 自定义下拉菜单
 * @Author: ray
 * @Date: 26/9/2024
 */
public class PopupMenu extends PopupWindow {
    private PopupMenuBinding menuBinding;
    private BaseActivity mActivity;
    public PopupMenu (final BaseActivity context) {
        mActivity = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuBinding = PopupMenuBinding.inflate(inflater);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(menuBinding.getRoot());
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 3 + 40);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置外部可点击
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupMenuAnimationPreview);

        menuBinding.scanText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //do something you need here
                PopupMenu.this.dismiss();
            }
        });
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, parent.getLayoutParams().width / 3, 5);
        } else {
            this.dismiss();
        }
    }
}