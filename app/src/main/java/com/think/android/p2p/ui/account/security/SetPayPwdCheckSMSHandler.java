package com.think.android.p2p.ui.account.security;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 交易密码设置验证短信
 * Created by Think on 2017/11/2.
 */

public class SetPayPwdCheckSMSHandler extends CommonRemoteHandler {

    String mobile;
    String smsCode;

    public SetPayPwdCheckSMSHandler(Context context, String mobile, String smsCode) {
        super(context);
        this.mobile = mobile;
        this.smsCode = smsCode;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("mobile", mobile);
            request.put("smsCode", smsCode);
            request.put("smsType", "UZYZ");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "verifyCode";
    }
}
