package com.amarsoft.support.android;


import com.amarsoft.android.AmarApplication;
import com.amarsoft.android.ErrorCodeManager;
import com.amarsoft.android.imp.AndroidHttpTransportSE;
import com.amarsoft.support.android.imp.DefaultTradeManager;
import com.amarsoft.support.android.model.BaseResponse;

import java.util.ArrayList;

/**
 * Created by Think on 2017/10/25.
 */

public abstract class TradeManager {

    public static String DEVICE_TYPE = "android";
    private static String DATA_TYPE = "FORM-DATA";
    private DataConvertor dataConvertor;
    private static TradeManager tradeManager = new DefaultTradeManager();

    public TradeManager() {
    }

    public static String getPushToken(AmarApplication app) {
        return app.PUSH_TOKEN;
    }

    public static void setPushToken(String pushToken, AmarApplication app) {
        app.PUSH_TOKEN = pushToken;
    }

    public static String getTransportEncryption(AmarApplication app) {
        return app.TRANSPORT_ENCRYPTION;
    }

    public static String getSessionKey(AmarApplication app) {
        return app.sessionKey;
    }

    public static void setSessionKey(String sessionKey, AmarApplication app) {
        app.sessionKey = sessionKey;
    }

    public static String getTransportKey(AmarApplication app) {
        return app.TRANSPORT_KEY;
    }

    public static String getTransportKeyIv(AmarApplication app) {
        return app.TRANSPORT_KEY_IV;
    }

    public static boolean isEncode(AmarApplication app) {
        return app.ENCODE;
    }

    public static String getDATA_TYPE(AmarApplication app) {
        return app.DATA_TYPE;
    }

    public static void setDATA_TYPE(String dATA_TYPE, AmarApplication app) {
        app.DATA_TYPE = dATA_TYPE;
    }

    public DataConvertor getDataConvertor() {
        return this.dataConvertor;
    }

    public void setdataConvertor(DataConvertor dataConvertor) {
        this.dataConvertor = dataConvertor;
    }

    public static String getSignKey(AmarApplication app) {
        return app.SIGN_KEY;
    }

    public static TradeManager getTradeManager() {
        return tradeManager;
    }

    public static synchronized void setTradeManager(TradeManager tradeManager_) {
        tradeManager = tradeManager_;
    }

    public BaseResponse executeRemoteService(String businessMethod, Object businessRequestObject, DataConvertor dataConvertor, AmarApplication app, int requestTimeout) throws Exception {
        return this.executeRemoteService(businessMethod, (String) null, businessRequestObject, dataConvertor, (String) null, app, requestTimeout);
    }

    public BaseResponse executeRemoteService(String businessMethod, Object businessRequestObject, DataConvertor dataConvertor, AmarApplication app) throws Exception {
        return this.executeRemoteService(businessMethod, (String) null, businessRequestObject, dataConvertor, (String) null, app, AndroidHttpTransportSE.DEFAULT_TIMEOUT);
    }

    public BaseResponse executeRemoteService(String businessMethod, String token, Object businessRequestObject, DataConvertor dataConvertor, AmarApplication app) throws Exception {
        return this.executeRemoteService(businessMethod, token, businessRequestObject, dataConvertor, (String) null, app, AndroidHttpTransportSE.DEFAULT_TIMEOUT);
    }

    public BaseResponse executeRemoteService(String businessMethod, String token, Object businessRequestObject, DataConvertor dataConvertor, String sessionKey, AmarApplication app, int requestTimeout) throws Exception {
        return this.executeRemoteService(businessMethod, token, businessRequestObject, dataConvertor, DATA_TYPE, sessionKey, app, requestTimeout);
    }

    public abstract BaseResponse executeRemoteService(String var1, String var2, Object var3, DataConvertor var4, String var5, String var6, AmarApplication var7, int var8) throws Exception;

    public static void iniTrade(String serverUrl, String signKey, String transportKey, String trasportKeyIV, boolean isEncode, String dataType, ErrorCodeManager errorCodeManager, AmarApplication app) {
        app.WEBSERVICE_URL = serverUrl;
        app.SIGN_KEY = signKey;
        app.TRANSPORT_KEY = transportKey;
        app.TRANSPORT_KEY_IV = trasportKeyIV;
        app.ENCODE = isEncode;
        app.DATA_TYPE = dataType;
        ErrorCodeManager.setManager(errorCodeManager, app);
    }

    public static void initTrade(String serverUrl, String signKey, String transportEncryption, String transportKey, String trasportKeyIV, boolean isEncode, String dataType, ErrorCodeManager errorCodeManager, AmarApplication app) {
        app.WEBSERVICE_URL = serverUrl;
        app.SIGN_KEY = signKey;
        app.TRANSPORT_KEY = transportKey;
        app.TRANSPORT_KEY_IV = trasportKeyIV;
        app.ENCODE = isEncode;
        app.DATA_TYPE = dataType;
        app.TRANSPORT_ENCRYPTION = transportEncryption;
        ErrorCodeManager.setManager(errorCodeManager, app);
    }

    public static synchronized void addUnsafeMethod(String method, AmarApplication app) {
        if (app.unsafeMethods == null) {
            app.unsafeMethods = new ArrayList();
        }

        if (!app.unsafeMethods.contains(method)) {
            app.unsafeMethods.add(method);
        }

    }
}
