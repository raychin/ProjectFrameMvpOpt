package com.ray.project.base;

/**
 * 界面业务处理传递消息体
 * @auhtor ray
 * @date 2018/06/24
 */
public class ResultEvent {
    public int code;
    public Object obj;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
