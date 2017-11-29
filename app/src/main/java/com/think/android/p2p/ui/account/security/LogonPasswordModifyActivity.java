package com.think.android.p2p.ui.account.security;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.base.CommonValid;
import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.AutoBackBtnActivity;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONObject;

/**
 * 修改登录密码
 * Created by Think on 2017/10/14.
 */

public class LogonPasswordModifyActivity extends AutoBackBtnActivity implements View.OnClickListener {

    EditText oldPasswordEdit;
    EditText passwordEdit;
    EditText confirmPasswordEdit;

    Button finishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_logon_password);
        addTopTitle(R.string.modify_logon_password);

        oldPasswordEdit = (EditText) findViewById(R.id.old_password_edit);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
        confirmPasswordEdit = (EditText) findViewById(R.id.confirm_password_edit);
        finishBtn = (Button) findViewById(R.id.finish_btn);
        finishBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.finish_btn:
                requestModify();
                break;
        }
    }

    private void requestModify() {
        CommonValid commonValid = CommonValid.getInstance(this);
        if (!commonValid.check(oldPasswordEdit, CommonValid.LOGON_PASSWORD_MODE, true, R.string.modify_logon_password_now_hint, R.string.logon_password_valid_hint)) {
            return;
        }
        if (!commonValid.check(passwordEdit, CommonValid.LOGON_PASSWORD_MODE, true, R.string.modify_logon_password_new_valid, R.string.logon_password_valid_hint)) {
            return;
        }
        if (!commonValid.check(confirmPasswordEdit, CommonValid.LOGON_PASSWORD_MODE, true, R.string.confirm_password_empty_valid, R.string.confirm_password_valid)) {
            return;
        }
        String oldPassword = oldPasswordEdit.getText().toString();
        final String newPassword = passwordEdit.getText().toString();
        String confirmPassword = confirmPasswordEdit.getText().toString();
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), R.string.confirm_password_unequal_valid, Toast.LENGTH_SHORT).show();
            return;
        }
        ModifyLogonPwdHandler modifyLogonPwdHandler = new ModifyLogonPwdHandler(this, oldPassword, newPassword);
        modifyLogonPwdHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    Toast.makeText(getApplicationContext(), R.string.modify_password_success, Toast.LENGTH_SHORT).show();
                    UserInfoUtils userInfoUtils = new UserInfoUtils(getApplicationContext());
//                    userInfoUtils.setUserPsw(newPassword);
                    setResult(RESULT_OK);
                    finishCurActivity();
                } else {
                    Toast.makeText(getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        modifyLogonPwdHandler.execute();
    }
}
