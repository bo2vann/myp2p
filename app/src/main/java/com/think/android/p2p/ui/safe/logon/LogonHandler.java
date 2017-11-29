package com.think.android.p2p.ui.safe.logon;

import android.content.Context;
import android.widget.Toast;

import com.amarsoft.support.android.model.BaseResponse;
import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.base.CommonErrorFeild;
import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登录
 * Created by Think on 2017/10/28.
 */

public class LogonHandler extends CommonRemoteHandler {

    String mobile;
    String passWord;

    public LogonHandler(Context context, String mobile, String passWord) {
        super(context);
        this.mobile = mobile;
        this.passWord = passWord;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("mobile", mobile);
            request.put("passWord", passWord);
            request.put("loginFrom", "ANDROID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return request;
    }

    @Override
    protected String getMethod() {
        return "UserLogin";
    }

    @Override
    protected void doSuccess(BaseResponse baseResponse) throws Exception {
        String resultCode = baseResponse.getResultCode();
        if ("1".equals(resultCode)) {
            UserInfoUtils userInfoUtils = new UserInfoUtils(context);
            JSONObject logonObject = (JSONObject) baseResponse.getBusinessResponseObject();
            userInfoUtils.setUserName(JSONHelper.getStringValue(logonObject, "userName"));
            userInfoUtils.setMobile(mobile);
//            userInfoUtils.setUserPsw(passWord);
            userInfoUtils.setRealName(JSONHelper.getStringValue(logonObject, "realName"));
            userInfoUtils.setNickName(JSONHelper.getStringValue(logonObject, "nickName"));
            userInfoUtils.setToken(JSONHelper.getStringValue(logonObject, "token"));
            userInfoUtils.setUserInfoObject(logonObject);

            if (logonListener != null) {
                logonListener.logonCallBack(0);
            }
        }

    }

    @Override
    protected void doError(BaseResponse baseResponse) {
        super.doError(baseResponse);
        String errorMsg = baseResponse.getResultMsg();

        if (!baseResponse.getResultCode().toUpperCase().equals(CommonErrorFeild.NET_OUT_TIME)) {
            Toast.makeText(context.getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
        }
        if (logonListener != null) {
            logonListener.logonCallBack(-1);
        }
    }

    /**
     * 设置登录监听
     *
     * @param listener
     */
    public void setLogonListener(LogonListener listener) {
        this.logonListener = listener;
    }

    protected LogonListener logonListener;

    public interface LogonListener {
        /**
         * 登录交易登录反馈回调
         *
         * @param resultCode 登录状态代码；0-登陆成功；1-用户名或密码错误；-1-未知错误；
         */
        void logonCallBack(int resultCode);
    }
}
