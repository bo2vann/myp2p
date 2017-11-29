package com.think.android.p2p.ui.account.message;

import android.content.Context;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 未读消息条数查询
 * Created by Think on 2017/11/11.
 */

public class MessageCountHandler extends CommonRemoteHandler {

    public MessageCountHandler(Context context) {
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
        return "MessageCenter";
    }
}
