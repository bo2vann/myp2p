package com.think.android.p2p.ui.account.bankcard;

import android.content.Context;

import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 已绑银行卡列表查询
 * Created by Think on 2017/10/30.
 */

public class QueryBindBankcardHandler extends CommonRemoteHandler {

    public QueryBindBankcardHandler(Context context) {
        super(context);
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        UserInfoUtils userInfoUtils = new UserInfoUtils(context);
        try {
            request.put("userName", userInfoUtils.getUserName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "UserBankCardList";
    }
}
