package com.think.android.p2p.ui.account.security;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amarsoft.support.android.ui.CommonFragment;
import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.base.CommonValid;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONObject;

/**
 * 支付密码设置-设置
 * Created by Think on 2017/10/15.
 */

public class PayPwdSetFragment extends CommonFragment implements View.OnClickListener {

    EditText payPwdEdit;
    EditText confirmPwdEdit;

    Button finishBtn;

    public PayPwdSetFragment() {
        super(R.layout.fragment_pay_pwd_set);
    }

    @Override
    protected void initViews() {
        super.initViews();

        View view = getView();
        if (view == null) return;

        payPwdEdit = (EditText) view.findViewById(R.id.password_edit);
        confirmPwdEdit = (EditText) view.findViewById(R.id.confirm_password_edit);
        finishBtn = (Button) view.findViewById(R.id.finish_btn);
        finishBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.finish_btn:
                request();
                break;
        }
    }

    private void request() {
        CommonValid commonValid = CommonValid.getInstance(getActivity());
        if (!commonValid.check(payPwdEdit, CommonValid.PAY_PASSWORD_MODE, true, R.string.pay_password_hint, R.string.pay_password_valid)) {
            return;
        }
        if (!commonValid.check(confirmPwdEdit, CommonValid.PAY_PASSWORD_MODE, true, R.string.confirm_pay_password_hint, R.string.confirm_pay_password_valid)) {
            return;
        }
        String password = payPwdEdit.getText().toString();
        String confirmPassword = confirmPwdEdit.getText().toString();
        if (!password.equals(confirmPassword)) {
            Toast.makeText(getContext().getApplicationContext(), R.string.confirm_password_unequal_valid, Toast.LENGTH_SHORT).show();
            return;
        }
        SetPayPwdHandler setPayPwdHandler = new SetPayPwdHandler(getActivity(), password);
        setPayPwdHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    Toast.makeText(getContext().getApplicationContext(), R.string.pay_password_set_success, Toast.LENGTH_SHORT).show();
                    getActivity().setResult(Activity.RESULT_OK);
                    finishCurActivity();
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        setPayPwdHandler.execute();
    }
}
