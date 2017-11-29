package com.think.android.p2p.ui.property.recharge;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 充值申请
 * Created by Think on 2017/11/6.
 */

public class RechargeApplyHandler extends CommonRemoteHandler {

    String bankCardId;
    String amount;

    public RechargeApplyHandler(Context context, String bankCardId, String amount) {
        super(context);
        this.bankCardId = bankCardId;
        this.amount = amount;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("bankCardId", bankCardId);
            request.put("amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "RechargeOrderApi";
    }
}
