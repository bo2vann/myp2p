package com.think.android.p2p.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.amarsoft.android.ui.VersionManager;
import com.think.android.p2p.ui.LoadingActivity;

/**
 * Created by Think on 2017/11/24.
 */

public class VersionUtils {
    public static final int SHOULD_UPDATE = 0x10;
    public static final int NEED_UPDATE = 0x20;
    public static final int NO_NEED_UPDATE = 0x30;

    public static int compareWithVersionCode(Context context, int lowCode, int highCode) {
        int current = VersionManager.getVersionManager(context.getPackageManager(), context.getPackageName()).getVersionCode();
        if (current < lowCode) return SHOULD_UPDATE;
        else if (current < highCode) return NEED_UPDATE;
        else return NO_NEED_UPDATE;
    }

    public static int compareWithVersionName(Context context, String lowName, String highName) throws Exception {
        String current = VersionManager.getVersionManager(context.getPackageManager(), context.getPackageName()).getVersionName();
        if (compareVersionName(current, lowName)) return SHOULD_UPDATE;
        else if (compareVersionName(current, highName)) return NEED_UPDATE;
        else return NO_NEED_UPDATE;
    }

    public static boolean compareVersionName(String current, String target) throws Exception {
        current = current.toLowerCase().replaceAll("v", "");
        target = target.toLowerCase().replaceAll("v", "");
        String[] currentList = current.split("\\.");
        String[] targetList = target.split("\\.");
        int length = currentList.length < targetList.length ? currentList.length : targetList.length;
        for (int i = 0; i < length; i++) {
            if (Integer.parseInt(currentList[i]) < Integer.parseInt(targetList[i])) {
                return true;
            } else if (Integer.parseInt(currentList[i]) > Integer.parseInt(targetList[i])) {
                return false;
            }
        }
        if (currentList.length < targetList.length) return true;
        else return false;
    }
}
