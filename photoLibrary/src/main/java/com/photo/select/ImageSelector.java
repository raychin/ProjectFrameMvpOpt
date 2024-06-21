package com.photo.select;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.photo.select.utils.Utils;

public class ImageSelector {


    public static final int IMAGE_REQUEST_CODE = 1002;
    public static final int IMAGE_CROP_CODE = 1003;

    public static void open(Activity activity, ImageConfig config) {
        if (config == null) {
            return;
        }

        if (config.getImageLoader() == null) {
            Toast.makeText(activity, R.string.open_camera_fail, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Utils.existSDCard()) {
            Toast.makeText(activity, R.string.empty_sdcard, Toast.LENGTH_SHORT).show();
            return;
        }


        Intent intent = new Intent(activity, ImageSelectorActivity.class);
        intent.putExtra("config",config);
        activity.startActivityForResult(intent, config.getRequestCode());
    }

    public static void open(Fragment fragment, ImageConfig config) {
        if (config == null) {
            return;
        }

        if (config.getImageLoader() == null) {
            Toast.makeText(fragment.getActivity(), R.string.open_camera_fail, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Utils.existSDCard()) {
            Toast.makeText(fragment.getActivity(), R.string.empty_sdcard, Toast.LENGTH_SHORT).show();
            return;
        }


        Intent intent = new Intent(fragment.getActivity(), ImageSelectorActivity.class);
        intent.putExtra("config",config);
        fragment.startActivityForResult(intent, config.getRequestCode());
    }

}
