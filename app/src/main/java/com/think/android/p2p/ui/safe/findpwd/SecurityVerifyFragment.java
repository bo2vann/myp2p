package com.think.android.p2p.ui.safe.findpwd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amarsoft.support.android.imp.HttpRunner;
import com.amarsoft.support.android.ui.CommonFragment;
import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.base.BaseApplication;
import com.think.android.p2p.base.CommonValid;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 找回登录密码-安全验证
 * Created by Think on 2017/10/10.
 */

public class SecurityVerifyFragment extends CommonFragment implements View.OnClickListener {

    EditText phoneNumEdit;
    EditText graphicsCodeEdit;
    EditText msgCodeEdit;

    ImageView graphicsImg;

    Button getPhoneCodeBtn;
    Button nextStepBtn;

    public SecurityVerifyFragment() {
        super(R.layout.fragment_security_verify);
    }

    @Override
    protected void initViews() {
        super.initViews();
        View view = getView();
        if (view == null) return;

        phoneNumEdit = (EditText) view.findViewById(R.id.phone_num_edit);
        graphicsCodeEdit = (EditText) view.findViewById(R.id.graphics_verify_code_edit);
        msgCodeEdit = (EditText) view.findViewById(R.id.msg_verify_code_edit);

        graphicsImg = (ImageView) view.findViewById(R.id.graphics_img);
        graphicsImg.setOnClickListener(this);

        getPhoneCodeBtn = (Button) view.findViewById(R.id.get_phone_code_button);
        nextStepBtn = (Button) view.findViewById(R.id.next_step_btn);

        getPhoneCodeBtn.setOnClickListener(this);
        nextStepBtn.setOnClickListener(this);

        setGraphicsImg();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.get_phone_code_button:
                requestPhoneCode();
                break;
            case R.id.next_step_btn:
                requestNext();
                break;
            case R.id.graphics_img:
                setGraphicsImg();
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

    Bitmap bitmap;

    private void setGraphicsImg() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = ((BaseApplication) getActivity().getApplication()).WEBSERVICE_URL + "/VerificationCode?rand=" + Math.random();
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

    /**
     * 请求短信码
     */
    private void requestPhoneCode() {
        CommonValid commonValid = CommonValid.getInstance(getContext());
        if (!commonValid.check(phoneNumEdit, CommonValid.MOBILE_MODE, true, R.string.phone_num_hint, R.string.phone_valid_hint)) {
            return;
        }
        if (!commonValid.check(graphicsCodeEdit, CommonValid.OTHER_MODE, true, R.string.graphics_verify_code_hint, R.string.graphics_verify_code_hint)) {
            return;
        }
        String phone = phoneNumEdit.getText().toString();
        String graphicsCode = graphicsCodeEdit.getText().toString();

        SendFindCodeHandler sendFindCodeHandler = new SendFindCodeHandler(getActivity(), phone, graphicsCode);
        sendFindCodeHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    Toast.makeText(getContext().getApplicationContext(), R.string.msg_send_success, Toast.LENGTH_SHORT).show();
                    setPhoneCodeBtnUnable();
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                    setGraphicsImg();
                }
            }
        });
        sendFindCodeHandler.execute();
    }

    /**
     * 请求下一步
     */
    private void requestNext() {
        CommonValid commonValid = CommonValid.getInstance(getContext());
        if (!commonValid.check(phoneNumEdit, CommonValid.MOBILE_MODE, true, R.string.phone_num_hint, R.string.phone_valid_hint)) {
            return;
        }
        if (!commonValid.check(graphicsCodeEdit, CommonValid.OTHER_MODE, true, R.string.graphics_verify_code_hint, R.string.graphics_verify_code_hint)) {
            return;
        }
        if (!commonValid.check(msgCodeEdit, CommonValid.OTHER_MODE, true, R.string.msg_verify_code_hint, R.string.msg_verify_code_hint)) {
            return;
        }

        final String phone = phoneNumEdit.getText().toString();
        String graphicsCode = graphicsCodeEdit.getText().toString();
        final String msgCode = msgCodeEdit.getText().toString();

        CheckFindCodeHandler checkFindCodeHandler = new CheckFindCodeHandler(getActivity(), phone, msgCode);
        checkFindCodeHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    toNextFragment(phone, msgCode);
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        checkFindCodeHandler.execute();
    }

    private void toNextFragment(String mobile, String verifyCode) {
        Bundle bundle = new Bundle();
        bundle.putString("mobile", mobile);
        bundle.putString("verifyCode", verifyCode);
        ResetPwdFragment resetPwdFragment =
                new ResetPwdFragment();
        resetPwdFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getActivity().
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,
                resetPwdFragment,
                ResetPwdFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
}
