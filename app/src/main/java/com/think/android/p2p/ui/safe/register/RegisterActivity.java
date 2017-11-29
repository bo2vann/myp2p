package com.think.android.p2p.ui.safe.register;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amarsoft.support.android.imp.HttpRunner;
import com.amarsoft.support.android.utils.JSONHelper;
import com.android.volley.toolbox.NetworkImageView;
import com.think.android.p2p.R;
import com.think.android.p2p.base.BaseApplication;
import com.think.android.p2p.base.CommonValid;
import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.AutoBackBtnActivity;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.ProtocolActivity;
import com.think.android.p2p.ui.safe.gesturepwd.GestureToUnlockActivity;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 注册
 * Created by Think on 2017/10/10.
 */
public class RegisterActivity extends AutoBackBtnActivity implements View.OnClickListener, TextWatcher {

    private static final String TAG = "RegisterActivity";

    EditText phoneNumEdit;
    EditText graphicsCodeEdit;
    EditText msgCodeEdit;
    EditText passwordEdit;
    EditText inviteCodeEdit;

    ImageView graphicsImg;

    Button getPhoneCodeBtn;
    Button registerBtn;

    TextView protocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        addTopTitle(R.string.register);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void initView() {
        phoneNumEdit = (EditText) findViewById(R.id.phone_num_edit);
        graphicsCodeEdit = (EditText) findViewById(R.id.graphics_verify_code_edit);
        msgCodeEdit = (EditText) findViewById(R.id.msg_verify_code_edit);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
        inviteCodeEdit = (EditText) findViewById(R.id.invite_code_edit);

        graphicsImg = (ImageView) findViewById(R.id.graphics_img);
        graphicsImg.setOnClickListener(this);

        getPhoneCodeBtn = (Button) findViewById(R.id.get_phone_code_button);
        registerBtn = (Button) findViewById(R.id.register_button);

        getPhoneCodeBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);

        protocol = (TextView) findViewById(R.id.protocol);
        protocol.setOnClickListener(this);

        phoneNumEdit.addTextChangedListener(this);
        graphicsCodeEdit.addTextChangedListener(this);
        msgCodeEdit.addTextChangedListener(this);
        passwordEdit.addTextChangedListener(this);

        registerBtn.setEnabled(false);

        setGraphicsImg();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.get_phone_code_button:
                requestPhoneCode();
                break;
            case R.id.register_button:
                requestRegister();
                break;
            case R.id.graphics_img:
                setGraphicsImg();
                break;
            case R.id.protocol:
                Intent intent = new Intent(this, ProtocolActivity.class);
                intent.putExtra("url", "/loan/protocol/register.html");
                intent.putExtra("title", "平台用户服务协议");
                startActivity(intent);
                break;
        }
    }

    Bitmap bitmap;

    private void setGraphicsImg() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = ((BaseApplication) getApplication()).WEBSERVICE_URL + "/VerificationCode?rand=" + Math.random();
                    URL httpUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
                    if (HttpRunner.sessionid != null) {
                        conn.setRequestProperty("cookie", HttpRunner.sessionid);
                    }
                    conn.setConnectTimeout(6000);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    InputStream in = conn.getInputStream();

                    String cookieval = conn.getHeaderField("Set-Cookie");
                    if (cookieval != null) {
                        HttpRunner.sessionid = cookieval.substring(0, cookieval.indexOf(";"));//获取sessionid
                    }

                    bitmap = BitmapFactory.decodeStream(in);
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }
        });

        thread.start();

        graphicsImg.setImageBitmap(bitmap);
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
                    getPhoneCodeBtn.setText(String.format(
                            getResources().getString(R.string.second_resend),
                            sec));

                }
            } else if (msg.what == 2 && bitmap != null) {
                graphicsImg.setImageBitmap(bitmap);
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
        getPhoneCodeBtn.setText(R.string.get_verify_code);
    }

    /**
     * 设置按钮不可点击
     */
    private void setPhoneCodeBtnUnable() {
        sec = 60;
        getPhoneCodeBtn.setClickable(false);
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

    private void requestPhoneCode() {
        CommonValid commonValid = CommonValid.getInstance(this);
        if (!commonValid.check(phoneNumEdit, CommonValid.MOBILE_MODE, true, R.string.phone_num_hint, R.string.phone_valid_hint)) {
            return;
        }
        if (!commonValid.check(graphicsCodeEdit, CommonValid.OTHER_MODE, true, R.string.graphics_verify_code_hint, R.string.graphics_verify_code_hint)) {
            return;
        }
        String phone = phoneNumEdit.getText().toString();
        String graphicsCode = graphicsCodeEdit.getText().toString();

        SendRegisterCodeHandler sendRegisterCodeHandler = new SendRegisterCodeHandler(this, phone, graphicsCode);
        sendRegisterCodeHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    Toast.makeText(getApplicationContext(), R.string.msg_send_success, Toast.LENGTH_SHORT).show();
                    setPhoneCodeBtnUnable();
                } else {
                    Toast.makeText(getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                    setGraphicsImg();
                }
            }
        });
        sendRegisterCodeHandler.execute();
    }

    private void requestRegister() {
        CommonValid commonValid = CommonValid.getInstance(this);
        if (!commonValid.check(phoneNumEdit, CommonValid.MOBILE_MODE, true, R.string.phone_num_hint, R.string.phone_valid_hint)) {
            return;
        }
        if (!commonValid.check(graphicsCodeEdit, CommonValid.OTHER_MODE, true, R.string.graphics_verify_code_hint, R.string.graphics_verify_code_hint)) {
            return;
        }
        if (!commonValid.check(msgCodeEdit, CommonValid.OTHER_MODE, true, R.string.msg_verify_code_hint, R.string.msg_verify_code_hint)) {
            return;
        }
        if (!commonValid.check(passwordEdit, CommonValid.LOGON_PASSWORD_MODE, true, R.string.logon_password_hint, R.string.logon_password_valid_hint)) {
            return;
        }

        final String phone = phoneNumEdit.getText().toString();
        String graphicsCode = graphicsCodeEdit.getText().toString();
        String msgCode = msgCodeEdit.getText().toString();
        final String password = passwordEdit.getText().toString();
        String inviteCode = inviteCodeEdit.getText().toString();

        RegisterHandler registerHandler = new RegisterHandler(this, phone, password, msgCode, inviteCode);
        registerHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    UserInfoUtils userInfoUtils = new UserInfoUtils(getApplicationContext());
                    userInfoUtils.setUserName(JSONHelper.getStringValue(response, "userName"));
                    userInfoUtils.setMobile(phone);
//                    userInfoUtils.setUserPsw(password);
                    userInfoUtils.setRealName(JSONHelper.getStringValue(response, "realName"));
                    userInfoUtils.setNickName(JSONHelper.getStringValue(response, "nickName"));
                    userInfoUtils.setToken(JSONHelper.getStringValue(response, "token"));
                    userInfoUtils.setUserInfoObject(response);
                    Toast.makeText(getApplicationContext(), R.string.register_success, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finishCurActivity();
                    Intent intent = new Intent(RegisterActivity.this, GestureToUnlockActivity.class);
                    intent.putExtra("flag", GestureToUnlockActivity.FIRST_SET_STATUS);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        registerHandler.execute();
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
        if (!"".equals(phoneNumEdit.getText().toString()) && !"".equals(graphicsCodeEdit.getText().toString()) && !"".equals(passwordEdit.getText().toString()) && !"".equals(msgCodeEdit.getText().toString())) {
            registerBtn.setEnabled(true);
        } else {
            registerBtn.setEnabled(false);
        }
    }
}
