package com.amarsoft.support.android.model;

/**
 * 基础请求对象
 * Created by Think on 2017/10/25.
 */

public class BaseRequest {
    private String method;
    private String token;
    private Object businessRequestObject;

    public BaseRequest() {
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public Object getBusinessRequestObject() {
        return businessRequestObject;
    }

    public void setBusinessRequestObject(Object businessRequestObject) {
        this.businessRequestObject = businessRequestObject;
    }
}
