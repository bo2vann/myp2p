package com.think.android.p2p.ui.invest;

import android.content.Context;
import android.os.Environment;

import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 查询项目详情
 * Created by Think on 2017/10/28.
 */

public class ProductInfoHandler extends CommonRemoteHandler {

    private String projectNo;

    public ProductInfoHandler(Context context, String projectNo) {
        super(context);
        this.projectNo = projectNo;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        UserInfoUtils userInfoUtils = new UserInfoUtils(context);
        String userName = userInfoUtils.getUserName();

        try {
            request.put("projectNo", projectNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "ProjectInfo";
    }
}
