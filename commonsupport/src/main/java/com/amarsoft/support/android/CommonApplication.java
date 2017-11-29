package com.amarsoft.support.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.amarsoft.android.AmarApplication;
import com.amarsoft.android.imp.DefaultErrorCodeManager;
import com.amarsoft.android.security.MD5;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * Created by Think on 2017/10/9.
 */

public class CommonApplication extends AmarApplication {
    protected SharedPreferences webRtPath;
    private Stack<Activity> activityList = new Stack<Activity>();

    public boolean exit = false;

    public void appExit() {
        try {
            exit = true;
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(getPackageName());
            System.exit(0);
        } catch (Exception localException) {
        }
    }

    /**
     * 判断栈中是否有某个ACtivity
     *
     * @param activity
     * @return
     */
    public boolean hasActivity(Class<?> cls) {
        boolean has = false;
        for (Activity newactivity : this.activityList)
            if (newactivity.getClass().equals(cls)) {
                has = true;
                break;
            }
        return has;
    }

    public void addActivity(Activity activity) {
        if (this.activityList == null) {
            this.activityList = new Stack<Activity>();
        }
        this.activityList.add(activity);
    }

    public Activity currentActivity() {
        Activity activity = (Activity) this.activityList.lastElement();
        return activity;
    }

    public void finishActivity() {
        Activity activity = (Activity) this.activityList.lastElement();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            this.activityList.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void finishActivity(Class<?> cls) {
        for (Activity activity : this.activityList)
            if (activity.getClass().equals(cls))
                finishActivity(activity);
    }

    public void finishAllActivity() {
        int i = 0;
        for (int size = this.activityList.size(); i < size; i++) {
            if (this.activityList.get(i) != null) {
                ((Activity) this.activityList.get(i)).finish();
            }
        }
        this.activityList.clear();
    }

    public void resetService() {
        this.webRtPath = getSharedPreferences("netpath", 0);
        WebRootPath = webRtPath.getString("WEBPATH", "").toString();
        WEBSERVICE_URL = (WebRootPath + "/TouchStoneService");
    }

    public void appStart(String signKey, String transportKey,
                         String transportKeyIV) {
        this.webRtPath = getSharedPreferences("netpath", 0);
        if ((this.webRtPath == null)
                || (this.webRtPath.getString("WEBPATH", "").equals(""))) {
            SharedPreferences.Editor editor = webRtPath.edit();
            editor.putString("WEBPATH", WebRootPath);
            editor.commit();
        }
        resetService();

        TradeManager.initTrade(WEBSERVICE_URL, signKey, null, transportKey,
                transportKeyIV, false, "json", new DefaultErrorCodeManager(),
                this);
        TradeManager.addUnsafeMethod("amarsoft.mobile.webservice.app.version",
                this);
    }

    /**
     * 将当前运行Activity栈中除ActivityName以外所有的Activity关闭
     *
     * @param ActivityName
     */
    public void finishActivityButThis(Class ActivityName) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(ActivityName)) {
                break;
            }
            finishActivity(activity);
        }
    }

    /**
     * 获取签名信息
     */
    public String checkSigneInfo() {
        Signature[] sigs = null;
        try {
            sigs = getPackageManager().getPackageInfo(getPackageName(), 64).signatures;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String sign = sigs[0].toCharsString();
        try {
            return MD5.getMD5String(sign);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 检查程序是否被调试
     *
     * @return
     */
    boolean isDebuggable() {
        boolean debuggable = false;
        PackageManager packageManager = getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager
                    .getApplicationInfo(getPackageName(), 0);
            debuggable = (0 != (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return debuggable;
    }

    /**
     * 获取APK打包文件classes.dex的Hash值
     *
     * @return
     */
    public String getAPPHash() {
        String apkPath = getPackageCodePath();
        try {
            ZipFile zipFile = new ZipFile(apkPath);
            ZipEntry dexentry = zipFile.getEntry("classes.dex");
            return dexentry.getCrc() + "";
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    /**
     * 获取APK的完整新信息
     *
     * @return
     */
    public String getAPKinfo() {
        //APK完整性校验
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5Util");
            byte[] bytes = new byte[8192];
            int byteCount;
            FileInputStream files = null;
            files = new FileInputStream(new File(getPackageCodePath()));
            while ((byteCount = files.read(bytes)) > 0) {
                messageDigest.update(bytes, 0, byteCount);
            }
            BigInteger bi = new BigInteger(1, messageDigest.digest());
            String md5 = bi.toString(16);
            files.close();
            return md5;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
}
