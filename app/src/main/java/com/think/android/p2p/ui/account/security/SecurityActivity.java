package com.think.android.p2p.ui.account.security;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.AutoBackBtnActivity;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.account.UserBaseInfoHandler;
import com.think.android.p2p.ui.safe.gesturepwd.GestureToUnlockActivity;
import com.think.android.p2p.ui.views.PasswordDialog;

import org.json.JSONObject;

/**
 * 账户信息安全
 * Created by Think on 2017/10/10.
 */

public class SecurityActivity extends AutoBackBtnActivity implements View.OnClickListener {

    LinearLayout payPwdLayout;
    LinearLayout gesturePwdLayout;
    LinearLayout logonPwdLayout;

    ImageView payStatusImg;
    TextView payStatusText;
    ImageView gestureStatusImg;
    TextView gestureStatusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        addTopTitle(R.string.account_safe);

        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();

        queryStatus();
    }

    private void initViews() {
        payPwdLayout = (LinearLayout) findViewById(R.id.pay_password_layout);
        gesturePwdLayout = (LinearLayout) findViewById(R.id.gesture_password_layout);
        logonPwdLayout = (LinearLayout) findViewById(R.id.logon_password_layout);

        payPwdLayout.setOnClickListener(this);
        gesturePwdLayout.setOnClickListener(this);
        logonPwdLayout.setOnClickListener(this);

        payStatusText = (TextView) findViewById(R.id.pay_status);
        payStatusImg = (ImageView) findViewById(R.id.pay_status_img);
        gestureStatusText = (TextView) findViewById(R.id.gesture_status);
        gestureStatusImg = (ImageView) findViewById(R.id.gesture_status_img);

        UserInfoUtils userInfoUtils = new UserInfoUtils(this);
        if (userInfoUtils.isGestureSet()) {
            gestureStatusText.setText(R.string.setted);
            gestureStatusText.setTextColor(getResources().getColor(R.color.status_set));
            gestureStatusImg.setImageResource(R.mipmap.status_set);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.pay_password_layout:
                Intent intent1 = new Intent(this, PayPasswordSetActivity.class);
                startActivity(intent1);
                break;
            case R.id.gesture_password_layout:
                checkPwd();
                break;
            case R.id.logon_password_layout:
                Intent intent3 = new Intent(this, LogonPasswordModifyActivity.class);
                startActivity(intent3);
                break;
        }
    }

    private void checkPwd() {
        PasswordDialog passwordDialog = new PasswordDialog(this, new PasswordDialog.OnCheckListener() {
            @Override
            public void onCall() {
                Intent intent2 = new Intent(SecurityActivity.this, GestureToUnlockActivity.class);
                intent2.putExtra("flag", GestureToUnlockActivity.SET_STATUS);
                startActivity(intent2);
            }
        });
        passwordDialog.show();
    }

    private void queryStatus() {
        UserBaseInfoHandler userBaseInfoHandler = new UserBaseInfoHandler(this, true);
        userBaseInfoHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    if ("1".equals(JSONHelper.getStringValue(response, "payPassWordFlag"))) {
                        payStatusText.setText(R.string.setted);
                        payStatusText.setTextColor(getResources().getColor(R.color.status_set));
                        payStatusImg.setImageResource(R.mipmap.status_set);
                    }
                }
            }
        });
        userBaseInfoHandler.execute();
    }
}
