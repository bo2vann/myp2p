package com.think.android.p2p.ui.invest;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 请求产品列表
 * Created by Think on 2017/10/25.
 */

public class ProductListHandler extends CommonRemoteHandler {

    private boolean needLoadingDialog;
    int curPage;
    int maxLine;

    public ProductListHandler(Context context, boolean needLoadingDialog, int curPage, int maxLine) {
        super(context);
        this.needLoadingDialog = needLoadingDialog;
        this.curPage = curPage;
        this.maxLine = maxLine;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("curPage", curPage);
            request.put("maxLine", maxLine);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "InvestCountMsg";
    }

    @Override
    protected boolean useOverLayout() {
        return needLoadingDialog;
    }
}
