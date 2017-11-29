package com.think.android.p2p.ui.home;

import android.content.Context;

import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by Think on 2017/11/11.
 */

public class BannerHandler extends CommonRemoteHandler {

    public BannerHandler(Context context) {
        super(context);
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("bannerType", "APPBN");
            request.put("uuid", UUID.randomUUID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "HomeBannerPic";
    }
}
