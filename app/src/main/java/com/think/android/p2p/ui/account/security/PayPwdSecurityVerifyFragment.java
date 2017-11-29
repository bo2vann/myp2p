package com.think.android.p2p.ui.account.security;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amarsoft.support.android.ui.CommonFragment;
import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.base.CommonValid;
import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 支付密码设置-安全验证
 * Created by Think on 2017/10/15.
 */

public class PayPwdSecurityVerifyFragment extends CommonFragment implements View.OnClickListener {

    EditText phoneNumEdit;
    EditText msgVerifyCodeEdit;
    Button getPhoneCodeBtn;
    Button nextBtn;

    public PayPwdSecurityVerifyFragment() {
        super(R.layout.fragment_pay_pwd_security_verify);
    }

    @Override
    protected void initViews() {
        super.initViews();

        View view = getView();
        if (view == null) return;

        phoneNumEdit = (EditText) view.findViewById(R.id.phone_num_edit);

        UserInfoUtils userInfoUtils = new UserInfoUtils(getActivity());
        phoneNumEdit.setText(userInfoUtils.getMaskMobile());

        msgVerifyCodeEdit = (EditText) view.findViewById(R.id.msg_verify_code_edit);
        getPhoneCodeBtn = (Button) view.findViewById(R.id.get_phone_code_button);
        getPhoneCodeBtn.setOnClickListener(this);
        nextBtn = (Button) view.findViewById(R.id.next_step_btn);
        nextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.get_phone_code_button:
                requestSMS();
                break;
            case R.id.next_step_btn:
                checkSMS();
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
        getPhoneCodeBtn.setText(R.string.get_verify_code);
    }

    /**
     * 设置按钮不可点击
     */
    private void setPhoneCodeBtnUnable() {
        sec = 120;
        getPhoneCodeBtn.setClickable(false);
//        getPhoneCodeBtn.setBackgroundResource(R.drawable.btn_msg_unable_bg);
        getPhoneCodeBtn.setTextColor(getResources().getColor(R.color.bind_send_msg_unable));
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
//        CommonValid commonValid = CommonValid.getInstance(getContext());
//        if (!commonValid.check(phoneNumEdit, CommonValid.MOBILE_MODE, true, R.string.phone_num_hint, R.string.phone_valid_hint)) {
//            return;
//        }
        UserInfoUtils userInfoUtils = new UserInfoUtils(getActivity());
        String mobile = userInfoUtils.getMobile();
        SetPayPwdSendSMSHandler setPayPwdSendSMSHandler = new SetPayPwdSendSMSHandler(getActivity(), mobile);
        setPayPwdSendSMSHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    Toast.makeText(getContext().getApplicationContext(), R.string.msg_send_success, Toast.LENGTH_SHORT).show();
                    setPhoneCodeBtnUnable();
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        setPayPwdSendSMSHandler.execute();
    }

    private void checkSMS() {
        CommonValid commonValid = CommonValid.getInstance(getContext());
//        if (!commonValid.check(phoneNumEdit, CommonValid.MOBILE_MODE, true, R.string.phone_num_hint, R.string.phone_valid_hint)) {
//            return;
//        }
        if (!commonValid.check(msgVerifyCodeEdit, CommonValid.OTHER_MODE, true, R.string.msg_verify_code_hint, R.string.msg_verify_code_hint)) {
            return;
        }
        UserInfoUtils userInfoUtils = new UserInfoUtils(getActivity());
        String mobile = userInfoUtils.getMobile();
        String smsCode = msgVerifyCodeEdit.getText().toString();

        SetPayPwdCheckSMSHandler setPayPwdCheckSMSHandler = new SetPayPwdCheckSMSHandler(getActivity(), mobile, smsCode);
        setPayPwdCheckSMSHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    toNext();
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        setPayPwdCheckSMSHandler.execute();
    }

    /**
     * 跳转下一步
     */
    private void toNext() {
        PayPwdSetFragment payPwdSetFragment = new PayPwdSetFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, payPwdSetFragment,
                PayPwdSetFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

}
