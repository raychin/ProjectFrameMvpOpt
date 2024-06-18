package com.ray.project.commons;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by ray on 2023/07/13.
 */
public class AppTools {

    /**
     * 根据包名判断是否已安装该软件
     * @param context
     * @param packageName
     * @return
     */
    public static boolean hasInstalled(Context context, String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }
        return packageInfo != null;
    }
}
