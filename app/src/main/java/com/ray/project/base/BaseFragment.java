package com.ray.project.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ray.project.R;
import com.ray.project.commons.Logger;
import com.ray.project.commons.RelayoutTool;
import com.ray.project.config.ProjectApplication;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * fragment抽象类
 * @author ray
 * @date 2018/06/25
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements IBaseView {

    protected P presenter;

    /**
     * 获取TAG的activity名称
     */
    protected final String TAG = this.getClass().getSimpleName();

    protected BaseActivity mActivity;

    private View mContentView;

    /**
     * 是否开启MVP模式
     * @return
     */
    protected boolean isMvp() { return false; }

    /**
     * 是否填充状态栏高度
     * @return
     */
    protected boolean isImmersiveStatusHeight() { return false; }

    /**
     * 设置布局
     *
     * @return
     */
    protected abstract int initLayout();

    /**
     * 初始化布局
     */
    protected abstract void initView(View view);

    /**
     * 沉浸式添加顶部占位颜色
     */
    public int statusColor() { return 0; };

    /**
     * 设置数据
     */
    protected abstract void initData(Bundle savedInstanceState);

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Unbinder bind;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(initLayout(), container, false);

        if (mActivity.scale == 0) {
            mActivity.initScreenScale();
        }
        if (mActivity.scale != 1) {
            RelayoutTool.relayoutViewHierarchy(mContentView, mActivity.scale);
        }

        bind = ButterKnife.bind(this, mContentView);
        setStatusViewWithColor(statusColor());
        return mContentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 初始化present view
        if(isMvp()) {
            initPresent();
//            if(presenter != null) { presenter.setView(this); }
        }
        initData(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bind.unbind();
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
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateView(ResultEvent event) {

    }

    /**
     * 添加状态栏占位视图
     */
    private void setStatusViewWithColor(int color) {
        View statusBarView = mContentView.findViewById(R.id.status);
        if(statusBarView == null) { return; }
        ViewGroup.LayoutParams lp = statusBarView.getLayoutParams();
        lp.height = isImmersiveStatusHeight() ? ProjectApplication.get().getStatusBarHeight() : 0;
        if(color != 0) { statusBarView.setBackgroundColor(color); };
        statusBarView.setLayoutParams(lp);
    }

    // 登记式单例实现单例模式的继承
    private static Map<String, BaseFragment> registryMap = new HashMap<String, BaseFragment>();

    /** 限定一个抽象类的所有子类都必须是单例
    public BaseFragment() throws SingletonException{
        String clazzName = this.getClass().getName();
        if (registryMap.containsKey(clazzName)){
            throw new SingletonException("Cannot construct instance for class " + clazzName + ", since an instance already exists!");
        } else {
            synchronized(registryMap){
                if (registryMap.containsKey(clazzName)){
                    throw new SingletonException("Cannot construct instance for class " + clazzName + ", since an instance already exists!");
                } else {
                    registryMap.put(clazzName, this);
                }
            }
        }
    }

    protected static class SingletonException extends Exception {
        private static final long serialVersionUID = -8633183690442262445L;

        private SingletonException(String message){
            super(message);
        }
    }
    /**/

    @SuppressWarnings("unchecked")
    public static <T extends BaseFragment> T newInstance(final Class<T> clazz) {
        String clazzName = clazz.getName();
        if(!registryMap.containsKey(clazzName)){
            synchronized(registryMap){
                if(!registryMap.containsKey(clazzName)){
                    T instance = null;
                    try {
                        instance = clazz.newInstance();
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return instance;
                }
            }
        }
        return (T) registryMap.get(clazzName);
    }

    public static BaseFragment newInstance(final String clazzName) {
        if(!registryMap.containsKey(clazzName)){
            try {
                Class<? extends BaseFragment> clazz = Class.forName(clazzName).asSubclass(BaseFragment.class);
                synchronized(registryMap){
                    if(!registryMap.containsKey(clazzName)){
                        BaseFragment instance = clazz.newInstance();
                        return instance;
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }
        }
        return registryMap.get(clazzName);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseFragment> T newInstance(final Class<T> clazz, Class<?>[] parameterTypes, Object[] initArgs) {
        String clazzName = clazz.getName();
        if(!registryMap.containsKey(clazzName)){
            synchronized(registryMap){
                if(!registryMap.containsKey(clazzName)){
                    Constructor<T> constructor = null;
                    try {
                        constructor = clazz.getConstructor(parameterTypes);
                        T instance = constructor.newInstance(initArgs);
                        return instance;
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return (T) registryMap.get(clazzName);
    }
}
