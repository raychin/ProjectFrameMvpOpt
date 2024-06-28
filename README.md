# ProjectFrameMvpOpt



## 1. 说明

基于android平台，采用mvp架构，实现androidx项目基本框架。

集成包括androidx、retrofit2、glide4.5、Luban1.8、mmkv1.3.5、room2.3.0等开源框架，
[主下载地址](https://common.shiply-cdn.qq.com/gray/c6097cfbf6/prod/1719479273/app1.0.4_4_ray_2024-06-27.apk)
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
    MMKV 是基于 mmap 内存映射的 key-value 组件，底层序列化/反序列化使用 protobuf 实现，性能高，稳定性强。也是腾讯微信团队使用的技术。MMKV使用了一些技术手段，如mmap文件映射和跨进程通信的共享内存，以实现更高效的数据存取操作。MMKV的性能比SharedPreferences快数十倍，尤其在读写大量数据时效果更加明显。
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

