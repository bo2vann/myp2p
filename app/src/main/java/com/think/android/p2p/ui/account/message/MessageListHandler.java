package com.think.android.p2p.ui.account.message;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 查询信息
 * Created by Think on 2017/11/18.
 */

public class MessageListHandler extends CommonRemoteHandler {

    String messageType;

    private boolean needLoadingDialog;

    public MessageListHandler(Context context, String messageType, boolean needLoadingDialog) {
        super(context);
        this.messageType = messageType;
        this.needLoadingDialog = needLoadingDialog;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("messageType", messageType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "MessageDetail";
    }

    @Override
    protected boolean useOverLayout() {
        return needLoadingDialog;
    }
}
