package com.think.android.p2p.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.amarsoft.support.android.ui.CommonActivity;
import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.home.BannerHandler;
import com.think.android.p2p.ui.safe.gesturepwd.GestureToUnlockActivity;
import com.think.android.p2p.utils.VersionUtils;

import org.json.JSONObject;

/**
 * Loading页
 * Created by Think on 2017/11/20.
 */

public class LoadingActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        queryBanner();
    }

    private void queryBanner() {
        BannerHandler bannerHandler = new BannerHandler(this);
        bannerHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    SharedPreferences sp = getSharedPreferences("Advertise", 0);
                    sp.edit().putString("ads", response == null ? "" : response.toString()).apply();
                } else {
                    Toast.makeText(getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
                queryVersion();
            }
        });
        bannerHandler.execute();
    }

    private void queryVersion() {
        QueryVersionHandler queryVersionHandler = new QueryVersionHandler(this);
        queryVersionHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    try {
                        int status = VersionUtils.compareWithVersionName(LoadingActivity.this, JSONHelper.getStringValue(response, "minVersion"), JSONHelper.getStringValue(response, "maxVersion"));
                        String url = JSONHelper.getStringValue(response, "url");
                        SharedPreferences sp = getSharedPreferences("Version", 0);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("status", status);
                        editor.putString("url", url);
                        editor.apply();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
                toNextActivity();
            }
        });
        queryVersionHandler.execute();
    }

    private void toNextActivity() {
        SharedPreferences pref = getSharedPreferences("loading", 0);
        Boolean isFirstIn = pref.getBoolean("isFirstIn", true);
        UserInfoUtils userInfoUtils = new UserInfoUtils(this);
        userInfoUtils.setLastTime();

        if (isFirstIn) {
            // 首次进入
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
        } else if (!"".equals(userInfoUtils.getToken())) {
            // 已登录
            if (!userInfoUtils.isGestureSet()) {
                // 未设置手势密码
                Intent intent = new Intent(this, GestureToUnlockActivity.class);
                intent.putExtra("flag", GestureToUnlockActivity.FIRST_SET_STATUS);
                intent.putExtra("way", "loading");
                startActivity(intent);
            } else {
                // 已设置手势密码
                Intent intent = new Intent(this, GestureToUnlockActivity.class);
                intent.putExtra("flag", GestureToUnlockActivity.CHECK_STATUS);
                intent.putExtra("way", "loading");
                startActivity(intent);
            }
        } else {
            // 未登录
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        finishCurActivity();
    }
}