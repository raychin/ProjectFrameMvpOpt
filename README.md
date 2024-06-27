# ProjectFrameMvpOpt



## 1. 说明

基于android平台，采用mvp架构，实现androidx项目基本框架。

集成包括androidx、retrofit2、glide4.5、Luban1.8、mmkv1.3.5、room2.3.0等开源框架，
[主下载地址](https://common.shiply-cdn.qq.com/gray/c6097cfbf6/prod/1719389187/app1.0.1_2_ray_2024-06-26.apk)
[备用地址](https://github.com/raychin/ProjectFrameMvpOpt/releases/download/untagged-05cfadbb50456ff0749f/app1.0.1_2_ray_2024-06-25.apk)

<div>
    <img src="https://gitee.com/KingsRay/gitee-image-host/raw/master/image/image-20240625171135839.png" alt="image-20240625171135839" width="15%" height="15%" />
    <img src="https://gitee.com/KingsRay/gitee-image-host/raw/master/image/image-20240627153650231.png" alt="image-20240625171606099" width="15%" height="15%" />
    <img src="https://gitee.com/KingsRay/gitee-image-host/raw/master/image/image-20240625171457865.png" alt="image-20240625171457865" width="15%" height="15%" />
	<img src="https://gitee.com/KingsRay/gitee-image-host/raw/master/image/image-20240625171227927.png" alt="image-20240625171227927" width="15%" height="15%" />
    <img src="https://gitee.com/KingsRay/gitee-image-host/raw/master/image/image-20240625171418584.png" alt="image-20240625171418584" width="15%" height="15%" />
    <img src="https://gitee.com/KingsRay/gitee-image-host/raw/master/image/image-20240625171606099.png" alt="image-20240625171606099" width="15%" height="15%" />
</div>






## 2. 集成功能



### 初始版本

-   网络请求封装
-   持久化MMKV，替换SharePreferences方案
-   数据库room操作封装
-   WebView封装，支持原生应用同vue、uni小程序交互；支持图片选取及压缩



### 2024年06月26日集成

-   集成腾讯bugly错误日志功能框架
    https://bugly.qq.com/
-   集成腾讯热修复shiply框架
    https://shiply.tds.qq.com/



### 2024年06月27日集成定位功能

-   LocationUtils使用

    ```java
    LocationUtils.getInstance(mActivity).setAddressCallback(new LocationUtils.AddressCallback() {
        @Override
        public void onGetAddress(Address address) {
            String countryName = address.getCountryName();//国家
            String adminArea = address.getAdminArea();//省
            String locality = address.getLocality();//市
            String subLocality = address.getSubLocality();//区
            String featureName = address.getFeatureName();//街道
            Logger.d("定位地址", countryName + adminArea + locality + subLocality + featureName);
        }
    
        @Override
        public void onGetLocation(double lat, double lng) {
            Logger.d("定位经纬度", lat + ", " + lng);
        }
    });
    
    
    @Override
    public void onStop() {
        super.onStop();
        // 销毁定位，避免持续定位耗电
        LocationUtils.getInstance(mActivity).clearAddressCallback();
    }
    ```



其他功能陆续集成开发中...

