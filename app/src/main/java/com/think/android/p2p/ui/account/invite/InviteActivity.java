package com.think.android.p2p.ui.account.invite;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amarsoft.android.imp.AndroidHttpTransportSE;
import com.amarsoft.support.android.imp.HttpRunner;
import com.amarsoft.support.android.utils.JSONHelper;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.think.android.p2p.R;
import com.think.android.p2p.base.BaseApplication;
import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.AutoBackBtnActivity;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.account.UserBaseInfoHandler;
import com.think.android.p2p.ui.ads.VolleyBitmapLruCache;
import com.think.android.p2p.utils.BitmapUtil;

import org.json.JSONObject;
import org.kobjects.util.Util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 推荐给好友
 * Created by Think on 2017/10/22.
 */

public class InviteActivity extends AutoBackBtnActivity implements View.OnClickListener {

    private static final String APP_ID = "wxb770877a6afa5c45";
    private IWXAPI api;

    TextView tips;
    ImageView qrcode;
    String url;

    LinearLayout shareWX;
    LinearLayout sharePYQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invite);

        addTopTitle(R.string.recommend_to_friends);
        addTopRightTxtBtn(R.string.my_invite);

        tips = (TextView) findViewById(R.id.tips);
        qrcode = (ImageView) findViewById(R.id.qrcode);
        shareWX = (LinearLayout) findViewById(R.id.share_wx);
        shareWX.setOnClickListener(this);
        sharePYQ = (LinearLayout) findViewById(R.id.share_pyq);
        sharePYQ.setOnClickListener(this);
        requestURL();
        requestQRCode();

        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.registerApp(APP_ID);
    }

    protected void topRightTxtBtnClick() {
        Intent intent = new Intent(this, MyInviteActivity.class);
        startActivity(intent);
    }


    private void requestURL() {
        InviteUrlHandler inviteUrlHandler = new InviteUrlHandler(this);
        inviteUrlHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    tips.setText(JSONHelper.getStringValue(response, "text"));
                    url = JSONHelper.getStringValue(response, "url");
                } else {
                    Toast.makeText(getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        inviteUrlHandler.execute();
    }

    Bitmap bitmap;

    private void requestQRCode() {
        final UserInfoUtils userInfoUtils = new UserInfoUtils(this);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = ((BaseApplication) getApplication()).WEBSERVICE_URL + "/GetQrcode";
                    URL httpUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
                    if (HttpRunner.sessionid != null) {
                        conn.setRequestProperty("cookie", HttpRunner.sessionid);
                    }
                    conn.setRequestProperty("authorization", userInfoUtils.getToken());
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                    conn.setConnectTimeout(AndroidHttpTransportSE.DEFAULT_TIMEOUT);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    OutputStream os = conn.getOutputStream();
                    String request = "inviteCode=" + userInfoUtils.getMobile();
                    os.write(request.getBytes("utf-8"));
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
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2 && bitmap != null) {
                qrcode.setImageBitmap(bitmap);
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.share_wx:
                share(SendMessageToWX.Req.WXSceneSession);
                break;
            case R.id.share_pyq:
                share(SendMessageToWX.Req.WXSceneTimeline);
                break;
        }
    }

    private void share(int scene) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "龙商e融-银行级理财平台，助力实现财富增值！";
        msg.description = "龙湾农商行见证优选项目，年化收益5-7%，千元起投，等你来赚！";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        msg.thumbData = BitmapUtil.getBitmapByte(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "100";
        req.message = msg;
        req.scene = scene;
        api.sendReq(req);
    }
}
