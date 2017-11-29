package com.think.android.p2p.utils;

import android.content.Context;

/**
 * Created by Think on 2017/11/14.
 */

public class DpPxUtils {
    /**
     * dp px转换
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dpToPx(Context context, int dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }
}
