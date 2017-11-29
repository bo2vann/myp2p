package com.think.android.p2p.ui.safe.register;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 注册交易
 * Created by Think on 2017/10/28.
 */

public class RegisterHandler extends CommonRemoteHandler {

    private String mobile;
    private String passWord;
    private String identifyCode;
    private String inviteCode;

    public RegisterHandler(Context context, String mobile, String passWord, String identifyCode, String inviteCode) {
        super(context);
        this.mobile = mobile;
        this.passWord = passWord;
        this.identifyCode = identifyCode;
        this.inviteCode = inviteCode;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("mobile", mobile);
            request.put("passWord", passWord);
            request.put("identifyCode", identifyCode);
            if (!"".equals(inviteCode)) {
                request.put("inviteCode", inviteCode);
            }
            request.put("registerFrom", "ANDROID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "UserRegister";
    }
}
