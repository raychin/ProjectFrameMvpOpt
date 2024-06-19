package com.ray.project.base;

/**
 * activity生命周期
 * @author ray
 * @date 2018/07/09
 */
public interface IActivityEvent {
    void onCreate();
    void onRestart();
    void onStart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();
}
