package com.think.android.p2p.ui.invest;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 收益计算
 * Created by Think on 2017/11/5.
 */

public class CalcProfitHandler extends CommonRemoteHandler {

    String investAmount;
    String projectNo;

    public CalcProfitHandler(Context context, String projectNo, String investAmount) {
        super(context);
        this.projectNo = projectNo;
        this.investAmount = investAmount;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("projectNo", projectNo);
            request.put("investAmount", investAmount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "incomeCalculator";
    }

    @Override
    protected boolean useOverLayout() {
        return false;
    }
}
