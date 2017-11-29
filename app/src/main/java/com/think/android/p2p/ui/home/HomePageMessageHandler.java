package com.think.android.p2p.ui.home;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONObject;

/**
 * 首页查询消息
 * Created by Think on 2017/11/26.
 */

public class HomePageMessageHandler extends CommonRemoteHandler {

    public HomePageMessageHandler(Context context) {
        super(context);
    }

    @Override
    protected JSONObject createRequestData() {
        return new JSONObject();
    }

    @Override
    protected String getMethod() {
        return "HomeMessage";
    }
}
