package com.think.android.p2p.ui.account.bankcard;

import android.content.Context;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 绑卡发送短信验证码
 * Created by Think on 2017/10/31.
 */

public class BindSendSMSHandler extends CommonRemoteHandler {

    private String accountName;
    private String bank;
    private String cardNo;
    private String bankName;
    private String identityNo;
    private String mobileNo;
    private String verifiedFlag;

    public BindSendSMSHandler(Context context, String accountName, String identityNo, String bank, String bankName, String cardNo, String mobileNo, String verifiedFlag) {
        super(context);
        this.accountName = accountName;
        this.bank = bank;
        this.bankName = bankName;
        this.cardNo = cardNo;
        this.identityNo = identityNo;
        this.mobileNo = mobileNo;
        this.verifiedFlag = verifiedFlag;
    }

    @Override
    protected JSONObject createRequestData() {
        JSONObject request = new JSONObject();
        try {
            request.put("accountName", accountName);
            request.put("bank", bank);
            request.put("bankName", bankName);
            request.put("cardNo", cardNo);
            request.put("identityNo", identityNo);
            request.put("mobileNo", mobileNo);
            request.put("verifiedFlag", verifiedFlag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    protected String getMethod() {
        return "QueryBankMobileVriCode";
    }
}
