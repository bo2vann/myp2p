package com.think.android.p2p.ui.account.security;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 设置支付密码安全校验发送短信
 * Created by Think on 2017/11/2.
 */

public class SetPayPwdSendSMSHandler extends CommonRemoteHandler {

    String mobile;

    public SetPayPwdSendSMSHandler(Context context, String mobile) {
        super(context);
        this.mobile = mobile;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("mobile", mobile);
            request.put("smsType", "UZYZ");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "smsVerifyCode";
    }
}
