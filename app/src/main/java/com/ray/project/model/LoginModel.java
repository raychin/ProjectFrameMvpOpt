package com.ray.project.model;

/**
 * 服务器返回登录数据实体
 */
public class LoginModel extends BaseModel {

    /**
     * token : 47a89fcbd3574dd9a9905ef89838e147
     */

    private String token;
    public String accessToken;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String loginCode;
    public String loginMsg;
}
