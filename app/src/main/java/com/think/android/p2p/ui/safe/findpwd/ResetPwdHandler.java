package com.think.android.p2p.ui.safe.findpwd;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 找回密码重设密码
 * Created by Think on 2017/11/11.
 */

public class ResetPwdHandler extends CommonRemoteHandler {

    String mobile;
    String newPassword;
    String verifyCode;

    public ResetPwdHandler(Context context, String mobile, String newPassword, String verifyCode) {
        super(context);
        this.mobile = mobile;
        this.newPassword = newPassword;
        this.verifyCode = verifyCode;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("mobile", mobile);
            request.put("newPassword", newPassword);
            request.put("verifyCode", verifyCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "ForgetPwdResetCode";
    }
}
