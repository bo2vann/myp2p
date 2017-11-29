package com.think.android.p2p.ui.account.about;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Think on 2017/11/18.
 */

public class QueryAboutHandler extends CommonRemoteHandler {

    String clientType;

    public QueryAboutHandler(Context context, String clientType) {
        super(context);
        this.clientType = clientType;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("clientType", clientType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "AboutIdjshi";
    }
}
