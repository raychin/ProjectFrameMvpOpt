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
    private static volatile Loading instance;

//    private static ProgressDialog mDialog;
    private LoadingDialog mDialog;

    private Loading() {}
    public static Loading getInstance() {
        if (instance == null) {
            synchronized (Loading.class) {
                if (instance == null) {
                    instance = new Loading();
                }
            }
        }
        return instance;
    }
    public void show(Context context) {
        dismiss();
//        mDialog = new ProgressDialog(context);
//        mDialog.setMessage(msg);
        mDialog = new LoadingDialog(context);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void show(Context context, String msg) {
        dismiss();
//        mDialog = new ProgressDialog(context);
//        mDialog.setMessage(msg);
        mDialog = new LoadingDialog(context).setMessage(msg);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void dismiss() {
        if (mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mDialog = null;
        }
    }
}
