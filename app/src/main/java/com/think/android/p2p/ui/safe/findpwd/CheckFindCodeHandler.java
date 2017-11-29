package com.think.android.p2p.ui.safe.findpwd;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 找回密码校验短信验证码
 * Created by Think on 2017/10/28.
 */

public class CheckFindCodeHandler extends CommonRemoteHandler {

    String mobile;
    String verifyCode;

    public CheckFindCodeHandler(Context context, String mobile, String verifyCode) {
        super(context);
        this.mobile = mobile;
        this.verifyCode = verifyCode;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("mobile", mobile);
            request.put("verifyCode", verifyCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "ForgetPwdConfirmCode";
    }
}
