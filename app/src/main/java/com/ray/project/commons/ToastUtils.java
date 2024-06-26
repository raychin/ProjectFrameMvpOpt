package com.ray.project.commons;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ray.project.R;

/**
 * tips类
 * @author ray
 * @date 2018/06/22
 */
public class ToastUtils {

    private static String oldMsg;
    private static long time;

    /**
     * 显示系统样式tips消息
     * @param context
     * @param msg
     * @param duration
     */
    public static void showToast(Context context, String msg, int duration) {
        if (!msg.equals(oldMsg)) { // 当显示的内容不一样时，即断定为不是同一个Toast
            Toast.makeText(context, msg, duration).show();
            time = System.currentTimeMillis();
        } else {
            // 显示内容一样时，只有间隔时间大于2秒时才显示
            if (System.currentTimeMillis() - time > 2000) {
                Toast.makeText(context, msg, duration).show();
                time = System.currentTimeMillis();
            }
        }
        oldMsg = msg;
    }

    /**
     * 显示自定义样式tips消息
     * @param context
     * @param msg
     * @param duration
     */
    public static void showCustomToast(Context context, String msg, int duration) {
        // 当显示的内容不一样时，即断定为不是同一个Toast
        if (!msg.equals(oldMsg)) {
            makeCustomToast(context, msg, duration).show();
            time = System.currentTimeMillis();
        } else {
            // 显示内容一样时，只有间隔时间大于2秒时才显示
            if (System.currentTimeMillis() - time > 2000) {
                makeCustomToast(context, msg, duration).show();
                time = System.currentTimeMillis();
            }
        }
        oldMsg = msg;
    }

    /**
     * 创建自定义样式tips消息
     * @param context
     * @param msg
     * @param duration
     * @return
     */
    private static Toast makeCustomToast(Context context, String msg, int duration) {
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.toast, null);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 800);
        toast.setView(toastRoot);
        TextView tv = toastRoot.findViewById(R.id.TextViewInfo);
        tv.setText(msg);
        toast.setDuration(duration);
        return toast;
    }
}
