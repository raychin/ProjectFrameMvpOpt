package com.ray.project.commons;

//import android.app.ProgressDialog;
import android.content.Context;

import com.ray.project.widget.LoadingDialog;

/**
 * 加载框
 * @author ray
 * @date 2018/07/03
 */
public class Loading {
//    private static ProgressDialog mDialog;
    private static LoadingDialog mDialog;

    public static void show(Context context) {
        dismiss();
//        mDialog = new ProgressDialog(context);
//        mDialog.setMessage(msg);
        mDialog = new LoadingDialog(context);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public static void show(Context context, String msg) {
        dismiss();
//        mDialog = new ProgressDialog(context);
//        mDialog.setMessage(msg);
        mDialog = new LoadingDialog(context).setMessage(msg);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public static void dismiss() {
        if (mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mDialog = null;
        }
    }
}
