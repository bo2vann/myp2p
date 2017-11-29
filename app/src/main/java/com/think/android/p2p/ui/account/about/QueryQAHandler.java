package com.think.android.p2p.ui.account.about;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Think on 2017/11/18.
 */

public class QueryQAHandler extends CommonRemoteHandler {

    String dicCode;

    public QueryQAHandler(Context context, String dicCode) {
        super(context);
        this.dicCode = dicCode;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("dicCode", dicCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "QueryQuestionAnswer";
    }
}
