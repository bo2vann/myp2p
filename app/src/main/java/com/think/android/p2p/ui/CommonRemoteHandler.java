package com.think.android.p2p.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.amarsoft.android.ui.HandlerRequestModel;
import com.amarsoft.support.android.model.BaseResponse;
import com.think.android.p2p.R;
import com.think.android.p2p.base.BaseApplication;
import com.think.android.p2p.base.CommonErrorFeild;
import com.think.android.p2p.base.RemoteHandler;
import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.safe.logon.LogonActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * 基础通讯handler
 * Created by blu on 2015/7/17.
 */
public abstract class CommonRemoteHandler extends RemoteHandler {

    protected Context context;

    protected ResponseListener responseListener;

    public CommonRemoteHandler(Context context) {
        super(context);
        this.context = context.getApplicationContext();
    }

    @Override
    public HandlerRequestModel createRequest() {
        JSONObject requestData = createRequestData();
        HandlerRequestModel model = new HandlerRequestModel();
        model.setMethod(getMethod());
        model.setRequestdata(requestData);
        return model;
    }

    /**
     * 建立请求数据包
     *
     * @return 请求数据包
     */
    protected abstract JSONObject createRequestData();

    /**
     * 设置请求方法
     *
     * @return 方法
     */
    protected abstract String getMethod();

    @Override
    protected void doSuccess(BaseResponse baseResponse) throws Exception {
        if (responseListener != null) {
            try {
                responseListener.responseCallback("SUCCESS", (JSONObject) baseResponse.getBusinessResponseObject());
            } catch (Exception e) {
                e.printStackTrace();
                responseListener.responseCallback("ERROR", null);
            }
        }
    }

    @Override
    protected void doError(BaseResponse baseResponse) {
        if (Arrays.asList(CommonErrorFeild.NEED_LOGON).contains(baseResponse.getResultCode())) {
            doOutTime();
            return;
        } else if (baseResponse.getResultCode().toUpperCase().equals(CommonErrorFeild.NET_OUT_TIME)) {
//            Toast.makeText(context, context.getString(R.string.result_msg_default_error), Toast.LENGTH_SHORT).show();
            if (responseListener != null) {
                JSONObject object = new JSONObject();
                try {
                    object.put("resultMsg", context.getResources().getString(R.string.result_msg_default_error));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                responseListener.responseCallback("fail", object);
            }
            return;
        }

        if (responseListener != null) {
            responseListener.responseCallback(baseResponse.getResultCode(), (JSONObject) baseResponse.getBusinessResponseObject());
        }
    }

    /**
     * 响应监听
     */
    public interface ResponseListener {
        /**
         * 回调函数
         *
         * @param object   object.toString() == "SUCCESS"，请求成功，否则失败
         * @param response 响应内容
         */
        void responseCallback(Object object, JSONObject response);
    }

    /**
     * 设置服务器响应监听
     *
     * @param responseListener 响应监听
     */
    public void setResponseListener(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    /**
     * 获取当前登录User id
     *
     * @return 当前登录UserId 若未登录返回“”
     */
    protected String getToken() {
        if (context instanceof Activity) {
            BaseApplication application = (BaseApplication) ((Activity) context)
                    .getApplication();
            if (application.getUserInfoUtils(context).isLogonSuccess()) {
                return application.getUserInfoUtils(context).getToken();
            }
        }
        return "";
    }

    protected void doOutTime() {
        UserInfoUtils userInfoUtils = new UserInfoUtils(context);
        userInfoUtils.clearUserInfo("exit");
        Intent intent = new Intent(context, LogonActivity.class);
        intent.putExtra("restart", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 设置加载动画点击外部不消失、加载时点击返回不消失
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (dialog != null) {
            dialog.setCancelable(false);// 设置进度条是否可以按退回键取消
            dialog.setCanceledOnTouchOutside(false);// 设置点击进度对话框外的区域对话框不消失
        }
    }

}
