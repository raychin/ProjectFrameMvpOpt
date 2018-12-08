package com.ray.project.commons;

import android.content.SharedPreferences;

import com.ray.project.BuildConfig;
import com.ray.project.config.ProjectApplication;

/**
 * SharedPreferences帮助类
 * Created by Ray on 2016/5/17.
 */
public class PrefUtils {

	private static final String PREF_NAME_CACHE = BuildConfig.APPLICATION_ID + ".cache";
	private static final String PREF_NAME_VARS = BuildConfig.APPLICATION_ID + ".vars";

	public static SharedPreferences cachePrefs() {
		return ProjectApplication.get().getSharedPreferences(PREF_NAME_CACHE, 0);
	}

	public static SharedPreferences varsPrefs() {
		return ProjectApplication.get().getSharedPreferences(PREF_NAME_VARS, 0);
	}

	public static void putCacheLoginState(boolean value) {
		SharedPreferences.Editor editor = ProjectApplication.get()
				.getSharedPreferences(PREF_NAME_CACHE, 0).edit();
		editor.putBoolean("login_state", value).commit();
	}
	public static boolean getCacheLoginState() {
		return ProjectApplication.get().getSharedPreferences(PREF_NAME_CACHE, 0)
				.getBoolean("login_state", false);
	}
	
	/**
	 * 保存为解析的用户数据
	 * @param info
	 */
	public static void saveUserInfo(String info){
		SharedPreferences.Editor editor = ProjectApplication.get()
				.getSharedPreferences(PREF_NAME_CACHE, 0).edit();
		editor.putString("user_info", info).commit();
	}
	public static String getUserInfo(){
		return ProjectApplication.get().getSharedPreferences(PREF_NAME_CACHE, 0)
				.getString("user_info", "");
	}
	
	/**
	 * 保存是否接收推送通知
	 * @param state
	 */
	public static void savePushState(boolean state){
		SharedPreferences.Editor editor = ProjectApplication.get()
				.getSharedPreferences(PREF_NAME_CACHE, 0).edit();
		editor.putBoolean("msg_push", state).commit();
	}
	public static boolean getPushState(){
		return ProjectApplication.get().getSharedPreferences(PREF_NAME_CACHE, 0)
				.getBoolean("msg_push", true);
	}
    
    /**
     * 是否第三方登录
     * @param value
     */
    public static void putThirdLoginState(boolean value) {
        SharedPreferences.Editor editor = ProjectApplication.get()
                .getSharedPreferences(PREF_NAME_CACHE, 0).edit();
        editor.putBoolean("third_state", value).commit();
    }

    public static boolean getThirdLoginState() {
        return ProjectApplication.get().getSharedPreferences(PREF_NAME_CACHE, 0)
                .getBoolean("third_state", false);
    }

    /**
     * 设置更新消息
     * @param sign
     */
    public static void putUpdateMessage(String sign) {
        SharedPreferences.Editor editor = ProjectApplication.get()
                .getSharedPreferences(PREF_NAME_CACHE, 0).edit();
        editor.putString("update_message", sign).commit();
    }

    public static String getUpdateMessage() {
        return ProjectApplication.get().getSharedPreferences(PREF_NAME_CACHE, 0)
                .getString("update_message", "");
    }

	/**
	 * 设置程序第一次启动时执行
	 * @param value
	 */
	public static void putFirstLoad(String key, int mode, int value) {
		SharedPreferences.Editor editor = ProjectApplication.get()
				.getSharedPreferences(key, mode).edit();
		editor.putInt("is_first_load", value).commit();
	}

	public static int getFirstLoad(String key, int mode) {
		return ProjectApplication.get().getSharedPreferences(key, mode)
				.getInt("is_first_load", 0);
	}

}
