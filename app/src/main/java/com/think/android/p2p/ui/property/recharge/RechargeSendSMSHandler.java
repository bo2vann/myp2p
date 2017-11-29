package com.think.android.p2p.ui.property.recharge;

import android.content.Context;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 充值发送短信
 * Created by Think on 2017/11/6.
 */

public class RechargeSendSMSHandler extends CommonRemoteHandler {

    String mobile;

    public RechargeSendSMSHandler(Context context, String mobile) {
        super(context);
        this.mobile = mobile;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("mobile", mobile);
            request.put("smsType", "FSYZ");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "smsVerifyCode";
    }
}
