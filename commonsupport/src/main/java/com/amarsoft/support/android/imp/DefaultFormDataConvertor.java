package com.amarsoft.support.android.imp;

import com.amarsoft.android.security.MD5;
import com.amarsoft.support.android.DataConvertor;
import com.amarsoft.support.android.model.BaseRequest;
import com.amarsoft.support.android.model.BaseResponse;
import com.amarsoft.support.android.utils.MD5Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by Think on 2017/10/25.
 */

public class DefaultFormDataConvertor implements DataConvertor {

    String salt = "9zI8ywQtzhNFxVrYUlnO";

    public DefaultFormDataConvertor() {
    }

    @Override
    public String convertToSignature(BaseRequest request) {
        JSONObject requestObject = new JSONObject();
        try {
            requestObject = (JSONObject) request.getBusinessRequestObject();
        } catch (Exception e) {
            return MD5Util.md5((String) request.getBusinessRequestObject(), salt);
        }
        if (requestObject.length() <= 0) {
            return MD5Util.md5("", salt);
        }
        Iterator iterator = requestObject.keys();
        StringBuilder sb = new StringBuilder();
        try {
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = requestObject.getString(key);
                sb.append(URLEncoder.encode(value, "utf-8")).append("=");
            }
//            sb.deleteCharAt(sb.length() - 1);
        } catch (JSONException e) {
            e.printStackTrace();
            return MD5Util.md5("", salt);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return MD5Util.md5(sb.toString(), salt);
        }
        return MD5Util.md5(sb.toString(), salt);
    }

    @Override
    public String convertToRequestString(BaseRequest request) {
        JSONObject requestObject = new JSONObject();
        try {
            requestObject = (JSONObject) request.getBusinessRequestObject();
        } catch (Exception e) {
            return (String) request.getBusinessRequestObject();
        }
        if (requestObject.length() <= 0) {
            return "";
        }
        Iterator iterator = requestObject.keys();
        StringBuilder sb = new StringBuilder();
        try {
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = requestObject.getString(key);
                sb.append(key).append("=").append(URLEncoder.encode(value, "utf-8"));
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

    @Override
    public BaseResponse convertResultToResponse(String response) {

        BaseResponse baseResponse = new BaseResponse();
        try {
            JSONObject responseObject = new JSONObject(response);
            baseResponse.setResultCode(responseObject.getString("resultCode"));
            baseResponse.setResultMsg(responseObject.getString("resultMsg"));
            baseResponse.setCountPage(responseObject.getString("countPage"));
            baseResponse.setCurPage(responseObject.getString("curPage"));
            baseResponse.setUuid(responseObject.getString("uuid"));
            baseResponse.setBusinessResponseObject(responseObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return baseResponse;
    }
}
