package com.think.android.p2p.ui.account.bankcard;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONObject;

/**
 * 查询可绑银行
 * Created by Think on 2017/10/31.
 */

public class QueryBankListHandler extends CommonRemoteHandler {

    public QueryBankListHandler(Context context) {
        super(context);
    }

    @Override
    protected JSONObject createRequestData() {
        return new JSONObject();
    }

    @Override
    protected String getMethod() {
        return "SysBankList";
    }
}
