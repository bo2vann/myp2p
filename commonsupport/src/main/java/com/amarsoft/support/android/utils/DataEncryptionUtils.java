package com.amarsoft.support.android.utils;

import com.amarsoft.android.security.Base64;
import com.amarsoft.android.security.imp.Des3Encryption;

/**
 * DES3加密解密工具类
 *
 */

public class DataEncryptionUtils {

    private static String sTransportKeyIV = "32410759";
    private static String sTransportKey = "androidForAmarsoftAndQSP2P@";

    /**
     * 解码
     *
     * @param str 待解码
     * @return 解码返回
     */
    public static String decodeDate(String str) {
        // EncryptAction ea = createEncrypAction(transportEncryption);
        try {
            Des3Encryption ea = new Des3Encryption();
            ea.setDecryptKey(sTransportKey.getBytes("UTF-8"));
            ea.setIV(sTransportKeyIV);
            // 解密
            byte[] bytes = Base64.decode(str);
            byte[] dedata = ea.decrypt(bytes);
            // 解压缩
            str = new String(dedata, "UTF-8");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return str;
    }

    /**
     * 数据编码
     *
     * @param str 待编码数据
     * @return 编码完成数据
     */
    public static String encodeData(String str) {
        // 加密模式下：需要先将数据进行加密和压缩
        try {
            // 压缩
            byte[] bytes = str.getBytes("UTF-8");
            Des3Encryption ea = new Des3Encryption();
            ea.setEncryptKey(sTransportKey.getBytes("UTF-8"));
            ea.setIV(sTransportKeyIV);
            return Base64.encode(ea.encrypt(bytes));
        } catch (Exception e) {
            return "response.unknown";
        }
    }
}
