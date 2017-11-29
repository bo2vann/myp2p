package com.think.android.p2p.ui.safe.gesturepwd;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Think on 2017/11/23.
 */

public class CheckPasswordHandler extends CommonRemoteHandler {

    String passWord;

    public CheckPasswordHandler(Context context, String passWord) {
        super(context);
        this.passWord = passWord;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("passWord", passWord);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "LoginVerification";
    }
}
