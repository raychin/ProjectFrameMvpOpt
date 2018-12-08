package com.ray.project.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ray.project.R;
import com.ray.project.commons.Logger;
import com.ray.project.commons.RelayoutTool;
import com.ray.project.commons.ToastUtils;
import com.ray.project.config.AppConfig;
import com.ray.project.config.AppManager;
import com.ray.project.config.ProjectApplication;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * activity抽象类
 * @author ray
 * @date 2018/06/22
 */
public abstract class BaseActivity<P extends BasePresenter>
        extends AppCompatActivity implements IBaseView {

    protected P presenter;

    public ProjectApplication appContext;
    protected FragmentManager mFragmentManager;

    /**
     * 获取TAG的activity名称
     */
    protected final String TAG = this.getClass().getSimpleName();

    /**
     * 是否透明化状态栏
     * @return 默认为不透明
     */
    protected boolean isTransparentStatus() { return false; }

    /**
     * 沉浸式菜单
     * @return 默认为不是
     */
    protected boolean isImmersiveStatus() { return false; }

    /**
     * 是否填充状态栏高度
     * @return
     */
    protected boolean isImmersiveStatusHeight() { return false; }

    /**
     * 界面是否全屏
     * @return 默认不全屏
     */
    protected boolean isFullScreen() { return false; }

    /**
     * 是否使用状态栏深色主题
     * @return 默认非深色主题
     */
    protected boolean isConvertStatusBarColor() { return false; }

    /**
     * 设置标题栏背景色
     * @param resId
     */
    protected void setTitleBarBackground(int resId) {
        RelativeLayout titleRl = findViewById(R.id.titleRl);
        if(titleRl != null) {
            titleRl.setBackgroundResource(resId);
        }
    }

    public void setTitleText(String text) {
        TextView title = findViewById(R.id.title);
        if(title != null) {
            title.setText(text);
        }
    }

    /**
     * 是否开启MVP模式
     * @return
     */
    protected boolean isMvp() { return false; }

    /**
     * 沉浸式添加顶部占位颜色
     */
    public int statusColor() { return 0; }

    private Unbinder bind;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置全屏
        if(isFullScreen()) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);    // 全屏，去掉状态栏
        }
        // 透明化状态栏
        if(isTransparentStatus()) { setTransparentStatus(); }
        // 沉浸式
        if(isImmersiveStatus()) { setImmersiveStatus(); }
        if(isConvertStatusBarColor()) { setStatusBar(); }

        AppManager.getAppManager().addActivity(this);
        mFragmentManager = getSupportFragmentManager();
        appContext = ProjectApplication.get();
        // 设置布局
        setContentView(initLayout());
        bind = ButterKnife.bind(this);
        if(isImmersiveStatus()) { setStatusViewWithColor(statusColor()); }
        // 初始化控件
        initView();
        // 初始化present view
        if(isMvp()) {
            initPresent();
            if(presenter != null) { presenter.onCreate(); }
//            if(presenter != null) { presenter.setView(this); }
        }
        // 设置数据
        initData();
    }

    /******** 基准分辨率 **********/
    private static final float UI_STANDARD_width = 1080;
    /******** 缩放比例值 **********/
    protected static float scale = 0;
    @Override
    public void setContentView(int layoutResID) {
        View view = View.inflate(this, layoutResID, null);
        this.setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        if (scale == 0) {
            initScreenScale();
        }

        if (scale != 1) {
            RelayoutTool.relayoutViewHierarchy(view, scale);
        }
        super.setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (scale == 0) {
            initScreenScale();
        }
        RelayoutTool.relayoutViewHierarchy(view, scale);
        RelayoutTool.scaleLayoutParams(params, scale);
        super.setContentView(view, params);
    }

    public void initScreenScale() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float width = displayMetrics.widthPixels;
        scale = width / UI_STANDARD_width;
    }

    /**
     * 通过反射实例化present
     */
    private void initPresent(){
        try {
            // 获取当前new对象的泛型的父类类型
            ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
            Class clazz = (Class<P>) parameterizedType.getActualTypeArguments()[0];
            Logger.d(TAG, "clazz ==>> " + clazz);
            //this.presenter = (P) clazz.newInstance();
            // 获取有参构造
            Constructor<?>[] cons = clazz.getConstructors();
            Constructor<?> con = cons[0];
            Object initArgs = this;
            this.presenter = (P) con.newInstance(initArgs, initArgs);
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setImmersiveStatus() {
        Window win = getWindow();
        win.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        win.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION     // 占用底部导航栏
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // 只对api21以上版本有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            win.setStatusBarColor(Color.TRANSPARENT);    // 透明状态栏
            //win.setNavigationBarColor(Color.TRANSPARENT);    // 透明底部导航栏
        }
    }

    private void setTransparentStatus() {
        // 只对api21以上版本有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window win = getWindow();
            win.setStatusBarColor(Color.TRANSPARENT);    // 透明状态栏
            //win.setNavigationBarColor(Color.TRANSPARENT);    // 透明底部导航栏
        }
    }

    // 是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected boolean useThemeStatusBarColor = false;
    // 是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
    protected boolean useStatusBarColor = true;
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            // 根据上面设置是否对状态栏单独设置颜色
            if (useThemeStatusBarColor) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.text_black));
            } else {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // android6.0以后可以对状态栏文字颜色和图标进行修改
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 设置布局
     *
     * @return
     */
    public abstract int initLayout();

    /**
     * 初始化布局
     */
    public abstract void initView();

    /**
     * 设置数据
     */
    public abstract void initData();

    @Override
    public void updateView(ResultEvent event) {

    }

    /**
     * 添加状态栏占位视图
     */
    private void setStatusViewWithColor(int color) {
        View statusBarView = findViewById(R.id.status);
        if(statusBarView == null) { return; }
        ViewGroup.LayoutParams lp = statusBarView.getLayoutParams();
        lp.height = isImmersiveStatusHeight() ? ProjectApplication.get().getStatusBarHeight() : 0;
        if(color != 0) { statusBarView.setBackgroundColor(color); };
        statusBarView.setLayoutParams(lp);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(presenter != null) { presenter.onStart(); }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(presenter != null) { presenter.onResume(); }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(presenter != null) { presenter.onPause(); }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(presenter != null) { presenter.onStop(); }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(presenter != null) { presenter.onDestroy(); }
        bind.unbind();
        AppManager.getAppManager().finishActivity(this);
    }

    public void nextActivity(Class<?> clazz, boolean isPlayAnim, String name, Serializable s) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        if (null != name && !name.trim().equals("")) {
            intent.putExtra(name, s);
        }
        startActivity(intent);
        if (isPlayAnim) {
            overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
        }
    }

    public void nextActivityByPackageName(String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        startActivity(intent);
    }

    public void nextActivity(Class<?> clazz){
        nextActivity(clazz, true);
    }

    public void nextActivity(Class<?> clazz, boolean isPlayAnim){
        nextActivity(clazz, isPlayAnim, null, null);
    }

    public void nextActivity(Class<?> clazz, String name, Serializable s) {
        nextActivity(clazz, true, name, s);
    }

    @Override
    public void finish() {
        super.finish();
        if (true) {
            overridePendingTransition(0, R.anim.push_right_out);
        }
    }

    /**
     * 单个授权申请
     * @param permission
     */
    public void checkPermission(String permission) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // 检查是否已经给了权限
            int checkPermission= ContextCompat.checkSelfPermission(getApplicationContext(),
                    permission);
            // 没有给权限
            if(checkPermission != PackageManager.PERMISSION_GRANTED){
                // 参数分别是当前活动，权限字符串数组，requestCode
                ActivityCompat.requestPermissions(this, new String[]{ permission }, 1);
            }
        }
    }

    /**
     * 多个授权申请
     * @param permissions
     */
    public void checkPermissions(String[] permissions) {
        for(int i = 0; i < permissions.length; i ++) {
            checkPermission(permissions[i]);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(grantResults.length == 0) { return; }
                // grantResults数组与权限字符串数组对应，里面存放权限申请结果
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    for(int i = 0; i < permissions.length; i ++) {
                        // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                        boolean b = shouldShowRequestPermissionRationale(permissions[i]);
                        if (!b) {
                            // 用户还是想用我的 APP 的
                            // 提示用户去应用设置界面手动开启权限
                            ToastUtils.showToast(this, "权限获取失败", 1);
                        } else {
                            finish();
                        }
                    }
                } else {
                    ToastUtils.showToast(this, "权限获取成功", 1);
                    AppConfig.getAppConfig(this);
                }
            }
        }
    }
}
