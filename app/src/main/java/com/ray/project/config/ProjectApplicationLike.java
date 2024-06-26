package com.ray.project.config;

import android.app.Application;
import android.os.Build;

import com.ray.project.BuildConfig;
import com.ray.project.commons.Logger;
import com.tencent.rfix.anno.ApplicationLike;
import com.tencent.rfix.entry.DefaultRFixApplicationLike;
import com.tencent.rfix.entry.RFixApplicationLike;
import com.tencent.rfix.lib.RFixInitializer;
import com.tencent.rfix.lib.RFixListener;
import com.tencent.rfix.lib.RFixParams;
import com.tencent.rfix.lib.config.PatchConfig;
import com.tencent.rfix.lib.entity.RFixPatchResult;
import com.tencent.rfix.loader.entity.RFixLoadResult;
import com.tencent.rfix.loader.log.IRFixLog;
import com.tencent.rfix.loader.log.RFixLog;

/**
 * @Description: rfix热修复
 * @Author: ray
 * @Date: 26/6/2024
 */
@ApplicationLike(application = ".ProjectLikeApplication")
public class ProjectApplicationLike extends DefaultRFixApplicationLike {

    private static final String TAG = "RFix.ProjectApplicationLike";

    public ProjectApplicationLike(Application application, RFixLoadResult loadResult) {
        super(application, loadResult);
    }

//    @Override
//    public void onBaseContextAttached(Context base) {
//        super.onBaseContextAttached(base);
//
//        // 初始化RFix组件
//        initRFixAsync(this);
//    }

    public static void initRFix(RFixApplicationLike applicationLike) {
        Logger.d(TAG, "initRFix...");

        // 1. 初始化日志接口
        RFixLog.setLogImpl(new CustomRFixLog());
        // 2. 构造RFix业务参数
        // appId和appKey在RFix发布平台注册应用时获得
        final String appId = BuildConfig.SHIPLY_APPID;
        final String appKey = BuildConfig.SHIPLY_APPKEY;
        RFixParams params = new RFixParams(appId, appKey)
                // 设置业务使用的设备ID，方便进行数据统计（如：QIMEI等）
                .setDeviceId("000000")
                // 设置设备厂商，用于下发规则控制
                .setDeviceManufacturer(Build.MANUFACTURER)
                // 设置设备型号，用于下发规则控制
                .setDeviceModel(Build.MODEL)
                // 设置用户ID，用于下发规则控制
                .setUserId("123456")
                // 设置自定义属性，用于扩展下发规则控制
                .setCustomProperty("property1", "xxx");
        // 3. 初始化RFix组件
        RFixInitializer.initialize(applicationLike, params);

        // 在初始化时可以注册回调，以便在各阶段处理业务逻辑
        RFixInitializer.initialize(applicationLike, params, new RFixListener() {
            @Override
            public void onConfig(boolean success, int resultCode, PatchConfig patchConfig) {
                // 处理配置
                Logger.d(TAG, "onConfig success: " + success + ", resultCode: " + resultCode + ", patchConfig: " + patchConfig);
            }
            @Override
            public void onDownload(boolean success, int resultCode, PatchConfig patchConfig, String patchFilePath) {
                // 处理下载文件
                Logger.d(TAG, "onDownload success: " + success + ", resultCode: " + resultCode + ", patchFilePath: " + patchFilePath);
            }
            @Override
            public void onInstall(boolean success, int resultCode, RFixPatchResult patchResult) {
                if (success && patchResult.isPatchSuccessFirstTime()) {
                    // 补丁安装成功，可以记录一个标记，并在合适的时机重启应用，以加快补丁生效速度
                    Logger.d(TAG, "onInstall success: " + success + ", patchResult: " + patchResult);
                }
            }
        });
    }

    public static void initRFixAsync(RFixApplicationLike applicationLike) {
        Logger.d(TAG, "initRFixAsync...");

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                Logger.e(TAG, "initRFixAsync fail!" + e);
            }

            initRFix(applicationLike);
        });
        thread.start();
    }

    private static class CustomRFixLog implements IRFixLog {
        @Override
        public void v(String s, String s1) {
            Logger.v(s, s1);
        }

        @Override
        public void d(String s, String s1) {
            Logger.d(s, s1);
        }

        @Override
        public void i(String s, String s1) {
            Logger.i(s, s1);
        }

        @Override
        public void w(String s, String s1) {
            Logger.e(s, s1);
        }

        @Override
        public void w(String s, String s1, Throwable throwable) {
            Logger.e(s, s1);
        }

        @Override
        public void e(String s, String s1) {
            Logger.e(s, s1);
        }

        @Override
        public void e(String s, String s1, Throwable throwable) {
            Logger.e(s, s1);
        }
    }
}
