package com.think.android.p2p.ui.account.personalinfo;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONObject;

/**
 * 登出交易
 * Created by Think on 2017/11/24.
 */

public class LogoutHandler extends CommonRemoteHandler {

    public LogoutHandler(Context context) {
        super(context);
    }

    @Override
    protected JSONObject createRequestData() {
        return new JSONObject();
    }

    @Override
    protected String getMethod() {
        return "LoginOut";
    }
}
