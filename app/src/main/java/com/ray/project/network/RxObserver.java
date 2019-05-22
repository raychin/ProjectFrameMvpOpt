package com.ray.project.network;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Button;
import android.widget.Toast;

import com.ray.project.commons.Loading;
import com.ray.project.commons.Logger;
import com.ray.project.commons.ToastUtils;

import rx.Observer;
import okhttp3.Dispatcher;

/**
 * rxAndroid服务器数据返回处理
 * @author ray
 * @date 2018/07/03
 */
public class RxObserver<T> implements Observer<T> {
    protected final String TAG = this.getClass().getSimpleName();

    private final Context mContext;
    private final RxResult<T> mResult;
    private Dispatcher dispatcher;
    private boolean isNeedError;
    SwipeRefreshLayout mSr;
    Button mBtn;

    public RxObserver(Context context, RxResult<T> result) {
        this.mContext = context;
        this.mResult = result;
    }

    public RxObserver(Context context, RxResult<T> result, SwipeRefreshLayout mSr) {
        this.mContext = context;
        this.mResult = result;
        this.mSr = mSr;
    }


    public RxObserver(Context context, RxResult<T> result, Button btn) {
        this.mContext = context;
        this.mResult = result;
        this.mBtn = btn;
    }

    public RxObserver(Context context, RxResult<T> result, boolean isNeedError) {
        this.mContext = context;
        this.mResult = result;
        this.isNeedError = isNeedError;
    }

    @Override
    public void onCompleted() {
        Loading.dismiss();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Loading.dismiss();
        if (mSr != null) { mSr.setRefreshing(false); }
        if (mBtn != null) { mBtn.setEnabled(true); }
        ToastUtils.showToast(mContext, "网络异常", Toast.LENGTH_SHORT);
    }

    @Override
    public void onNext(T t) {
        if(t instanceof Result) {
            Logger.e(TAG, ((Result) t).retMsg);
            mResult.onResult(t);
            return;
        }
        /*
        if (t instanceof Result) {
            Logger.e(TAG, "data:" + String.valueOf((Result) t));
            if (((Result) t).getReturnCode().equals("200")) {
                if (((Result) t).getData() == null) {
                    ((Result) t).setData("");
                    mResult.onResult(t);
                    return;
                }
                if (((Result) t).getSecure()) {
//                    String datas = AES256Util.decrypt(String.valueOf(((Result) t).getData()));
//                    ((Result) t).setData(datas);
                } else {
                    ((Result) t).setData(new Gson().toJson(((Result) t).getData()));
                }
                if (mSr != null) { mSr.setRefreshing(false); }
                mResult.onResult(t);
            } else if (((Result) t).getReturnCode().equals("-99")) {
//                Intent intent = new Intent(mContext, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("token", true);
//                mContext.startActivity(intent);
            } else if (((Result) t).getReturnCode().equals("401")) {
//                SPUserInfo.clear(mContext);
//                Intent intent = new Intent(mContext, LoginAct.class);
//                intent.putExtra("Type", 1);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mContext.startActivity(intent);
            } else {
                if (isNeedError) {
                    mResult.onResult(t);
                    return;
                }
                if (mBtn != null) { mBtn.setEnabled(true); }
                ToastUtils.showToast(mContext, ((Result) t).getMsg(), Toast.LENGTH_SHORT);
            }
        }
        */
    }

    public interface RxResult<T> {
        void onResult(T t);
    }
}
