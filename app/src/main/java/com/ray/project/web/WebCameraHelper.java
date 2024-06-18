package com.ray.project.web;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;

import com.ray.project.commons.Logger;
import com.ray.project.config.AppConfig;
import com.ray.project.config.ProjectApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class WebCameraHelper {

    private File mTempFile;

    private static class SingletonHolder {
        static final WebCameraHelper INSTANCE = new WebCameraHelper();
    }

    public static WebCameraHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 图片选择回调
     */
    public ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadCallbackAboveL;

    public Uri fileUri;
    public static final int TYPE_REQUEST_PERMISSION = 3;
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_GALLERY = 2;

    /**
     * 包含拍照和相册选择
     */
    public void showOptions(final Activity act) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(act);
        alertDialog.setOnCancelListener(new ReOnCancelListener());
        alertDialog.setTitle("选择");
        alertDialog.setItems(new CharSequence[]{"拍照", "从手机相册中选择"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if (ContextCompat.checkSelfPermission(act,
                                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                // 申请WRITE_EXTERNAL_STORAGE权限，这里转载的兄弟只写了相机权限，部分手机无法使用照相机上传，需要加存储权限
                                ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, TYPE_REQUEST_PERMISSION);
                            } else {
                                toCamera(act);
                            }
                        } else {
                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
                            act.startActivityForResult(i,
                                    TYPE_GALLERY);
                        }
                    }
                });
        alertDialog.show();
    }

    /**
     * 点击取消的回调
     */
    private class ReOnCancelListener implements
            DialogInterface.OnCancelListener {

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
            }
            if (mUploadCallbackAboveL != null) {
                mUploadCallbackAboveL.onReceiveValue(null);
                mUploadCallbackAboveL = null;
            }
        }
    }

    /**
     * 请求拍照
     *
     * @param act
     */
    public void toCamera(Activity act) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用android的相机
        // 创建一个文件保存图片
        File dirs = FileUtils.createDirs(AppConfig.CACHE_UPLOAD_IMAGES_DIR);
        mTempFile = FileUtils.createTempImage(dirs);

        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(ProjectApplication.get(),
                    ProjectApplication.get().getPackageName() + ".fileprovider",
                    mTempFile);
        } else {
            fileUri = Uri.fromFile(mTempFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        act.startActivityForResult(intent, TYPE_CAMERA);

    }

    /**
     * startActivityForResult之后要做的处理
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        switch (requestCode) {
            case TYPE_CAMERA: // 拍照
                handleCamera(resultCode, intent);
                break;
            case TYPE_GALLERY:// 相册选择
                handleGallery(resultCode, intent);
                break;
        }

    }


    /**
     * 处理拍照
     *
     * @param resultCode
     * @param intent
     */
    private void handleCamera(int resultCode, Intent intent) {
        if (mUploadCallbackAboveL != null) { //Android > 5.0 以上的处理方法
            Uri[] uris ;
            if (resultCode == -1) {//RESULT_OK = -1，拍照成功
                uris = new Uri[]{fileUri};
            } else { //拍照不成功，或者什么也不做就返回了，以下的处理非常有必要，不然web页面不会有任何响应
//                uris = WebChromeClient.FileChooserParams.parseResult(resultCode, intent);
                mUploadCallbackAboveL.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                mUploadCallbackAboveL = null;
                return;
            }

//            LogUtils.i("压缩前的URI :" + uris[0].toString());

//            try {
                compressImage(mTempFile, new OnCompressImage() {

                    @Override
                    public void onCompressImageSuccess(List<Uri> uri) {
                        Uri[] uris1 = uri.toArray(new Uri[uri.size()]);
                        mUploadCallbackAboveL.onReceiveValue(uris1);
                        mUploadCallbackAboveL = null;
                    }

                    @Override
                    public void onCompressImageFailure(String errorString) {
                        mUploadCallbackAboveL.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                        mUploadCallbackAboveL = null;
                    }
                });
//            } catch (Exception e) {
//                e.printStackTrace();
//                mUploadCallbackAboveL.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
//                mUploadCallbackAboveL = null;
//            }


        }


        if (mUploadMessage != null) { //Android 低版本处理方法
            mUploadMessage.onReceiveValue(fileUri);
            mUploadMessage = null;
        }
    }

    /**
     * 处理相册
     *
     * @param resultCode
     * @param intent
     */
    private void handleGallery(int resultCode, Intent intent) {
        if (mUploadCallbackAboveL != null) {//Android > 5.0 以上的处理方法

            if (intent == null) {
                mUploadCallbackAboveL.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                mUploadCallbackAboveL = null;
                return;
            }
            List<Uri> uriList = Arrays.asList(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));

            Logger.i("handleGallery", "压缩前的URI :" + uriList.get(0).toString());

            compressImage(uriList, new OnCompressImage() {

                @Override
                public void onCompressImageSuccess(List<Uri> uri) {
                    Uri[] uris1 = uri.toArray(new Uri[uri.size()]);
                    mUploadCallbackAboveL.onReceiveValue(uris1);
                    mUploadCallbackAboveL = null;
                }

                @Override
                public void onCompressImageFailure(String errorString) {
                    mUploadCallbackAboveL.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                    mUploadCallbackAboveL = null;
                }
            });

        }

        if (mUploadMessage != null) {//Android 低版本处理方法
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }


    /**
     * 压缩图片
     */
    public void compressImage(List<Uri> uriList, OnCompressImage onCompressImage) {
        List<Uri> temps = new ArrayList<>();
        List<String> paths = new ArrayList<>();

        for (Uri uri : uriList) {
            paths.add(FileUtils.getPath(uri));
        }

        Luban.with(ProjectApplication.get())
                .load(paths)
                .ignoreBy(100)
                .filter(path -> {
                    if (TextUtils.isEmpty(path)) {
                        return false;
                    }
                    if (path.toLowerCase().endsWith(".jpg")
                            || path.toLowerCase().endsWith(".jpeg")
                            || path.toLowerCase().endsWith(".png")
                            || path.toLowerCase().endsWith(".gif")
                            || path.toLowerCase().endsWith(".bmp")
                    ) {
                        //只压缩图片格式的文件
                        return true;
                    }
                    {
                        return false;
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        Logger.i("setCompressListener", "文件名称 :" + file.getName() + ";文件大小 :" + FileUtils.getFileSize(file));
                        Uri fileUri;//file转uri
                        if (Build.VERSION.SDK_INT >= 24) {
                            fileUri = FileProvider.getUriForFile(ProjectApplication.get(),
                                    ProjectApplication.get().getPackageName() + ".fileprovider",
                                    file);
                        } else {
                            fileUri = Uri.fromFile(file);
                        }

                        Logger.i("setCompressListener", "压缩后的URI :" + fileUri.toString());

                        temps.add(fileUri);
                        if (temps.size() == uriList.size()) {
                            if (onCompressImage != null) {
                                onCompressImage.onCompressImageSuccess(temps);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        Logger.i("setCompressListener", "onError" + e.getMessage());
                        if (onCompressImage != null) {
                            onCompressImage.onCompressImageFailure(e.getMessage());
                        }
                    }
                }).launch();
    }

    public void compressImage(File file, OnCompressImage onCompressImage) {
        List<Uri> temps = new ArrayList<>();

        Luban.with(ProjectApplication.get())
                .load(file)
                .ignoreBy(100)
                .filter(path -> {
                    if (TextUtils.isEmpty(path)) {
                        return false;
                    }
                    if (path.toLowerCase().endsWith(".jpg")
                            || path.toLowerCase().endsWith(".jpeg")
                            || path.toLowerCase().endsWith(".png")
                            || path.toLowerCase().endsWith(".gif")
                            || path.toLowerCase().endsWith(".bmp")
                    ) {
                        //只压缩图片格式的文件
                        return true;
                    }
                    {
                        return false;
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件

                        Logger.i("setCompressListener", "文件名称 :" + file.getName() + ";文件大小 :" + FileUtils.getFileSize(file));
                        Uri fileUri;//file转uri
                        if (Build.VERSION.SDK_INT >= 24) {
                            fileUri = FileProvider.getUriForFile(ProjectApplication.get(),
                                    ProjectApplication.get().getPackageName() + ".fileprovider",
                                    file);
                        } else {
                            fileUri = Uri.fromFile(file);
                        }

                        Logger.i("setCompressListener", "压缩后的URI :" + fileUri.toString());

                        temps.add(fileUri);
//                        if (temps.size() == uriList.size()) {
                            if (onCompressImage != null) {
                                onCompressImage.onCompressImageSuccess(temps);
                            }
//                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        Logger.i("setCompressListener", "onError" + e.getMessage());
                        if (onCompressImage != null) {
                            onCompressImage.onCompressImageFailure(e.getMessage());
                        }
                    }
                }).launch();
    }


    public interface OnCompressImage {

        void onCompressImageSuccess(List<Uri> uri);

        void onCompressImageFailure(String errorString);
    }

}

