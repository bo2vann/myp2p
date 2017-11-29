package com.think.android.p2p.ui.property;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONObject;

/**
 * 资产页信息查询
 * <p>
 * Created by Think on 2017/10/29.
 */

public class PropertyHandler extends CommonRemoteHandler {

    public PropertyHandler(Context context) {
        super(context);
    }

    @Override
    protected JSONObject createRequestData() {
        return new JSONObject();
    }

    @Override
    protected String getMethod() {
        return "PersonalAccount";
    }
}
