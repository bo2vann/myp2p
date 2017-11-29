package com.think.android.p2p.ui.account.security;

import android.content.Context;

import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 修改登录密码
 * Created by Think on 2017/11/11.
 */

public class ModifyLogonPwdHandler extends CommonRemoteHandler {

    String oldPassWord;
    String newPassWord;

    public ModifyLogonPwdHandler(Context context, String oldPassWord, String newPassWord) {
        super(context);
        this.oldPassWord = oldPassWord;
        this.newPassWord = newPassWord;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        UserInfoUtils userInfoUtils = new UserInfoUtils(context);
        try {
            request.put("userName", userInfoUtils.getUserName());
            request.put("oldPassWord", oldPassWord);
            request.put("newPassWord", newPassWord);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "UpdateUserPsw";
    }
}
