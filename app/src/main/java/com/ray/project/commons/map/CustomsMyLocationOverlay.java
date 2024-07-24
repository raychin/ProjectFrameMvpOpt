package com.ray.project.commons.map;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

/**
 * @Description: 类说明
 * @Author: ray
 * @Date: 23/7/2024
 */
public class CustomsMyLocationOverlay extends MyLocationNewOverlay {
    public CustomsMyLocationOverlay(MapView mapView) {
        super(mapView);
    }

    public CustomsMyLocationOverlay(IMyLocationProvider myLocationProvider, MapView mapView) {
        super(myLocationProvider, mapView);
    }
}
