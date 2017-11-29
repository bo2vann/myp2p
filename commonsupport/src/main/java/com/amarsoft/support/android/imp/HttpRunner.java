package com.amarsoft.support.android.imp;

import android.util.Log;

import com.amarsoft.android.AmarApplication;
import com.amarsoft.android.imp.AndroidHttpTransportSE;
import com.amarsoft.support.android.DataConvertor;
import com.amarsoft.support.android.TradeRunner;
import com.amarsoft.support.android.model.BaseRequest;
import com.amarsoft.support.android.model.BaseResponse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Think on 2017/10/25.
 */

public class HttpRunner implements TradeRunner {
    private DataConvertor dataConvertor;
    private BaseResponse response;
    protected int requestTimeout;
    private boolean runStatus;
    protected AmarApplication app;
    protected String webserviceUrl;

    public static String sessionid = null;

    public HttpRunner(AmarApplication app, int requestTimeout) {
        this.requestTimeout = AndroidHttpTransportSE.DEFAULT_TIMEOUT;
        this.runStatus = false;
        this.webserviceUrl = "";
        this.app = app;
        this.webserviceUrl = app.WEBSERVICE_URL;
        this.requestTimeout = requestTimeout;
    }

    @Override
    public void setDataConvertor(DataConvertor dataConvertor) {
        this.dataConvertor = dataConvertor;
    }

    @Override
    public BaseResponse getResponseObject() {
        return this.response;
    }

    protected BaseResponse createResponseFromString(String sResult) {
        BaseResponse r = new BaseResponse();
        r.setResultCode(sResult);
        return r;
    }

    @Override
    public void run(String businessMethod, String requestFormat, BaseRequest requestObject) throws Exception {
        String request = dataConvertor.convertToRequestString(requestObject);
        Log.d("", "token:" + requestObject.getToken());
        Log.d("", webserviceUrl + "/" + businessMethod);
        Log.d("", "request:" + request);
        URL url = new URL(webserviceUrl + "/" + businessMethod);
//        URL url = new URL("http://bolufly.imwork.net/jymweb/code.jsp");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        if (sessionid != null) {
            conn.setRequestProperty("cookie", sessionid);
        }

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        conn.setConnectTimeout(AndroidHttpTransportSE.DEFAULT_TIMEOUT);
        conn.setRequestProperty("sign", dataConvertor.convertToSignature(requestObject));
        conn.setRequestProperty("authorization", requestObject.getToken());
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(request.getBytes("utf-8"));

        String cookieval = conn.getHeaderField("Set-Cookie");
        if (cookieval != null) {
            sessionid = cookieval.substring(0, cookieval.indexOf(";"));//获取sessionid
        }

        InputStream is = conn.getInputStream();
        String responseString = getStringByBytes(getBytesByInputStream(is));
        Log.d("", responseString);
        response = this.dataConvertor.convertResultToResponse(responseString);

        if (this.response == null) {
            this.response = new BaseResponse();
            this.response.setResultCode("@ERROR");
            this.response.setBusinessResponseObject("服务返回了错误的数据，请检查服务状态是否正常");
        }

        //最后将conn断开连接
        if (conn != null) {
            conn.disconnect();
        }
    }

    private byte[] getBytesByInputStream(InputStream is) {
        byte[] bytes = null;
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        byte[] buffer = new byte[1024 * 8];
        int length = 0;
        try {
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            bos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }

    private String getStringByBytes(byte[] bytes) {
        String str = "";
        try {
            str = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }
}
