package com.think.android.p2p.ui.account.message;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 消息状态更新
 * Created by Think on 2017/11/25.
 */

public class MessageUpdateHandler extends CommonRemoteHandler {

    String id;

    public MessageUpdateHandler(Context context, String id) {
        super(context);
        this.id = id;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "UpdateMessage";
    }
}
