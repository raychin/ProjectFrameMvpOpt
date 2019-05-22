package com.ray.project.network;

import java.io.Serializable;

/**
 * 服务器返回数据体
 * @author ray
 * @date 2018/07/03
 */
public class Result<T> implements Serializable {

    public String retMsg;
    public String retCode;
    public boolean success;
    public String errorCode;

    public T data;
}
