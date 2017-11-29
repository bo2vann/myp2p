package com.think.android.p2p.ui.property.transrecord;

import android.content.Context;

import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户交易记录查询
 * Created by Think on 2017/11/12.
 */

public class TransListHandler extends CommonRemoteHandler {

    boolean needLoadingDialog;

    String timeType;
    String tranType;
    int curPage;
    int maxLine;

    public TransListHandler(Context context, boolean needLoadingDialog, String timeType, String tranType, int curPage, int maxLine) {
        super(context);
        this.needLoadingDialog = needLoadingDialog;
        this.timeType = timeType;
        this.tranType = tranType;
        this.curPage = curPage;
        this.maxLine = maxLine;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        UserInfoUtils userInfoUtils = new UserInfoUtils(context);
        try {
            request.put("userName", userInfoUtils.getUserName());
            request.put("timeType", timeType);
            request.put("tranType", tranType);
            request.put("curPage", curPage);
            request.put("maxLine", maxLine);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "TrasactionList";
    }

    @Override
    protected boolean useOverLayout() {
        return needLoadingDialog;
    }
}
