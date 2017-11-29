package com.think.android.p2p.ui.property.recharge;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amarsoft.support.android.ui.CommonFragment;
import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.base.CommonValid;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.account.bankcard.BankcardItem;
import com.think.android.p2p.ui.account.bankcard.SelectBankCardAdapter;
import com.think.android.p2p.ui.account.bankcard.SelectBankcardActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_OK;

/**
 * 充值-第一页
 * Created by Think on 2017/10/15.
 */

public class RechargeConfirmFragment extends CommonFragment implements View.OnClickListener {

    public static final int SELECT_BANKCARD = 0x10;

    TextView rechargeAmountText;
    TextView idText;
    EditText phoneNumEdit;
    EditText msgCodeEdit;
    Button getPhoneCodeBtn;

    CheckBox protocolCheck;
    Button rechargeBtn;

    String rechargeNo;

    BankcardItem bankcardItem;

    public RechargeConfirmFragment() {
        super(R.layout.fragment_recharge_confirm);
    }

    @Override
    protected void initViews() {
        super.initViews();

        View view = getView();
        if (view == null) return;

        String data = getArguments().getString("data");
        JSONObject dataJSON;
        try {
            dataJSON = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        rechargeNo = JSONHelper.getStringValue(dataJSON, "rechargeNo");

        rechargeAmountText = (TextView) view.findViewById(R.id.recharge_amount_text);
        rechargeAmountText.setText(JSONHelper.getStringValue(dataJSON, "amount"));
        idText = (TextView) view.findViewById(R.id.id_text);
        idText.setText(JSONHelper.getStringValue(dataJSON, "identityNo"));
        phoneNumEdit = (EditText) view.findViewById(R.id.phone_num_edit);

        msgCodeEdit = (EditText) view.findViewById(R.id.msg_verify_code_edit);
        getPhoneCodeBtn = (Button) view.findViewById(R.id.get_phone_code_button);
        getPhoneCodeBtn.setOnClickListener(this);

//        protocolCheck = (CheckBox) view.findViewById(R.id.protocol_check);

        rechargeBtn = (Button) view.findViewById(R.id.recharge_btn);
        rechargeBtn.setOnClickListener(this);
        bankcardItem = new BankcardItem(getActivity(), view);
        bankcardItem.init();
        bankcardItem.setIndex(getArguments().getInt("index"));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.get_phone_code_button:
                requestSMS();
                break;
            case R.id.recharge_btn:
                requestRechargeSubmit();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
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

    private void requestSMS() {
        CommonValid commonValid = CommonValid.getInstance(getActivity());
        String mobile = phoneNumEdit.getText().toString();
        if (!commonValid.check(phoneNumEdit, CommonValid.MOBILE_MODE, true, R.string.reserve_phone_hint, R.string.reserve_phone_valid)) {
            return;
        }
        RechargeSendSMSHandler rechargeSendSMSHandler = new RechargeSendSMSHandler(getActivity(), mobile);
        rechargeSendSMSHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    setPhoneCodeBtnUnable();
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        rechargeSendSMSHandler.execute();
    }

    private void requestRechargeSubmit() {
        CommonValid commonValid = CommonValid.getInstance(getActivity());
        String smsCode = msgCodeEdit.getText().toString();
        if (!commonValid.check(msgCodeEdit, CommonValid.OTHER_MODE, true, R.string.msg_verify_code_hint, R.string.msg_verify_code_hint)) {
            return;
        }

        RechargeSubmitHandler rechargeSubmitHandler = new RechargeSubmitHandler(getActivity(), rechargeNo, smsCode);
        rechargeSubmitHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    toNext();
                } else {
                    if ("7".equals(JSONHelper.getStringValue(response, "resultCode"))) {
                        toNext();
                    } else
                        Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        rechargeSubmitHandler.execute();
    }

    private void toNext() {
        Bundle bundle = new Bundle();
        bundle.putString("amount", rechargeAmountText.getText().toString());
        RechargeSuccessFragment rechargeSuccessFragment = new RechargeSuccessFragment();
        rechargeSuccessFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, rechargeSuccessFragment,
                RechargeSuccessFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
}
