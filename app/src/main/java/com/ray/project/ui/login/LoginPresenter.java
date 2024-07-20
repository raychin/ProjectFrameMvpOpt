package com.ray.project.ui.login;

import android.widget.Toast;
import java.util.Base64;

import com.ray.project.base.BaseActivity;
import com.ray.project.base.ResultEvent;
import com.ray.project.base.BasePresenter;
import com.ray.project.base.IBaseView;
import com.ray.project.commons.MD5;
import com.ray.project.commons.ToastUtils;
import com.ray.project.model.LoginModel;
import com.ray.project.network.AndroidScheduler;
import com.ray.project.network.Result;
import com.ray.project.network.Net;
import com.ray.project.network.RxApiManager;
import com.ray.project.network.RxObserver;

import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * 登录接口实现
 * @author ray
 * @date 2018/06/28
 */
public class LoginPresenter extends BasePresenter implements ILoginPresenter {


    public LoginPresenter(IBaseView view, BaseActivity activity) {
        super(view, activity);
    }

    @Override
    public void clear() {


        Subscription subscription = Net.getService().getToken1("1", "2")
                // 指定网络请求在io后台线程中进行
                .subscribeOn(Schedulers.io())
                // 指定observer回调在UI主线程中进行
                // 可使用RxAndroid中的AndroidSchedulers.mainThread()，需要添加RxAndroid依赖
                .observeOn(AndroidScheduler.mainThread())
                .subscribe(new RxObserver(getActivity(), new RxObserver.RxResult<Result<LoginModel>>() {
                    @Override
                    public void onResult(Result<LoginModel> data) {
                        LoginModel loginModel = data.data;
                        if (!loginModel.loginCode.equals("00")) {
                            ToastUtils.showToast(getActivity(), loginModel.loginMsg, Toast.LENGTH_SHORT);
                            return;
                        }
                        ResultEvent event = new ResultEvent();
                        event.setCode(0);
                        event.setObj(data.data);
                        getView().updateView(event);
                    }
                }));
        subscriptions.put("getToken1", subscription);
        RxApiManager.get().add("getToken1", subscription);
    }

    @Override
    public void doLogin(String name, String password) {
//        String time = String.valueOf(System.currentTimeMillis());
//        Subscription subscription = Net.getService().getToken(
//                name,
//                MD5.hashKeyForDisk(time + password),
//                    time
//                )
//                // 指定网络请求在io后台线程中进行
//                .subscribeOn(Schedulers.io())
//                // 指定observer回调在UI主线程中进行
//                // 可使用RxAndroid中的AndroidSchedulers.mainThread()，需要添加RxAndroid依赖
//                .observeOn(AndroidScheduler.mainThread())
//                .subscribe(new RxObserver(getActivity(), new RxObserver.RxResult<Result<LoginModel>>() {
//                    @Override
//                    public void onResult(Result<LoginModel> data) {
//                        Logger.e(TAG, data.toString());
//
//                        ResultEvent event = new ResultEvent();
//                        event.setCode(0);
//                        event.setObj(data.data);
//                        getView().updateView(event);
//                    }
//                }));
//        subscriptions.put("login", subscription);
//        RxApiManager.get().add("login", subscription);

        UserLogin userLogin = new UserLogin();
        userLogin.setUserid(name);
        Base64.Encoder encoder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            encoder = Base64.getEncoder();
            byte[] data = encoder.encode((name + password).getBytes());
            userLogin.setPassword(MD5.hashKeyForDisk(new String(data)));
        }

//        Subscription subscription = Net.getService().login(userLogin)
        Subscription subscription = Net.getService().login(name, "a2a2aae13e539b23f674cb9023db1cc5")
                // 指定网络请求在io后台线程中进行
                .subscribeOn(Schedulers.io())
                // 指定observer回调在UI主线程中进行
                // 可使用RxAndroid中的AndroidSchedulers.mainThread()，需要添加RxAndroid依赖
                .observeOn(AndroidScheduler.mainThread())
                .subscribe(new RxObserver(getActivity(), new RxObserver.RxResult<Result<LoginModel>>() {
                    @Override
                    public void onResult(Result<LoginModel> data) {
                        LoginModel loginModel = data.data;
                        if (!loginModel.loginCode.equals("00")) {
                            ToastUtils.showToast(getActivity(), loginModel.loginMsg, Toast.LENGTH_SHORT);
                            return;
                        }
                        ResultEvent event = new ResultEvent();
                        event.setCode(0);
                        event.setObj(data.data);
                        getView().updateView(event);
                    }
                }));
        subscriptions.put("login", subscription);
        RxApiManager.get().add("login", subscription);
    }

    @Override
    public void onStop() {
        cancelAll();
    }
}
