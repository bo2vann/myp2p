package com.amarsoft.support.android.model;

/**
 * 基础接收对象
 * Created by Think on 2017/10/25.
 */

public class BaseResponse {

    private String countPage;
    private String curPage;
    private String resultCode;
    private String resultMsg;
    private String uuid;

    private Object businessResponseObject;

    public String getCountPage() {
        return countPage;
    }

    public void setCountPage(String countPage) {
        this.countPage = countPage;
    }

    public String getCurPage() {
        return curPage;
    }

    public void setCurPage(String curPage) {
        this.curPage = curPage;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Object getBusinessResponseObject() {
        return businessResponseObject;
    }

    public void setBusinessResponseObject(Object businessResponseObject) {
        this.businessResponseObject = businessResponseObject;
    }
}
