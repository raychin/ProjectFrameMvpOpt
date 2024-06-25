package com.ray.project.config;

import android.os.Parcelable;

import com.tencent.mmkv.MMKV;

import java.util.Collections;
import java.util.Set;

/**
 * MMKV管理类，替换SharePreferences方案
 * https://github.com/Tencent/MMKV
 */
public class MMKVManager {

    private static volatile MMKVManager instance;
    private static MMKV mv;

    private MMKVManager() {
        MMKV.initialize(ProjectApplication.get());
        mv = MMKV.defaultMMKV();
        // 如果不同业务需要区别存储，也可以单独创建自己的实例 mv = MMKV.mmkvWithID("MyID");
        // 默认是支持单进程的，如果业务需要多进程访问，那么在初始化的时候加上标志位 mv = MMKV.mmkvWithID("MyID", MMKV.MULTI_PROCESS_MODE);
        // MMKV 默认把文件存放在 $(FilesDir)/mmkv/ 目录。你可以在 App 启动时自定义根目录String rootDir = MMKV.initialize((getFilesDir().getAbsolutePath() + "/mmkv_2"));
        // MMKV 甚至支持自定义某个文件的目录String rootDir = MMKV.mmkvWithID("MyID", (getFilesDir().getAbsolutePath() + "/mmkv_3"));
    }
    /**
     * 单一实例
     */
    public static MMKVManager getInstance() {
        if (instance == null) {
            synchronized (MMKVManager.class) {
                if (instance == null) {
                    instance = new MMKVManager();
                }
            }
        }
        return instance;
    }

    /*** 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法 */
    public static void encode(String key, Object object) {
        if (object instanceof String) {
            mv.encode(key, (String) object);
        } else if (object instanceof Integer) {
            mv.encode(key, (Integer) object);
        } else if (object instanceof Boolean) {
            mv.encode(key, (Boolean) object);
        } else if (object instanceof Float) {
            mv.encode(key, (Float) object);
        } else if (object instanceof Long) {
            mv.encode(key, (Long) object);
        } else if (object instanceof Double) {
            mv.encode(key, (Double) object);
        } else if (object instanceof byte[]) {
            mv.encode(key, (byte[]) object);
        } else {
            mv.encode(key, object.toString());
        }
    }

    public static void encodeSet(String key, Set<String> sets) {
        mv.encode(key, sets);
    }

    public static void encodeParcelable(String key, Parcelable obj) {
        mv.encode(key, obj);
    }

    /**得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值*/
    public static Integer decodeInt(String key) {
        return mv.decodeInt(key, 0);
    }

    public static Double decodeDouble(String key) {
        return mv.decodeDouble(key, 0.00);
    }

    public static Long decodeLong(String key) {
        return mv.decodeLong(key, 0L);
    }

    public static Boolean decodeBoolean(String key) {
        return mv.decodeBool(key, false);
    }

    public static Float decodeFloat(String key) {
        return mv.decodeFloat(key, 0F);
    }

    public static byte[] decodeBytes(String key) {
        return mv.decodeBytes(key);
    }

    public static String decodeString(String key) {
        return mv.decodeString(key, "");
    }

    public static Set<String> decodeStringSet(String key) {
        return mv.decodeStringSet(key, Collections.<String>emptySet());
    }

    public static Parcelable decodeParcelable(String key) {
        return mv.decodeParcelable(key, null);
    }

    /*** 移除某个key对 ** @param key */
    public static void removeKey(String key) {
        mv.removeValueForKey(key);
    }

    /*** 清除所有key */
    public static void clearAll() {
        mv.clearAll();
    }

    public static long counts() {
        // 获取存储的数据总数
        return mv.count();
    }

    public static String[] keys() {
        // 获取所有存储的键
        return mv.allKeys();
    }

    public static boolean containsKey(String key) {
        return mv.containsKey(key);
    }

}
