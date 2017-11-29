package com.think.android.p2p.ui.account.personalinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.base.BaseApplication;
import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.AutoBackBtnActivity;
import com.think.android.p2p.ui.CommonActivity;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.MainActivity;
import com.think.android.p2p.ui.account.UserBaseInfoHandler;
import com.think.android.p2p.ui.account.bankcard.BindBankCardActivity;
import com.think.android.p2p.ui.account.invite.MyInviteActivity;

import org.json.JSONObject;

/**
 * 个人信息
 * Created by Think on 2017/10/10.
 */

public class PersonalInfoActivity extends AutoBackBtnActivity implements View.OnClickListener {

    TextView phoneNumText;

    LinearLayout realnameLayout;
    ImageView realnameStatusImg;
    TextView realnameStatusText;

    LinearLayout myInviteLayout;

    Button safeExitBtn;

    boolean realFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        addTopTitle(R.string.personal_info);

        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();

        queryStatus();
    }

    private void initViews() {
        UserInfoUtils userInfoUtils = new UserInfoUtils(this);

        phoneNumText = (TextView) findViewById(R.id.phone_num_text);
        phoneNumText.setText(userInfoUtils.getMaskMobile());

        safeExitBtn = (Button) findViewById(R.id.safe_exit_btn);
        safeExitBtn.setOnClickListener(this);

        realnameLayout = (LinearLayout) findViewById(R.id.realname_layout);
        realnameLayout.setOnClickListener(this);
        realnameStatusText = (TextView) findViewById(R.id.realname_status);
        realnameStatusImg = (ImageView) findViewById(R.id.realname_status_img);

        myInviteLayout = (LinearLayout) findViewById(R.id.my_invite_layout);
        myInviteLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.realname_layout:
                if (!realFlag) {
                    Intent intent1 = new Intent(this, BindBankCardActivity.class);
                    startActivityForResult(intent1, 1);
                }
                break;
            case R.id.safe_exit_btn:
                LogoutHandler logoutHandler = new LogoutHandler(this);
                logoutHandler.execute();
                UserInfoUtils userInfoUtils = new UserInfoUtils(this);
                userInfoUtils.clearUserInfo("safe");
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                ((BaseApplication) getApplication()).finishActivityButThis(MainActivity.class);
                break;
            case R.id.my_invite_layout:
                Intent intent3 = new Intent(this, MyInviteActivity.class);
                startActivity(intent3);
                break;
        }
    }

    private void queryStatus() {
        UserBaseInfoHandler userBaseInfoHandler = new UserBaseInfoHandler(this, true);
        userBaseInfoHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    if ("1".equals(JSONHelper.getStringValue(response, "cerFlag"))) {
                        realFlag = true;
                        realnameLayout.setClickable(false);
                        realnameStatusText.setText(R.string.setted);
                        realnameStatusText.setTextColor(getResources().getColor(R.color.status_set));
                        realnameStatusImg.setImageResource(R.mipmap.status_set);
                    } else {
                        realFlag = false;
                    }
                }
            }
        });
        userBaseInfoHandler.execute();
    }
}
