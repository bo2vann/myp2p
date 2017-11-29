package com.think.android.p2p.ui.views;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.think.android.p2p.R;

/**
 * Created by Think on 2017/10/14.
 */

public class NoDataView {
    public static View getEmptyView(Context context) {

        return LinearLayout.inflate(context, R.layout.view_empty, null);
    }
}
