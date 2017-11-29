package com.think.android.p2p.ui.safe.findpwd;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amarsoft.support.android.ui.CommonFragment;
import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.base.CommonValid;
import com.think.android.p2p.ui.CommonActivity;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONObject;

/**
 * 找回登录密码-设置新密码
 * Created by Think on 2017/10/10.
 */

public class ResetPwdFragment extends CommonFragment implements View.OnClickListener {

    EditText passwordEdit;
    EditText confirmPasswordEdit;

    String mobile;
    String verifyCode;

    public ResetPwdFragment() {
        super(R.layout.fragment_reset_pwd);
    }

    @Override
    protected void initViews() {
        super.initViews();
        View v = getView();
        if (v == null) return;

        passwordEdit = (EditText) v.findViewById(R.id.password_edit);
        confirmPasswordEdit = (EditText) v.findViewById(R.id.confirm_password_edit);

        Button finishBtn = (Button) v.findViewById(R.id.finish_btn);
        finishBtn.setOnClickListener(this);

        mobile = getArguments().getString("mobile");
        verifyCode = getArguments().getString("verifyCode");
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
        CommonValid commonValid = CommonValid.getInstance(getContext());
        if (!commonValid.check(passwordEdit, CommonValid.LOGON_PASSWORD_MODE, true, R.string.logon_password_hint, R.string.logon_password_valid_hint)) {
            return;
        }
        if (!commonValid.check(confirmPasswordEdit, CommonValid.LOGON_PASSWORD_MODE, true, R.string.confirm_password_empty_valid, R.string.confirm_password_valid)) {
            return;
        }
        String password = passwordEdit.getText().toString();
        String confirmPassword = confirmPasswordEdit.getText().toString();
        if (!password.equals(confirmPassword)) {
            Toast.makeText(getContext().getApplicationContext(), R.string.confirm_password_unequal_valid, Toast.LENGTH_SHORT).show();
            return;
        }
        ResetPwdHandler resetPwdHandler = new ResetPwdHandler(getActivity(), mobile, password, verifyCode);
        resetPwdHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    Toast.makeText(getContext().getApplicationContext(), R.string.forget_password_success, Toast.LENGTH_SHORT).show();
                    getActivity().setResult(Activity.RESULT_OK);
                    ((CommonActivity) getActivity()).finishCurActivity();
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        resetPwdHandler.execute();
    }

}
