package com.think.android.p2p.ui.account.bankcard;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 解绑银行卡
 * Created by Think on 2017/11/15.
 */

public class UnbindBankcardHandler extends CommonRemoteHandler {

    String bankId;

    public UnbindBankcardHandler(Context context, String bankId) {
        super(context);
        this.bankId = bankId;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("bankId", bankId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "DeleteUserBankCard";
    }
}
