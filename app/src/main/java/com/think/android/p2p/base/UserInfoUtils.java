package com.think.android.p2p.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.midi.MidiOutputPort;

import org.json.JSONException;
import org.json.JSONObject;

import com.amarsoft.support.android.utils.DataEncryptionUtils;
import com.amarsoft.support.android.utils.MaskUtil;

/**
 * Created by Think on 2017/10/10.
 */

public class UserInfoUtils {
    private Context context;

    /**
     * 用户userId，即登录时的账号（用户名）
     */
    private String userName;
    /**
     * 用户手机号
     */
    private String mobile;
    /**
     * 用户的（真实）姓名，在登录成功之后返回
     */
    private String realName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 登录识别码
     */
    private String token;

    /**
     * 用户信息，包括用户的姓名、性别等基本信息
     */
    private JSONObject userInfoObject;
    private String logonData;

    private SharedPreferences userInfoPreferences;
    private SharedPreferences gesturePreferences;

    /**
     * 构造函数
     *
     * @param context 当前上下文
     */
    public UserInfoUtils(Context context) {
        this.context = context;
        userInfoPreferences = context.getSharedPreferences("UserInfo", 0);
        gesturePreferences = context.getSharedPreferences("Gesture", 0);
    }

    /**
     * 获得上次登录的毫秒数
     *
     * @return 登录毫秒数
     */
    public String getLogonData() {
        this.logonData = get("logonData");
        return this.logonData;
    }

    /**
     * 设置当前登录的毫秒数
     *
     * @param logonData 登录毫秒数
     */
    public void setLogonData(long logonData) {
        this.logonData = logonData + "";
        save("logonData", logonData + "");
    }

    /**
     * 设置用户登录用户名
     *
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
        save("UserName", userName);
    }

    /**
     * 获取用户登录用户名
     *
     * @return 用户名
     */
    public String getUserName() {
        this.userName = get("UserName");
        return this.userName;
    }

    /**
     * 设置用户手机号
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
        save("mobile", mobile);
    }

    /**
     * 获取手机号
     *
     * @return 手机号
     */
    public String getMobile() {
        this.mobile = get("mobile");
        return this.mobile;
    }

    public String getMaskMobile() {
        this.mobile = get("mobile");
        return MaskUtil.mobileMask(this.mobile);
    }

    /**
     * 设置用户（真实）姓名
     */
    public void setRealName(String realName) {
        this.realName = realName;
        save("RealName", realName);
    }

    /**
     * 获取用户的真实姓名
     *
     * @return 真实姓名
     */
    public String getRealName() {
        this.realName = get("RealName");
        return this.realName;
    }

    /**
     * 设置用户昵称
     *
     * @param nickName
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
        save("NickName", nickName);
    }

    /**
     * 获取用户昵称
     *
     * @return
     */
    public String getNickName() {
        this.nickName = get("NickName");
        return this.nickName;
    }

    public void setToken(String token) {
        this.token = token;
        save("Token", token);
    }

    public String getToken() {
        this.token = get("Token");
        return this.token;
    }

    /**
     * 设置用户信息
     *
     * @param userInfoObject 用户信息
     */
    public void setUserInfoObject(JSONObject userInfoObject) {
        this.userInfoObject = userInfoObject;
        save("UserInfoObject", userInfoObject.toString());// 转化成字符串
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public JSONObject getUserInfoObject() {
        try {
            this.userInfoObject = new JSONObject(get("UserInfoObject"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return userInfoObject;
    }

    /**
     * 设置用户账户密码
     *
     * @param userPsw 登录密码
     */
    public void setUserPsw(String userPsw) {
        if (userPsw == null || userPsw.trim().length() == 0)
            return;
        save("UserPSW", userPsw);
    }

    /**
     * 获取用户账户密码
     *
     * @return 用户密码
     */
    public String getUserPsw() {
        return get("UserPSW");
    }

    public void setUserGesturePsw(String gesturePsw) {
        if (gesturePsw == null || gesturePsw.trim().length() == 0)
            return;
        gestureSave("GesturePsw" + getMobile(), gesturePsw);
    }

    public boolean isGestureSet() {
        return !"".equals(gestureGet("GesturePsw" + getMobile()));
    }

    public boolean checkUserGesturePsw(String gesturePsw) {
        if (gesturePsw == null || gesturePsw.trim().length() == 0)
            return false;
        String saveGesture = gestureGet("GesturePsw" + getMobile());
        return gesturePsw.equals(saveGesture);
    }

    public void setLastTime() {
        save("lastTime", "" + System.currentTimeMillis());
    }

    public boolean needCheck() {
        String lastString = get("lastTime");
        if (lastString.equals("")) {
            return false;
        }
        long lastTime = Long.parseLong(lastString);
        long now = System.currentTimeMillis();
        if (!"".equals(getToken()) && isGestureSet() && now - lastTime > 30 * 60 * 1000) {
            return true;
        } else {
            return false;
        }
    }

    public void save(String key, String value) {
        if (key == null || key.trim().length() == 0)
            return;
        if (userInfoPreferences == null) {
            userInfoPreferences = context.getSharedPreferences("UserInfo", 0);
        }
        SharedPreferences.Editor editor = userInfoPreferences.edit();
        editor.remove(key);
        editor.putString(key, DataEncryptionUtils.encodeData(value));
        // editor.putString(key,value);
        editor.apply();
    }

    public String get(String key) {
        if (key == null || key.trim().length() == 0)
            return "";
        if (userInfoPreferences == null) {
            userInfoPreferences = context.getSharedPreferences("UserInfo", 0);
        }
        return DataEncryptionUtils.decodeDate(userInfoPreferences.getString(
                key, ""));
    }

    public void gestureSave(String key, String value) {
        if (key == null || key.trim().length() == 0)
            return;
        if (gesturePreferences == null) {
            gesturePreferences = context.getSharedPreferences("Gesture", 0);
        }
        SharedPreferences.Editor editor = gesturePreferences.edit();
        editor.remove(key);
        editor.putString(key, DataEncryptionUtils.encodeData(value));
        // editor.putString(key,value);
        editor.apply();
    }

    public String gestureGet(String key) {
        if (key == null || key.trim().length() == 0)
            return "";
        if (gesturePreferences == null) {
            gesturePreferences = context.getSharedPreferences("Gesture", 0);
        }
        return DataEncryptionUtils.decodeDate(gesturePreferences.getString(
                key, ""));
    }

    /**
     * 情况用户信息（退出程序或重新登录之前）
     */
    public void clearUserInfo(String cleartype) {
        this.token = this.nickName = this.realName = this.userName = this.mobile = null;
        this.userInfoObject = null;
        if (userInfoPreferences == null) {
            userInfoPreferences = context.getSharedPreferences("UserInfo", 0);
        }
        SharedPreferences.Editor editor = userInfoPreferences.edit();
        editor.remove("Token");
        if (cleartype.equals("exit")) {

        } else {
            editor.clear();
        }
        editor.apply();
    }

    /**
     * 判断用户当前是否登录
     *
     * @return true-目前已经登录，false-目前还未登录
     */
    public boolean isLogonSuccess() {

        if (getToken() != null && getToken().trim().length() != 0) {// token为空，则认为未登录；
            return true;
        }
        clearUserInfo("exit");
        return false;
    }

    /**
     * 登录产生界面状态更新
     *
     * @param update 是否更新状态
     */
    public void setLogonNeedUpdate(boolean update) {
        save("LogonNeedUpdate", update ? "YES" : "NO");
    }

    /**
     * 登录产生界面状态更新
     *
     * @return true-需要；false-不需要；
     */
    public boolean getLogonNeedUpdate() {
        return get("LogonNeedUpdate").equals("YES");
    }

}
