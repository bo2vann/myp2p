package com.think.android.p2p.ui.invest;

import android.content.Context;

import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 投资校验交易密码
 * Created by Think on 2017/11/5.
 */

public class InvestCheckPwdHandler extends CommonRemoteHandler {

    String password;

    public InvestCheckPwdHandler(Context context, String password) {
        super(context);
        this.password = password;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        UserInfoUtils userInfoUtils = new UserInfoUtils(context);
        try {
            request.put("userName", userInfoUtils.getUserName());
            request.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "verificationPassword";
    }
}
