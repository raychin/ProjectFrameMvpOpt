package com.ray.project.network;

import rx.Subscription;

public interface RxActionManager<T> {
    void add(T tag, Subscription subscription);
    void remove(T tag);
    void cancel(T tag);
    void cancelAll();
}
