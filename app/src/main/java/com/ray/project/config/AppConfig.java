package com.ray.project.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.ray.project.BuildConfig;
import com.ray.project.commons.AssignValueForAttributeUtils;
import com.ray.project.commons.FileUtils;
import com.ray.project.commons.Logger;
import com.ray.project.commons.PrefUtils;
import com.ray.project.entity.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * 应用配置类：用于保存用户相关信息及设置
 * @author ray
 * @date 2018/06/22
 */
public class AppConfig {
    private final static String TAG = "AppConfig";

    private static AppConfig appConfig;
    private Context mContext;
    public final static String CONF_APP_UNIQUE_ID = BuildConfig.APPLICATION_ID + ".APP_UNIQUE_ID";
    private final static String APP_CONFIG = BuildConfig.APPLICATION_ID + ".config";
    /**
     * 缓存相关保存文件的名称
     */
    public static final String APP_CACHE_DIR_NAME = "/" + BuildConfig.APPLICATION_ID;
    /**
     * 错误日志保存路径
     */
    public static final String DIR_LOG = APP_CACHE_DIR_NAME + "/log";

    public static String HEAD;
    // 图标缓存
    public static String ICON_CACHE_DIR;
    // 上传图片缓存
    public static String CACHE_UPLOAD_IMAGES_DIR;
    // 下载文件存放
    public static String DOWNLOAD_DIR;
    // 音频文件
    public static String CACHE_VOICE_DIR;
    // 视频文件
    public static String CACHE_AUDIO_DIR;

    public static AppConfig getAppConfig(Context context) {
        if (appConfig == null) {
            appConfig = new AppConfig();
            appConfig.mContext = context;
            init(context);
        }
        return appConfig;
    }

    //本方法只在程序第一次启动时执行
    private static void init(Context context) {
        boolean isCreated = false;
        /*
        SharedPreferences sp = context.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        int code = sp.getInt("is_first_load", 0);
        Logger.i(TAG, code + "");
        /**/
        //*
        int code = PrefUtils.getFirstLoad(APP_CONFIG, Context.MODE_PRIVATE);
        Logger.i(TAG, code + "");
        /**/
        String state = Environment.getExternalStorageState();
        // 存在SD卡
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            HEAD = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_CACHE_DIR_NAME;
        } else {
            HEAD = context.getCacheDir().getAbsolutePath() + APP_CACHE_DIR_NAME;
        }
        ICON_CACHE_DIR = HEAD + "/icons";    // 缓存图片路径
        CACHE_UPLOAD_IMAGES_DIR = HEAD + "/cache_upload_images";    // 上传图片路径
        DOWNLOAD_DIR = HEAD + "/downloads";    // 下载文件路径
        CACHE_VOICE_DIR = HEAD + "/cache_voices";    // 视频缓存路径
        CACHE_AUDIO_DIR = HEAD + "/cache_audios";    // 音频缓存路径
        if (code == 0) {
//            doClearCache(sp);
            doClearCache();
            File head = new File(HEAD);
            if(!head.exists()){
                isCreated = head.mkdir();
                Logger.i(TAG, HEAD + " = " + isCreated);
            }
            File cache_upload_images = new File(CACHE_UPLOAD_IMAGES_DIR);
            if (!cache_upload_images.exists()) {
                isCreated = cache_upload_images.mkdirs();
                Logger.i(TAG, CACHE_UPLOAD_IMAGES_DIR + " = " + isCreated);
            }
            File download_dir = new File(DOWNLOAD_DIR);
            if (!download_dir.exists()) {
                isCreated = download_dir.mkdirs();
                Logger.i(TAG, DOWNLOAD_DIR + " = " + isCreated);
            }
            File icon_cache_dir = new File(ICON_CACHE_DIR);
            if (!icon_cache_dir.exists()) {
                isCreated = icon_cache_dir.mkdirs();
                Logger.i(TAG, ICON_CACHE_DIR + " = " + isCreated);
            }
            File voice_dir = new File(CACHE_VOICE_DIR);
            if (!voice_dir.exists()) {
                isCreated = voice_dir.mkdirs();
                Logger.i(TAG, CACHE_VOICE_DIR + " = " + isCreated);
            }
            File audio_dir = new File(CACHE_AUDIO_DIR);
            if (!audio_dir.exists()) {
                isCreated = audio_dir.mkdirs();
                Logger.i(TAG, CACHE_AUDIO_DIR + " = " + isCreated);
            }

            if(isCreated) {
//                sp.edit().putInt("is_first_load", 1).commit();
                PrefUtils.putFirstLoad(APP_CONFIG, Context.MODE_PRIVATE, 1);
            }
        }
    }

    private static void doClearCache(SharedPreferences sp) {
        File file = new File(HEAD);
        FileUtils.DeleteFile(file);
        sp.edit().putInt("is_first_load", 0).commit();
    }

    public static void doClearCache() {
        File file = new File(HEAD);
        FileUtils.DeleteFile(file);
        PrefUtils.putFirstLoad(APP_CONFIG, Context.MODE_PRIVATE, 0);
    }

    /**
     * 获取SD卡根目录
     * @return
     */
    public static String getSdCard() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);   // 判断sd卡是否存在
        if(sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();    // 获取跟目录
        }
        return sdDir == null ? null:sdDir.toString();
    }

    public Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator
                    + APP_CONFIG);
            props.load(fis);
        } catch (Exception e) {
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return props;
    }

    public String get(String key) {
        Properties props = get();
        return (props != null) ? props.getProperty(key) : null;
    }

    public void set(String key, String value) {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    private void setProps(Properties p) {
        FileOutputStream fos = null;
        try {
            // 把config建在files目录下
            // fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);

            // 把config建在(自定义)app_config的目录下
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            File conf = new File(dirConf, APP_CONFIG);
            fos = new FileOutputStream(conf);

            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    public static void doSaveUser(Context mContext, User user) {
        if (user != null) {
            SharedPreferences sp = mContext.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
//            edit.putString("username", user.getUsername() == null ? "" : user.getUsername());
//            edit.putString("uid", user.getId());
//            edit.putString("password", user.getPassword() == null ? "" : user.getPassword());
//            edit.putString("loginType", user.getIsThirdLogin());
//            edit.putString("photo_url", user.getPhotoUrl());
//            edit.putString("email", user.getEmail() == null ? "" : user.getEmail());
//            edit.putString("nickname", user.getNick_name() == null ? "" : user.getNick_name());
//            edit.putString("phone", user.getPhone() == null ? "" : user.getPhone());
//            edit.putString("user_portrait_uri", user.getUser_portrait_uri() == null ? "" : user.getUser_portrait_uri());
            edit.commit();
        }
    }

    public static User doGetUser(Context mContext) {
        User user = new User();
        SharedPreferences sp = mContext.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
//        user.setUsername(sp.getString("username", ""));
//        user.setId(sp.getString("uid", ""));
//        user.setPassword(sp.getString("password", ""));
//        user.setIsThirdLogin(sp.getString("password", "loginType"));
//        user.setPhotoUrl(sp.getString("photo_url", ""));
//        user.setEmail(sp.getString("email", ""));
//        user.setNick_name(sp.getString("nickname", ""));
//        user.setPhone(sp.getString("phone", ""));
//        user.setUser_portrait_uri(sp.getString("phone", "user_portrait_uri"));
        return user;
    }

    public static User getUser(Context mContext) {
        User user = new User();
        AssignValueForAttributeUtils.JsonToModel(PrefUtils.getUserInfo(), user);
        return PrefUtils.getUserInfo().equals("")?null:user;
    }

    public static void doDeleteUser(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
//        edit.remove("username");
//        edit.remove("uid");
//        edit.remove("password");
//        edit.remove("loginType");
//        edit.remove("photo_url");
//        edit.remove("email");
//        edit.remove("nickname");
//        edit.remove("loginType");
//        edit.remove("phone");
//        edit.remove("user_portrait_uri");
        edit.commit();
    }
}
