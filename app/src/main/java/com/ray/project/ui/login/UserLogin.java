package com.ray.project.ui.login;

import com.ray.project.entity.BaseBean;

/**
 * @Description: 登录用户实体
 * @Author: ray
 * @Date: 20/6/2024
 */
public class UserLogin extends BaseBean {
    public String userid;
    public String password;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
