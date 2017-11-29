package com.think.android.p2p.ui.property;

import android.content.Context;

import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 查询用户投资列表
 * Created by Think on 2017/11/12.
 */

public class UserProjectListHandler extends CommonRemoteHandler {

    public static final String UNDUE_PROFIT = "TZCC";
    public static final String UNDUE_PROPERTY = "TZCC";
    public static final String DUED_PROPERTY = "HKWC";

    boolean needLoadingDialog;

    String status;
    int curPage;
    int maxLine;

    public UserProjectListHandler(Context context, boolean needLoadingDialog, String status, int curPage, int maxLine) {
        super(context);
        this.needLoadingDialog = needLoadingDialog;
        this.status = status;
        this.curPage = curPage;
        this.maxLine = maxLine;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        UserInfoUtils userInfoUtils = new UserInfoUtils(context);
        try {
            request.put("userName", userInfoUtils.getUserName());
            request.put("status", status);
            request.put("curPage", curPage);
            request.put("maxLine", maxLine);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "UserProjectList";
    }

    @Override
    protected boolean useOverLayout() {
        return needLoadingDialog;
    }
}
