package com.think.android.p2p.ui.safe.logon;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.think.android.p2p.R;
import com.think.android.p2p.base.CommonValid;
import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.AutoBackBtnActivity;
import com.think.android.p2p.ui.MainActivity;
import com.think.android.p2p.ui.safe.findpwd.FindPwdActivity;
import com.think.android.p2p.ui.safe.gesturepwd.GestureToUnlockActivity;
import com.think.android.p2p.ui.safe.register.RegisterActivity;

/**
 * 登录
 * Created by Think on 2017/10/10.
 */
public class LogonActivity extends AutoBackBtnActivity implements View.OnClickListener, TextWatcher {

    EditText phoneNumEdit;
    EditText passwordEdit;
    Button logonButton;
    Button findPasswordButton;

    boolean restart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restart = getIntent().getBooleanExtra("restart", false);

        setContentView(R.layout.activity_logon);
        clearTopTitle();
        addTopRightTxtBtn(R.string.register);
        setBackTitle(R.string.close);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        UserInfoUtils userInfoUtils = new UserInfoUtils(this);
        if (!"".equals(userInfoUtils.getToken())) {
            finishCurActivity();
        }
    }

    /**
     * 初始化界面
     */
    private void initView() {
        phoneNumEdit = (EditText) findViewById(R.id.phone_num_edit);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
        logonButton = (Button) findViewById(R.id.logon_button);

        findPasswordButton = (Button) findViewById(R.id.find_password_button);

        logonButton.setOnClickListener(this);
        findPasswordButton.setOnClickListener(this);

        UserInfoUtils userInfoUtils = new UserInfoUtils(this);
        String mobile = userInfoUtils.getMobile();
        if (!"".equals(mobile)) {
            phoneNumEdit.setText(mobile);
        }

        phoneNumEdit.addTextChangedListener(this);
        passwordEdit.addTextChangedListener(this);
        logonButton.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.logon_button:
                logon();
                break;
            case R.id.find_password_button:
                Intent intent1 = new Intent(this, FindPwdActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    protected void keyBackDown() {
        if (!restart)
            finishCurActivity();
        else {
            app.finishAllActivity();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void topRightTxtBtnClick() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void logon() {
        CommonValid commonValid = CommonValid.getInstance(this);

        String phone = phoneNumEdit.getText().toString();
        if (!commonValid.check(phoneNumEdit, CommonValid.MOBILE_MODE, true, R.string.phone_num_hint, R.string.phone_valid_hint)) {
            return;
        }
        String password = passwordEdit.getText().toString();
        if (!commonValid.check(passwordEdit, CommonValid.LOGON_PASSWORD_MODE, true, R.string.logon_password_hint, R.string.logon_password_valid_hint)) {
            return;
        }

        LogonHandler logonHandler = new LogonHandler(this, phone, password);
        logonHandler.setLogonListener(new LogonHandler.LogonListener() {
            @Override
            public void logonCallBack(int resultCode) {
                if (resultCode == 0) {
                    UserInfoUtils userInfoUtils = new UserInfoUtils(LogonActivity.this);
                    userInfoUtils.setLogonData(System.currentTimeMillis());
//                    setResult(0);
                    if (!userInfoUtils.isGestureSet()) {
                        Intent intent = new Intent(LogonActivity.this, GestureToUnlockActivity.class);
                        intent.putExtra("way", getIntent().getStringExtra("way"));
                        intent.putExtra("flag", GestureToUnlockActivity.FIRST_SET_STATUS);
                        startActivity(intent);
                    } else if (!restart) {
                        Intent intent = new Intent(LogonActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    LogonActivity.this.finishCurActivity();
                }
            }
        });
        logonHandler.execute();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        changeBtnStatus();
    }

    private void changeBtnStatus() {
        if (!"".equals(phoneNumEdit.getText().toString()) && !"".equals(passwordEdit.getText().toString())) {
            logonButton.setEnabled(true);
        } else {
            logonButton.setEnabled(false);
        }
    }
}
