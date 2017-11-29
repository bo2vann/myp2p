package com.think.android.p2p.ui.invest;

import android.content.Context;

import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 投资
 * Created by Think on 2017/11/5.
 */

public class InvestHandler extends CommonRemoteHandler {

    String projectNo;
    String invAmount;

    public InvestHandler(Context context, String projectNo, String invAmount) {
        super(context);
        this.projectNo = projectNo;
        this.invAmount = invAmount;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        UserInfoUtils userInfoUtils = new UserInfoUtils(context);
        try {
            request.put("userName", userInfoUtils.getUserName());
            request.put("projectNo", projectNo);
            request.put("invAmount", invAmount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "InvestProject";
    }
}
