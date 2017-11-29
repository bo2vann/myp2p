package com.think.android.p2p.ui.account.security;

import android.content.Context;

import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 支付密码保存
 * Created by Think on 2017/11/2.
 */

public class SetPayPwdHandler extends CommonRemoteHandler {

    String password;

    public SetPayPwdHandler(Context context, String password) {
        super(context);
        this.password = password;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        UserInfoUtils userInfoUtils = new UserInfoUtils(context);
        try {
            request.put("password", password);
            request.put("userName", userInfoUtils.getUserName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "SavePayPassword";
    }
}
