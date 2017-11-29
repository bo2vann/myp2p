package com.think.android.p2p.ui.home;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONObject;

/**
 * 首页请求投资列表
 * Created by Think on 2017/11/11.
 */

public class HomeProductListHandler extends CommonRemoteHandler {

    boolean needLoadingDialog;

    public HomeProductListHandler(Context context, boolean needLoadingDialog) {
        super(context);
        this.needLoadingDialog = needLoadingDialog;
    }

    @Override
    protected JSONObject createRequestData() {
        return new JSONObject();
    }

    @Override
    protected String getMethod() {
        return "HomeProject";
    }

    @Override
    protected boolean useOverLayout() {
        return needLoadingDialog;
    }
}
