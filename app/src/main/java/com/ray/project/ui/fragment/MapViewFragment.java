package com.ray.project.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ray.project.R;
import com.ray.project.base.BaseFragment;
import com.ray.project.base.BasePresenter;
import com.ray.project.commons.Logger;
import com.ray.project.databinding.FragmentMapViewBinding;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TMSOnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayManager;
import org.osmdroid.views.overlay.TilesOverlay;

/**
 * MapView界面fragment
 * @author ray
 * @date 2018/07/03
 */
public class MapViewFragment extends BaseFragment<FragmentMapViewBinding, BasePresenter> {
    private MapView mapView;

    @Override
    protected boolean isImmersiveStatusHeight() {
        return true;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_web_view;
    }

    @Override
    protected void initView(View view) {
//        mapView = mBinding.mapView;

        mapView = new MapView(mActivity);
        mapView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // add the mapView instance to the main layout
        mBinding.mainRlNonMap.addView(mapView);

//        config.InitMapOverlays(mapView, mActivity);
//        Configuration.getInstance().load(mActivity, mActivity.getSharedPreferences("map", Context.MODE_PRIVATE));
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (null != bundle) {
//            if (bundle.containsKey(WEB_VIEW_URL_KEY)) {
//            }
        }

        // setContentView之前执行，放到activity或者application中 osmdroid map 初始化
//        Configuration.getInstance().load(this, getSharedPreferences("map", Context.MODE_PRIVATE));
        initMapView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return false;
    }

    private void initMapView () {
        // 设置地图类型
//        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMinZoomLevel(0.00);
        mapView.setMaxZoomLevel(20.00);

        // 设置让瓦片适应不同像素密度:默认地图显示的字体小，图片像素高，可设置以下代码，使地图适应不同像素密度，更美观
        mapView.setTilesScaledToDpi(true);

        // 启用触控缩放及滑动手势
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);


//        mapView?.overlayManager?.tilesOverlay?.isEnabled = true
//        mapView?.isSelected = true
//        var dm = resources.displayMetrics
//        //指南针
//        var mCompassOverlay = CompassOverlay(
//                this, InternalCompassOrientationProvider(this),
//                mapView
//        )
//        mCompassOverlay.enableCompass()
//        mapView?.getOverlays()?.add(mCompassOverlay)
//        //比例尺配置
//        var mScaleBarOverlay = ScaleBarOverlay(mapView)
//        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
//        mapView?.getOverlays()?.add(mScaleBarOverlay)
//        mapView?.overlays?.add(mScaleBarOverlay)
//        //定位
//        var mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), mapView)
//        mapView?.overlays?.add(mLocationOverlay)
//        mLocationOverlay.enableMyLocation()

        // 设置地图中心经纬度和缩放级别
        mapView.getController().setCenter(new GeoPoint(22.5464293, 114.0528185));
        mapView.getController().setZoom(15);

        mapView.setUseDataConnection(true);

//        mapView.setTileSource(iServerMapsSource);
        mapView.setTileSource(wprdMapsSource);
//        mapView.setTileSource(szMapsSource);
//        mapView.setTileSource(tianDiTuImgTileSource);
//        mapView.setTileSource(tianDiTuImg2TileSource);
//        TilesOverlay tilesOverlay = new TilesOverlay(new MapTileProviderBasic(mActivity, tianDiTuCiaTileSource), mActivity);
//        mapView.getOverlayManager().add(tilesOverlay);
    }


    static String wz = "tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=6cda6bd921b61ce58c7cece2f3ba578c";
    // 影像地图 _W是墨卡托投影  _c是国家2000的坐标系
    public static OnlineTileSourceBase tianDiTuImgTileSource = new XYTileSource("Tian Di Tu Img", 0, 18, 256, "",
            new String[] {
                    "https://t0." + wz,
                    "https://t1." + wz,
                    "https://t2." + wz,
                    "https://t3." + wz,
                    "https://t4." + wz,
                    "https://t5." + wz,
                    "https://t6." + wz,
                    "https://t7." + wz
    }) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            Logger.d("url", getBaseUrl()
                    + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex)
                    + "&TILECOL=" + MapTileIndex.getX(pMapTileIndex)
                    + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex)
                    + mImageFilenameEnding
            );
            return getBaseUrl()
                    + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex)
                    + "&TILECOL=" + MapTileIndex.getX(pMapTileIndex)
                    + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex)
                    + mImageFilenameEnding;
        }
    };

    // 影像地图 _W是墨卡托投影  _c是国家2000的坐标系
    OnlineTileSourceBase tianDiTuImg2TileSource = new XYTileSource("Tian Di Tu Img", 1, 18, 256, "",
            new String[] {
                    "http://t1.tianditu.com/DataServer?T=img_w&tk=6cda6bd921b61ce58c7cece2f3ba578c",
                    "http://t2.tianditu.com/DataServer?T=img_w&tk=6cda6bd921b61ce58c7cece2f3ba578c",
                    "http://t3.tianditu.com/DataServer?T=img_w&tk=6cda6bd921b61ce58c7cece2f3ba578c",
                    "http://t4.tianditu.com/DataServer?T=img_w&tk=6cda6bd921b61ce58c7cece2f3ba578c",
                    "http://t5.tianditu.com/DataServer?T=img_w&tk=6cda6bd921b61ce58c7cece2f3ba578c",
                    "http://t6.tianditu.com/DataServer?T=img_w&tk=6cda6bd921b61ce58c7cece2f3ba578c"
    }) {
        @Override
        public String getTileURLString(final long pMapTileIndex){
            Logger.d("url", getBaseUrl()
                    + "&X=" + MapTileIndex.getX(pMapTileIndex)
                    + "&Y=" + MapTileIndex.getY(pMapTileIndex)
                    + "&L=" + MapTileIndex.getZoom(pMapTileIndex)
                    + mImageFilenameEnding
            );
            return getBaseUrl()
                    + "&X=" + MapTileIndex.getX(pMapTileIndex)
                    + "&Y=" + MapTileIndex.getY(pMapTileIndex)
                    + "&L=" + MapTileIndex.getZoom(pMapTileIndex)
                    + mImageFilenameEnding;
        }
    };

    // 影像标注 _W是墨卡托投影  _c是国家2000的坐标系
    OnlineTileSourceBase tianDiTuCiaTileSource = new OnlineTileSourceBase("Tian Di Tu Cia", 0, 18, 256, "",
            new String[]{
                    "http://t1.tianditu.com/DataServer?T=cia_w&tk=6cda6bd921b61ce58c7cece2f3ba578c",
                    "http://t2.tianditu.com/DataServer?T=cia_w&tk=6cda6bd921b61ce58c7cece2f3ba578c",
                    "http://t3.tianditu.com/DataServer?T=cia_w&tk=6cda6bd921b61ce58c7cece2f3ba578c",
                    "http://t4.tianditu.com/DataServer?T=cia_w&tk=6cda6bd921b61ce58c7cece2f3ba578c",
                    "http://t5.tianditu.com/DataServer?T=cia_w&tk=6cda6bd921b61ce58c7cece2f3ba578c",
                    "http://t6.tianditu.com/DataServer?T=cia_w&tk=6cda6bd921b61ce58c7cece2f3ba578c"
    }) {
        @Override
        public String getTileURLString(final long pMapTileIndex){
            Logger.d("url", getBaseUrl()
                    + "&X=" + MapTileIndex.getX(pMapTileIndex)
                    + "&Y=" + MapTileIndex.getY(pMapTileIndex)
                    + "&L=" + MapTileIndex.getZoom(pMapTileIndex)
                    + mImageFilenameEnding
            );
            return getBaseUrl()
                    + "&X=" + MapTileIndex.getX(pMapTileIndex)
                    + "&Y=" + MapTileIndex.getY(pMapTileIndex)
                    + "&L=" + MapTileIndex.getZoom(pMapTileIndex)
                    + mImageFilenameEnding;
        }
    };

    // 已验证
    OnlineTileSourceBase iServerMapsSource = new OnlineTileSourceBase("iserver map", 1, 18, 256, "",
            new String[] { "https://iserver.supermap.io/iserver/services/map-china400/rest/maps/China/zxyTileImage.png" }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            Logger.d("url", getBaseUrl()
                    + "?z=" + MapTileIndex.getZoom(pMapTileIndex)
                    + "&y=" + MapTileIndex.getY(pMapTileIndex)
                    + "&x=" + MapTileIndex.getX(pMapTileIndex)
                    + mImageFilenameEnding);
            return getBaseUrl()
                    + "?z=" + MapTileIndex.getZoom(pMapTileIndex)
                    + "&y=" + MapTileIndex.getY(pMapTileIndex)
                    + "&x=" + MapTileIndex.getX(pMapTileIndex)
                    + mImageFilenameEnding;
        }
    };

    // 已验证
    OnlineTileSourceBase wprdMapsSource = new TMSOnlineTileSourceBase("wprd map", 0, 18, 256, "",
            new String[] {
                    "http://wprd01.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scl=1&style=7",
                    "http://wprd02.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scl=1&style=7",
                    "http://wprd03.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scl=1&style=7",
                    "http://wprd04.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scl=1&style=7",
            }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            Logger.d("url", getBaseUrl()
                    + "&x=" + MapTileIndex.getX(pMapTileIndex)
                    + "&y=" + MapTileIndex.getY(pMapTileIndex)
                    + "&z=" + MapTileIndex.getZoom(pMapTileIndex)
                    + mImageFilenameEnding);
            return getBaseUrl()
                    + "&x=" + MapTileIndex.getX(pMapTileIndex)
                    + "&y=" + MapTileIndex.getY(pMapTileIndex)
                    + "&z=" + MapTileIndex.getZoom(pMapTileIndex)
                    + mImageFilenameEnding;
        }
    };

    OnlineTileSourceBase szMapsSource = new OnlineTileSourceBase("sz map", 0, 18, 256, "",
//            new String[] {"http://pnr.sz.gov.cn/d-suplicmap/tilemap_1/rest/services/SZMAP_BASEMAP_GK2K/MapServer/tile/{z}/{y}/{x}",
            new String[] {
                    "http://pnr.sz.gov.cn/d-suplicmap/tilemap_1/rest/services/SZMAP_BASEMAP_GK2K/MapServer/tile/",
            }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            Logger.d("url", getBaseUrl()
                    + MapTileIndex.getZoom(pMapTileIndex)
                    + "/" + MapTileIndex.getY(pMapTileIndex)
                    + "/" + MapTileIndex.getX(pMapTileIndex)
                    + mImageFilenameEnding);
            return getBaseUrl()
                    + MapTileIndex.getZoom(pMapTileIndex)
                    + "/" + MapTileIndex.getY(pMapTileIndex)
                    + "/" + MapTileIndex.getX(pMapTileIndex)
                    + mImageFilenameEnding;
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();

//        // 释放资源
//        if (mapView != null) {
//            mapView = null;
//        }
    }
}
