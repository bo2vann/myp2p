package com.think.android.p2p.ui.account.about;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONObject;

/**
 * 查询QA类型
 * Created by Think on 2017/11/18.
 */

public class QueryQATypeHandler extends CommonRemoteHandler {

    public QueryQATypeHandler(Context context) {
        super(context);
    }

    @Override
    protected JSONObject createRequestData() {
        return new JSONObject();
    }

    @Override
    protected String getMethod() {
        return "QueryQAType";
    }
}
