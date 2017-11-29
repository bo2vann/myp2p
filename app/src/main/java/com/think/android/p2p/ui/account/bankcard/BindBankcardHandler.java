package com.think.android.p2p.ui.account.bankcard;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 绑卡验证短信
 * Created by Think on 2017/10/31.
 */

public class BindBankcardHandler extends CommonRemoteHandler {

    String verNo;
    String verCode;

    public BindBankcardHandler(Context context, String verNo, String verCode) {
        super(context);
        this.verNo = verNo;
        this.verCode = verCode;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("verNo", verNo);
            request.put("verCode", verCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "SaveUserBankCard";
    }
}
