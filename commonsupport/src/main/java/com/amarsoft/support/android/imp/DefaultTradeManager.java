package com.amarsoft.support.android.imp;

import com.amarsoft.android.AmarApplication;
import com.amarsoft.android.ErrorCodeManager;
import com.amarsoft.support.android.CommonApplication;
import com.amarsoft.support.android.DataConvertor;
import com.amarsoft.support.android.TradeManager;
import com.amarsoft.support.android.TradeRunner;
import com.amarsoft.support.android.model.BaseRequest;
import com.amarsoft.support.android.model.BaseResponse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Think on 2017/10/25.
 */

public class DefaultTradeManager extends TradeManager {
    public DefaultTradeManager() {
    }


    protected TradeRunner getTradeRunner(AmarApplication app, int requestTimeout) {
        return (TradeRunner) new HttpRunner(app, requestTimeout);
    }

    @Override
    public BaseResponse executeRemoteService(String businessMethod, String token, Object businessRequestObject, DataConvertor dataConvertor, String dataType, String sessionKey, AmarApplication app, int requestTimeout) throws Exception {
        TradeRunner handler = this.getTradeRunner(app, requestTimeout);
        BaseRequest request = new BaseRequest();
        request.setToken(token);
        request.setBusinessRequestObject(businessRequestObject);
        handler.setDataConvertor(dataConvertor);

        try {
            handler.run(businessMethod, dataType, request);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(ErrorCodeManager.getManager(app).getErrorTitle("url.error"));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            throw new Exception(ErrorCodeManager.getManager(app).getErrorTitle("request.invalid"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("服务执行失败，请检查服务是否可用");
        }

        return handler.getResponseObject();
    }
}
