package com.ray.project.base;

import android.util.ArrayMap;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Set;

import rx.Subscription;

/**
 * present业务处理抽象类
 * @param <V>
 */
public abstract class BasePresenter<V extends IBaseView, T extends BaseActivity>
        implements IActivityEvent, IRxAction {
    protected final String TAG = this.getClass().getSimpleName();

    protected ArrayMap<Object, Subscription> subscriptions;

    protected Reference<V> viewRef;
    protected V view;
    protected Reference<T> activityRef;
    protected T activity;

    public BasePresenter(V view, T activity) {
        subscriptions = new ArrayMap<>();
        attachView(view);
        attachActivity(activity);
    }

    /**
     * 关联
     *
     * @param view
     */
    private void attachView(V view) {
        viewRef = new WeakReference<V>(view);
        view = viewRef.get();
    }

    /**
     * 关联
     *
     * @param activity
     */
    private void attachActivity(T activity) {
        activityRef = new WeakReference<T>(activity);
        activity = activityRef.get();
    }

    /**
     * 销毁
     */
    private void detachView() {
        if (isViewAttached()) {
            viewRef.clear();
            viewRef = null;
        }
    }

    /**
     * 销毁
     */
    private void detachActivity() {
        if (isActivityAttached()) {
            activityRef.clear();
            activityRef = null;
        }
    }

    /**
     * 获取
     *
     * @return
     */
    public V getView() {
        if (viewRef == null) {
            return null;
        }
        return viewRef.get();
    }

    /**
     * 获取
     *
     * @return
     */
    public T getActivity() {
        if (activityRef == null) {
            return null;
        }
        return activityRef.get();
    }

    /**
     * 是否已经关联
     *
     * @return
     */
    public boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    /**
     * 是否已经关联
     *
     * @return
     */
    public boolean isActivityAttached() {
        return activityRef != null && activityRef.get() != null;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void add(String tag, Subscription subscription) {
        subscriptions.put(tag, subscription);
    }

    @Override
    public void remove(String tag) {
        if (!subscriptions.isEmpty()) { subscriptions.remove(tag); }
    }

    @Override
    public void removeAll() {
        if (!subscriptions.isEmpty()) { subscriptions.clear(); }
    }

    @Override
    public void cancel(String tag) {
        if (subscriptions.isEmpty()) { return; }
        if (subscriptions.get(tag) == null) { return; }
        if (!subscriptions.get(tag).isUnsubscribed()) {
            subscriptions.get(tag).unsubscribe();
            subscriptions.remove(tag);
        }
    }

    @Override
    public void cancelAll() {
        if (subscriptions.isEmpty()) { return; }
        Set<Object> keys = subscriptions.keySet();
        for (Object apiKey : keys) { cancel(apiKey.toString()); }
    }
}
