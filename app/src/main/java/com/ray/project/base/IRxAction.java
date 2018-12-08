package com.ray.project.base;

import rx.Subscription;

/**
 * 管理网络请求
 * @author ray
 * @date 2018/07/09
 */
public interface IRxAction {
    void add(String tag, Subscription subscription);
    void remove(String tag);
    void removeAll();
    void cancel(String tag);
    void cancelAll();
}
