package com.ray.project.ui.login;

import com.ray.project.base.BaseActivity;
import com.ray.project.base.ResultEvent;
import com.ray.project.base.BasePresenter;
import com.ray.project.base.IBaseView;
import com.ray.project.commons.Logger;
import com.ray.project.network.Result;
import com.ray.project.network.Net;
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

    }

    @Override
    public void doLogin(String name, String password) {

        Subscription subscription = Net.getService().text("ae240f7fba620fc370b803566654949e")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new RxObserver(getActivity(), new RxObserver.RxResult<Result>() {
                    @Override
                    public void onResult(Result data) {
                        Logger.e(TAG, data.toString());

                        ResultEvent event = new ResultEvent();
                        event.setCode(0);
                        event.setObj("-----------------test = " + data.toString());
                        getView().updateView(event);
                    }
                }));
        subscriptions.put("login", subscription);
    }

    @Override
    public void onStop() {
        System.out.println("执行了这里。。。");
        cancelAll();
        System.out.println("执行了这里2。。。");
    }
}
