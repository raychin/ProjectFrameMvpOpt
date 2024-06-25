package com.ray.project.commons;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: LiveDataBus
 * @Author: ray
 * @Date: 25/6/2024
 */
public class LiveDataBus {
    // 存放订阅者
    private Map<String, BusMutableLiveData<Object>> bus;

    // 单例
    private static volatile LiveDataBus liveDataBus;
    private LiveDataBus () { bus = new HashMap<>(); }
    public static LiveDataBus getInstance () {
        if (null == liveDataBus) {
            synchronized (LiveDataBus.class) {
                if (null == liveDataBus) {
                    liveDataBus = new LiveDataBus();
                }
            }
        }
        return liveDataBus;
    }

    /**
     * 用来给用户进行订阅（存入map）因为可能会有很多用户进行订阅，所以加上同步
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public synchronized <T> BusMutableLiveData<T> with (String key, Class<T> type) {
        if(!bus.containsKey(key)){
            bus.put(key, new BusMutableLiveData<Object>());
        }
        return (BusMutableLiveData<T>) bus.get(key);
    }

    public BusMutableLiveData<Object> with (String key) {
        return with(key, Object.class);
    }

    public static class BusMutableLiveData<T> extends MutableLiveData<T> {
        @Override
        public void observe (@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
            super.observe(owner, observer);
            hook(observer);
        }

        private void hook (Observer<? super T> observer) {

            // 通过反射修改mLastVersion的值
            Class<LiveData> liveDataClass = LiveData.class;
            try {
                Field field = liveDataClass.getDeclaredField("mObservers");
                field.setAccessible(true);
                // 获取这个字段对应的对象
                Object mObject = field.get(this);
                // 得到SafeIterableMap.class
                Class<?> aClass = mObject.getClass();
                // 获取到SafeIterableMap的get方法
                Method get = aClass.getDeclaredMethod("get", Object.class);
                get.setAccessible(true);
                //执行get方法
                Object invoke = get.invoke(mObject, observer);
                //取到SafeIterableMap中的value
                Object observerWraper = null;
                if (invoke != null && invoke instanceof Map.Entry) {
                    observerWraper = ((Map.Entry) invoke).getValue();
                }
                if (observerWraper == null) {
                    throw new NullPointerException("observerWraper is null");
                }
                // 得到ObserverWrapper的类对象(这里需要getSuperclass是因为map集合塞的就是ObserverWrapper的子类对象)
                Class<?> superclass = observerWraper.getClass().getSuperclass();
                Field mLastVersion = superclass.getDeclaredField("mLastVersion");
                mLastVersion.setAccessible(true);

                // 得到mVersion
                Field mVersion = liveDataClass.getDeclaredField("mVersion");
                mVersion.setAccessible(true);
                //把mVersion的值填入到mLastVersion中去
                Object mVersionValue = mVersion.get(this);
                mLastVersion.set(observerWraper, mVersionValue);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
