package com.think.android.p2p.ui.property.withdraw;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 提现确认交易
 * Created by Think on 2017/11/14.
 */

public class WithdrawConfirmHandler extends CommonRemoteHandler {

    String tcNo;
    String password;

    public WithdrawConfirmHandler(Context context, String tcNo, String password) {
        super(context);
        this.tcNo = tcNo;
        this.password = password;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("tcNo", tcNo);
            request.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "ToCashConfirm";
    }
}
