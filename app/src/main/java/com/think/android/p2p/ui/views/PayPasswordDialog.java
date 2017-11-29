package com.think.android.p2p.ui.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.think.android.p2p.R;
import com.think.android.p2p.ui.account.security.PayPasswordSetActivity;

/**
 * 支付密码弹窗
 * Created by Think on 2017/10/18.
 */

public class PayPasswordDialog extends Dialog implements View.OnClickListener {

    Context context;

    EditText passwordEdit;

    LinearLayout pwdPanel;

    ImageView pw[];

    ImageButton close;
    Button forget;
    Button pay;

    OnPayListener onPayListener;

    public PayPasswordDialog(Context context, OnPayListener onPayListener) {
        super(context);
        this.context = context;
        this.onPayListener = onPayListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_check_pay_password);

        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        passwordEdit = (EditText) findViewById(R.id.password_edit);

        pwdPanel = (LinearLayout) findViewById(R.id.pwd_panel);

        pw = new ImageView[6];
        pw[0] = (ImageView) findViewById(R.id.pw1);
        pw[1] = (ImageView) findViewById(R.id.pw2);
        pw[2] = (ImageView) findViewById(R.id.pw3);
        pw[3] = (ImageView) findViewById(R.id.pw4);
        pw[4] = (ImageView) findViewById(R.id.pw5);
        pw[5] = (ImageView) findViewById(R.id.pw6);

        close = (ImageButton) findViewById(R.id.close);
        forget = (Button) findViewById(R.id.forget);
        pay = (Button) findViewById(R.id.pay);

        pwdPanel.setOnClickListener(this);
        close.setOnClickListener(this);
        forget.setOnClickListener(this);
        pay.setOnClickListener(this);
        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = passwordEdit.getText().toString().length();
                for (int i = 0; i < 6; i++) {
                    pw[i].setImageDrawable(null);
                }
                for (int i = 0; i < length; i++) {
                    pw[i].setImageResource(R.mipmap.dot);
                }
            }
        });
        showKeyboard();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.pwd_panel:
                showKeyboard();
                break;
            case R.id.close:
                hideKeyboard();
                dismiss();
                break;
            case R.id.forget:
                hideKeyboard();
                dismiss();
                Intent intent = new Intent(getContext(), PayPasswordSetActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.pay:
                String pwd = passwordEdit.getText().toString();
                if (pwd.length() < 6) {
                    Toast.makeText(context, R.string.pwd_toast, Toast.LENGTH_SHORT).show();
                    return;
                }
                hideKeyboard();
                if (onPayListener != null) {
                    onPayListener.onCall(pwd);
                }
                break;
        }
    }

    private void showKeyboard() {
        passwordEdit.setFocusableInTouchMode(true);
        passwordEdit.setFocusable(true);
        passwordEdit.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(passwordEdit, InputMethodManager.SHOW_FORCED);
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public interface OnPayListener {
        public void onCall(String pwd);
    }
}
