package com.think.android.p2p.base;

import android.content.Context;
import android.support.v4.util.ArraySet;
import android.util.Log;

import com.amarsoft.android.AmarApplication;
import com.amarsoft.android.ErrorCodeManager;
import com.amarsoft.android.imp.AndroidHttpTransportSE;
import com.amarsoft.android.ui.ASHandler;
import com.amarsoft.android.ui.HandlerRequestModel;
import com.amarsoft.android.ui.MessageHelp;
import com.amarsoft.support.android.TradeManager;
import com.amarsoft.support.android.imp.DefaultFormDataConvertor;
import com.amarsoft.support.android.model.BaseResponse;

import org.json.JSONException;

import java.util.Arrays;


/**
 * Created by Think on 2017/10/24.
 */

public abstract class RemoteHandler extends ASHandler {
    private static final String TAG = "RemoteHandler";

    private HandlerRequestModel model;
    private TradeManager tradeManager;

    public RemoteHandler(Context context) {
        super(context);
        this.tradeManager = TradeManager.getTradeManager();
    }

    protected Object doInBackground(Object... params) {
        Log.d(this.getClass().getName(), "doInBackground()");

        BaseResponse response;
        try {
            UserInfoUtils userInfoUtils = new UserInfoUtils(context);
            String token = userInfoUtils.getToken();
            response = tradeManager.executeRemoteService(this.model.getMethod(), token, this.model.getRequestdata(), new DefaultFormDataConvertor(), (AmarApplication) this.context.getApplicationContext());
        } catch (JSONException e) {
            e.printStackTrace();
            response = new BaseResponse();
            response.setResultCode("@ERROR");
            response.setBusinessResponseObject("发送或返回数据解析出现错误，请检查服务是否可用");
        } catch (Exception e) {
            e.printStackTrace();
            response = new BaseResponse();
            response.setResultCode("@ERROR");
            if (e.getMessage() != null) {
                response.setBusinessResponseObject(e.getMessage());
            } else {
                response.setBusinessResponseObject("未知的错误,请重试");
            }
        }
        Log.d(TAG, "response code:" + response.getResultCode() + ";msg:" + response.getResultMsg());
        return response;
    }


    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        BaseResponse response = (BaseResponse) result;
        if (response.getResultCode().equals("1")) {
            try {
                this.doSuccess(response);
            } catch (Exception var7) {
                var7.printStackTrace();
                StackTraceElement[] stacks = var7.getStackTrace();
                StringBuffer errorInfos = new StringBuffer();

                for (int i = 0; i < stacks.length; ++i) {
                    if (i == 0 || stacks[i].getClassName().contains(this.getClass().getName())) {
                        errorInfos.append("class: " + stacks[i].getClassName() + ",");
                        errorInfos.append("method: " + stacks[i].getMethodName() + ",");
                        errorInfos.append("line: " + stacks[i].getLineNumber() + ",");
                    }
                }

                MessageHelp.error("执行错误：" + var7.toString() + "[" + errorInfos.toString() + "]", this.getClass().getName(), var7, this.context);
            }
        } else {
            this.doError(response);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.model = this.createRequest();
    }

    protected int getRequestTimeout() {
        return AndroidHttpTransportSE.DEFAULT_TIMEOUT;
    }

    public abstract HandlerRequestModel createRequest();

    protected abstract void doSuccess(BaseResponse var1) throws Exception;

    protected void doError(BaseResponse response) {
        if (this.context.getApplicationContext() instanceof AmarApplication) {
            final AmarApplication amapp = (AmarApplication) this.context.getApplicationContext();
            String sTitle = "";
            if (CommonErrorFeild.NET_OUT_TIME.equals(response.getResultCode())) {
                if (response.getBusinessResponseObject() != null) {
                    sTitle = response.getBusinessResponseObject().toString();
                }

                if (sTitle.startsWith("Protocol not found")) {
                    sTitle = ErrorCodeManager.getManager((AmarApplication) this.context.getApplicationContext()).getErrorTitle("url.error");
                }
            } else {
                sTitle = ErrorCodeManager.getManager((AmarApplication) this.context.getApplicationContext()).getErrorTitle(response.getResultCode());
            }
            MessageHelp.alert(sTitle, this.context);
        } else {
            MessageHelp.alert("客户端设置错误：" + this.context.getApplicationContext() + "类不符合要求", this.context);
        }
    }

}
