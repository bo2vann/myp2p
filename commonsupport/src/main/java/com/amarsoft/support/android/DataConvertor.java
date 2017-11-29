package com.amarsoft.support.android;

import com.amarsoft.support.android.model.BaseRequest;
import com.amarsoft.support.android.model.BaseResponse;

/**
 * Created by Think on 2017/10/25.
 */

public interface DataConvertor {
    String convertToSignature(BaseRequest val);

    String convertToRequestString(BaseRequest var1);

    BaseResponse convertResultToResponse(String var1);
}
