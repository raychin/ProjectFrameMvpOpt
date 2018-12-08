package com.ray.project.ui.login;

/**
 * 登录接口
 * @author ray
 * @date 2018/06/28
 */
public interface ILoginPresenter {
    void clear();
    void doLogin(String name, String password);
}
