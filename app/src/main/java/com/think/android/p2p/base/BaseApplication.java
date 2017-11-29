package com.think.android.p2p.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.amarsoft.android.imp.AndroidHttpTransportSE;
import com.think.android.p2p.ui.safe.gesturepwd.GestureToUnlockActivity;

/**
 * Created by Think on 2017/10/9.
 */

public class BaseApplication extends com.amarsoft.support.android.CommonApplication {

    /**
     * 记录用户的信息
     */
    private UserInfoUtils userInfoUtils;

    /**
     * Activity执行onResume()方法时，通过该标志判断我的账户页面是否刷新页面，
     * true:需要刷新，false:不需要刷新
     */
    private boolean myRefreshFlag = false;

    /**
     * Activity执行onResume()方法时，通过该标志判断银行卡管理是否刷新页面
     * true:需要刷新，false:不需要刷新
     */
    private boolean bankRefreshFlag = false;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        initSystemParams();
        initEngineManager();

        CommonValid.init(getApplicationContext());
    }

    /**
     * 获取用户信息类
     *
     * @return
     */
    public UserInfoUtils getUserInfoUtils(Context context) {
        if (userInfoUtils == null) {
            userInfoUtils = new UserInfoUtils(context);
        }
        return userInfoUtils;
    }

    public void setMyRefreshFlag(boolean flag) {
        this.myRefreshFlag = flag;
    }

    public boolean getMyRefreshFlag() {
        return myRefreshFlag;
    }

    public void setBankRefreshFlag(boolean flag) {
        this.bankRefreshFlag = flag;
    }

    public boolean getBankRefreshFlag() {
        return bankRefreshFlag;
    }

    private void initEngineManager() {

    }

    /**
     * 初始化系统参数
     */
    private void initSystemParams() {
        this.RUN_MODE = "DEV";// 开发模式，不启用自定义的退出管理程序
//		this.LOG_MODE = "DEV";//开发模式，启用交易日志
//        this.LOG_MODE = "-";//非开发模式，不启用交易日志

        WebRootPath = "http://101.37.151.231:8080";
        SharedPreferences sp = getSharedPreferences("netpath", 0);
        sp.edit().putString("WEBPATH", WebRootPath).apply();

        AndroidHttpTransportSE.DEFAULT_TIMEOUT = CommonGlobalVariables.REQUEST_TIMEOUT;
        this.appStart("", "", "");
    }

    @Override
    public void appExit() {
        super.appExit();
    }


}
