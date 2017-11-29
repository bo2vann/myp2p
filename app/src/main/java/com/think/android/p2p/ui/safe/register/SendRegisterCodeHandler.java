package com.think.android.p2p.ui.safe.register;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 请求注册短信验证码
 * Created by Think on 2017/10/28.
 */

public class SendRegisterCodeHandler extends CommonRemoteHandler {

    private String mobile;
    private String verificationCode;

    public SendRegisterCodeHandler(Context context, String mobile, String verificationCode) {
        super(context);
        this.mobile = mobile;
        this.verificationCode = verificationCode;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("mobile", mobile);
            request.put("verificationCode", verificationCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "SmsRegisterCode";
    }
}
