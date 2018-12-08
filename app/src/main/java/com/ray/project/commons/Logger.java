package com.ray.project.commons;

import android.util.Log;
import com.ray.project.BuildConfig;

/**
 * log打印工具类
 * @author ray
 * @date 2018/06/22
 */
public class Logger {
	public static void i(String tag, String msg){
		if(BuildConfig.DEBUG){
			Log.i(tag, msg);
		}
	}
	public static void d(String tag, String msg){
		if(BuildConfig.DEBUG){
			Log.d(tag, msg);
		}
	}
	public static void e(String tag, String msg){
		if(BuildConfig.DEBUG){
			Log.e(tag, msg);
		}
	}
	public static void v(String tag, String msg){
		if(BuildConfig.DEBUG){
			Log.v(tag, msg);
		}
	}
}
