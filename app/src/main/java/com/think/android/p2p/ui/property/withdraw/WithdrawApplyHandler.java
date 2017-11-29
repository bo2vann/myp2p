package com.think.android.p2p.ui.property.withdraw;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 提现申请
 * Created by Think on 2017/11/11.
 */

public class WithdrawApplyHandler extends CommonRemoteHandler {

    String amount;
    String bankCardId;

    public WithdrawApplyHandler(Context context, String amount, String bankCardId) {
        super(context);
        this.amount = amount;
        this.bankCardId = bankCardId;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("amount", amount);
            request.put("bankCardId", bankCardId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "ToCashApply";
    }
}
