package com.think.android.p2p.ui;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * app版本查询
 * Created by Think on 2017/11/20.
 */

public class QueryVersionHandler extends CommonRemoteHandler {

    public QueryVersionHandler(Context context) {
        super(context);
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("platform", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "Versions";
    }
}
