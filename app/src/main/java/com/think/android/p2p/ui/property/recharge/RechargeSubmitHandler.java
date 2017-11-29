package com.think.android.p2p.ui.property.recharge;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 充值提交
 * Created by Think on 2017/11/6.
 */

public class RechargeSubmitHandler extends CommonRemoteHandler {

    String rechargeNo;
    String smsCode;

    public RechargeSubmitHandler(Context context, String rechargeNo, String smsCode) {
        super(context);
        this.rechargeNo = rechargeNo;
        this.smsCode = smsCode;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("rechargeNo", rechargeNo);
            request.put("smsCode", smsCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "RechargeConfirmApi";
    }
}
