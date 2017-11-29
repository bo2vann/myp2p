package com.think.android.p2p.ui.account;

import android.content.Context;

import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 查询用户基本信息
 * Created by Think on 2017/11/4.
 */

public class UserBaseInfoHandler extends CommonRemoteHandler {

    boolean needLoadingDialog;

    public UserBaseInfoHandler(Context context, boolean needLoadingDialog) {
        super(context);
        this.needLoadingDialog = needLoadingDialog;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        UserInfoUtils userInfoUtils = new UserInfoUtils(context);
        try {
            request.put("userName", userInfoUtils.get("UserName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "UserBasicInfo";
    }


    @Override
    protected boolean useOverLayout() {
        return needLoadingDialog;
    }
}
