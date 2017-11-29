package com.amarsoft.support.android;

import com.amarsoft.support.android.model.BaseRequest;
import com.amarsoft.support.android.model.BaseResponse;

/**
 * Created by Think on 2017/10/25.
 */

public interface TradeRunner {
    void setDataConvertor(DataConvertor var1);

    BaseResponse getResponseObject();

    void run(String var1, String var2, BaseRequest var3) throws Exception;
}
