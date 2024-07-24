package com.ray.project.commons.map;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import com.ray.project.config.MMKVManager;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

/**
 * @Description: OsmdroidMap配置
 * @Author: ray
 * @Date: 23/7/2024
 */
public class CustomsOsmdroidMapConfig {

    private static volatile CustomsOsmdroidMapConfig instance;
    // 地图旋转
    private RotationGestureOverlay mRotationGestureOverlay;
    // 比例尺
    private ScaleBarOverlay mScaleBarOverlay;
    // 指南针方向
    private CompassOverlay mCompassOverlay = null;
    // 设置导航图标的位置
    protected MyLocationNewOverlay mLocationOverlay;
    // 是否地图旋转
    protected boolean mbRotation = false;

    public CustomsOsmdroidMapConfig() {
    }

    public static CustomsOsmdroidMapConfig getInstance() {
        if (instance == null) {
            synchronized (CustomsOsmdroidMapConfig.class) {
                if (instance == null) {
                    instance = new CustomsOsmdroidMapConfig();
                }
            }
        }
        return instance;
    }

    public void InitMapOverlays(MapView mapView, Activity context) {
        mapView.setDrawingCacheEnabled(true);
        mapView.setMaxZoomLevel(29.0);
        mapView.setMinZoomLevel(5.0);
        mapView.getController().setZoom(12.0);
        mapView.setUseDataConnection(true);
        // 触控放大缩小
        mapView.setMultiTouchControls(true);
        mapView.getOverlayManager().getTilesOverlay().setEnabled(true);
        // 禁止自动出现放大，缩小的按钮 osmdroid 6.0以后才有的
        mapView.setBuiltInZoomControls(false);

        if (mbRotation) {
            // 地图自由旋转
            mRotationGestureOverlay = new RotationGestureOverlay(mapView);
            mRotationGestureOverlay.setEnabled(true);
            mapView.getOverlays().add(mRotationGestureOverlay);
        }

        // 比例尺配置
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mScaleBarOverlay = new ScaleBarOverlay(mapView);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setAlignBottom(true); //底部显示
        mScaleBarOverlay.setLineWidth(2);
        mScaleBarOverlay.setMaxLength(1.5F);
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, dip2px(context, 60));
        mapView.getOverlays().add(mScaleBarOverlay);

        // 指南针方向
        mCompassOverlay = new CompassOverlay(context, new InternalCompassOrientationProvider(context), mapView);
        mCompassOverlay.enableCompass();
        mapView.getOverlays().add(mCompassOverlay);

        // 设置导航图标
        // 自已重写了MyLocationNewOverlay
        mLocationOverlay = new CustomsMyLocationOverlay(new GpsMyLocationProvider(context), mapView);
        mapView.getOverlays().add(mLocationOverlay);
        mLocationOverlay.enableMyLocation();

//        // 设置定位图标的样式（可选）
//        locationOverlay.setMarker(Drawable drawable);
//        locationOverlay.setDirectionIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
//        locationOverlay.setPersonIcon(Drawable drawable);
//        locationOverlay.setPersonIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        // 可以设置精度循环（可选）
        // mLocationOverlay.setFollowAccuracy(int accuracy);
    }

    public MyLocationNewOverlay getLocationOverlay(){
        return mLocationOverlay;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void onPause() {
        mCompassOverlay.disableCompass();
        mLocationOverlay.disableFollowLocation();
        mLocationOverlay.disableMyLocation();
        mScaleBarOverlay.enableScaleBar();
    }

    public void onResume(){
        mLocationOverlay.enableFollowLocation();
        mLocationOverlay.enableMyLocation();
        mScaleBarOverlay.disableScaleBar();
    }
}
