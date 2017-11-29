package com.think.android.p2p.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.base.CommonValid;
import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.safe.gesturepwd.CheckPasswordHandler;

import org.json.JSONObject;

/**
 * Created by Think on 2017/11/23.
 */

public class PasswordDialog extends Dialog implements View.OnClickListener {

    ImageButton close;
    TextView mobile;
    EditText password;
    Button cancel;
    Button confirm;

    OnCheckListener onCheckListener;

    public PasswordDialog(Context context, OnCheckListener onCheckListener) {
        super(context);
        this.onCheckListener = onCheckListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_check_password);

        initView();
    }

    private void initView() {
        close = (ImageButton) findViewById(R.id.close);
        mobile = (TextView) findViewById(R.id.mobile);

        UserInfoUtils userInfoUtils = new UserInfoUtils(getContext());
        mobile.setText(userInfoUtils.getMaskMobile());
        password = (EditText) findViewById(R.id.password);
        cancel = (Button) findViewById(R.id.cancel);
        confirm = (Button) findViewById(R.id.confirm);
        close.setOnClickListener(this);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.close:
            case R.id.cancel:
                dismiss();
                break;
            case R.id.confirm:
                checkPwd();
                break;
        }
    }

    private void checkPwd() {
        CommonValid commonValid = CommonValid.getInstance(getContext());
        String pwd = password.getText().toString();
        if (!commonValid.check(password, CommonValid.LOGON_PASSWORD_MODE, true, R.string.pls_input_your_pwd, R.string.logon_password_valid_hint)) {
            return;
        }
        CheckPasswordHandler checkPasswordHandler = new CheckPasswordHandler(getContext(), pwd);
        checkPasswordHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    if (onCheckListener != null) {
                        onCheckListener.onCall();
                    }
                    dismiss();
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        checkPasswordHandler.execute();
    }

    public interface OnCheckListener {
        public void onCall();
    }
}
