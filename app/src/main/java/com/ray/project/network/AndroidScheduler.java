package com.ray.project.network;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * 仿照rxAndroid中的AndroidSchedulers实现切换到主进程
 *
 * @author ray
 * @date 2019-05-22 10:18
 */
public class AndroidScheduler implements Executor {

    private static AndroidScheduler instance;

    private final Scheduler mMainScheduler;
    private final Handler mHandler;

    private AndroidScheduler() {
        mHandler = new Handler(Looper.myLooper());
        mMainScheduler = Schedulers.from(this);
    }

    public static synchronized Scheduler mainThread() {
        if (instance == null) {
            instance = new AndroidScheduler();
        }
        return instance.mMainScheduler;
    }

    @Override
    public void execute(@NonNull Runnable command) {
        mHandler.post(command);
    }

}
