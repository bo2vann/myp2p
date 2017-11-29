package com.think.android.p2p.ui.account.invite;

import android.content.Context;

import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 查询邀请链接
 * Created by Think on 2017/11/18.
 */

public class InviteUrlHandler extends CommonRemoteHandler {

    public InviteUrlHandler(Context context) {
        super(context);
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        UserInfoUtils userInfoUtils = new UserInfoUtils(context);
        try {
            request.put("userName", userInfoUtils.getUserName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "QRCodeShare";
    }
}
