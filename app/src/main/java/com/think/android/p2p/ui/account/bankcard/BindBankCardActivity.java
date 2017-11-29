package com.think.android.p2p.ui.account.bankcard;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.base.CommonValid;
import com.think.android.p2p.ui.AutoBackBtnActivity;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.account.UserBaseInfoHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 银行卡列表
 * Created by Think on 2017/10/14.
 */

public class BindBankCardActivity extends AutoBackBtnActivity implements View.OnClickListener {

    EditText cardHolderNameEdit;
    EditText idCardEdit;
    Spinner bankSpinner;
    EditText cardNoEdit;
    EditText reservePhoneEdit;
    EditText msgCodeEdit;
    Button getPhoneCodeBtn;

    Button finishBtn;

    String verNo = "";

    ArrayList<JSONObject> bankList;
    BankListAdapter bankListAdapter;
    JSONObject selectBank;

    boolean realFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_bankcard);

        addTopTitle(R.string.my_bankcard);

        initViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void initViews() {
        cardHolderNameEdit = (EditText) findViewById(R.id.cardholder_name_edit);
        idCardEdit = (EditText) findViewById(R.id.identity_card_edit);
        bankSpinner = (Spinner) findViewById(R.id.bank_spinner);
        cardNoEdit = (EditText) findViewById(R.id.bankcard_no_edit);
        reservePhoneEdit = (EditText) findViewById(R.id.reserve_phone_edit);
        msgCodeEdit = (EditText) findViewById(R.id.msg_verify_code_edit);
        getPhoneCodeBtn = (Button) findViewById(R.id.get_phone_code_button);
        finishBtn = (Button) findViewById(R.id.finish_btn);

        getPhoneCodeBtn.setOnClickListener(this);
        finishBtn.setOnClickListener(this);

        bankList = new ArrayList<>();
        JSONObject first = new JSONObject();
        try {
            first.put("bankName", "请点击选择银行");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bankList.add(first);
        bankListAdapter = new BankListAdapter(this, bankList);
        bankSpinner.setAdapter(bankListAdapter);

        bankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = (int) (position < id ? position : id);
                if (index == 0) selectBank = null;
                else selectBank = bankList.get(index);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        requestBaseInfo();
        requestBank();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.get_phone_code_button:
                requestPhoneCode();
                break;
            case R.id.finish_btn:
                requestBind();
                break;
        }
    }

    private void requestBaseInfo() {
        UserBaseInfoHandler userBaseInfoHandler = new UserBaseInfoHandler(this, true);
        userBaseInfoHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    if ("1".equals(JSONHelper.getStringValue(response, "cerFlag"))) {
                        cardHolderNameEdit.setText(JSONHelper.getStringValue(response, "realName"));
                        cardHolderNameEdit.setFocusable(false);
                        idCardEdit.setText(JSONHelper.getStringValue(response, "identityNo"));
                        idCardEdit.setFocusable(false);
                        realFlag = true;
                    } else {
                        realFlag = false;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        userBaseInfoHandler.execute();
    }

    private void requestBank() {
        QueryBankListHandler queryBankListHandler = new QueryBankListHandler(this);
        queryBankListHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    try {
                        JSONArray bankArray = (JSONArray) JSONHelper.getValue(response, "list");
                        for (int i = 0; i < bankArray.length(); i++) {
                            JSONObject bank = bankArray.getJSONObject(i);
                            bankList.add(bank);
                        }
                        bankListAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        queryBankListHandler.execute();
    }

    int sec;
    Timer timer;

    /**
     * 更新获取手机验证码按钮
     */
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (sec == 0) {
                    resetGetPhoneCodeBtn();
                } else {
                    sec--;
                    getPhoneCodeBtn.setText("" + sec);

                }
            }
        }

    };

    /**
     * 重置按钮
     */
    private void resetGetPhoneCodeBtn() {
        timer.cancel();
        getPhoneCodeBtn.setClickable(true);
//        getPhoneCodeBtn.setBackgroundResource(R.drawable.btn_msg_able_bg);
        getPhoneCodeBtn.setTextColor(getResources().getColor(R.color.bind_send_msg));
        getPhoneCodeBtn.setText(R.string.get);
    }

    /**
     * 设置按钮不可点击
     */
    private void setPhoneCodeBtnUnable() {
        sec = 60;
        getPhoneCodeBtn.setClickable(false);
        getPhoneCodeBtn.setTextColor(getResources().getColor(R.color.bind_send_msg_unable));
//        getPhoneCodeBtn.setBackgroundResource(R.drawable.btn_msg_unable_bg);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }, 1000, 1000);
    }

    private void requestPhoneCode() {
        CommonValid commonValid = CommonValid.getInstance(this);
        if (!realFlag) {
            if (!commonValid.check(cardHolderNameEdit, CommonValid.NAME_MODE, true, R.string.cardholder_name_hint, R.string.cardholder_name_valid)) {
                return;
            }
            if (!commonValid.check(idCardEdit, CommonValid.ID_MODE, true, R.string.identity_card_no_hint, R.string.identity_card_no_valid)) {
                return;
            }
        }
        if (selectBank == null) {
            Toast.makeText(getApplicationContext(), R.string.bank_name_hint, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!commonValid.check(cardNoEdit, CommonValid.OTHER_MODE, true, R.string.bankcard_no_hint, R.string.bankcard_no_hint)) {
            return;
        }
        if (!commonValid.check(reservePhoneEdit, CommonValid.MOBILE_MODE, true, R.string.reserve_phone_hint, R.string.reserve_phone_valid)) {
            return;
        }
        String accountName = "";
        String identityNo = "";
        String verifiedFlag = "1";
        if (!realFlag) {
            accountName = cardHolderNameEdit.getText().toString();
            identityNo = idCardEdit.getText().toString();
            verifiedFlag = "0";
        }
        String bank = JSONHelper.getStringValue(selectBank, "bankCode");
        String bankName = JSONHelper.getStringValue(selectBank, "bankName");
        String cardNo = cardNoEdit.getText().toString();
        String mobileNo = reservePhoneEdit.getText().toString();

        BindSendSMSHandler bindSendSMSHandler = new BindSendSMSHandler(this, accountName, identityNo, bank, bankName, cardNo, mobileNo, verifiedFlag);
        bindSendSMSHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    Toast.makeText(getApplicationContext(), R.string.msg_send_success, Toast.LENGTH_SHORT).show();
                    setPhoneCodeBtnUnable();
                    verNo = JSONHelper.getStringValue(response, "verNo");
                } else {
                    Toast.makeText(getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        bindSendSMSHandler.execute();
    }

    private void requestBind() {
        CommonValid commonValid = CommonValid.getInstance(this);
        if ("".equals(verNo)) {
            Toast.makeText(getApplicationContext(), R.string.need_sms, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!commonValid.check(msgCodeEdit, CommonValid.OTHER_MODE, true, R.string.msg_verify_code_hint, R.string.msg_verify_code_hint)) {
            return;
        }

        String verCode = msgCodeEdit.getText().toString();
        BindBankcardHandler bindBankcardHandler = new BindBankcardHandler(this, verNo, verCode);
        bindBankcardHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    Toast.makeText(getApplicationContext(), R.string.bind_card_success, Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finishCurActivity();
                } else {
                    Toast.makeText(getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        bindBankcardHandler.execute();
    }
}
