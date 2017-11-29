package com.amarsoft.support.android.utils;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Think on 2017/11/21.
 */

public class MaskUtil {

    // 手机号掩码
    public static String mobileMask(String mobile) {
        if (mobile == null || mobile.length() == 0) return "";
        if (mobile.length() == 11) {
            String v = mobile.substring(0, 3);
            String end = mobile.substring(mobile.length() - 4);
            return v + StringUtils.repeat("*", 4) + end;
        } else
            return "";
    }

    // 身份证掩码
    public static String maskCertId(String certId) {
        if (certId == null || certId.length() == 0) return "";
        if (certId.length() == 18) {
            String v = certId.substring(0, 4);
            String end = certId.substring(certId.length() - 4);
            return v + StringUtils.repeat("*", 10) + end;
        } else
            return "";
    }

    public static String maskCardNo(String cardNo) {
        if (cardNo == null || cardNo.length() == 0) return "";
        else {
            String end = cardNo.substring(cardNo.length() - 4);
            return StringUtils.repeat("*", 4) + "\u3000" + StringUtils.repeat("*", 4) + "\u3000" + StringUtils.repeat("*", 4) + "\u3000" + end;
        }
    }
}
