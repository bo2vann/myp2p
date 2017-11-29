package com.think.android.p2p.ui.property.transrecord;

import android.widget.Toast;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.CommonActivity;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.PullToRefreshFragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 交易记录
 * Created by Think on 2017/10/15.
 */

public class TransRecordFragment extends PullToRefreshFragment {

    public TransRecordFragment() {
        super();
        this.adapter = "com.think.android.p2p.ui.property.transrecord.TransRecordAdapter";
    }

    @Override
    protected void initViews() {

        if (getView() == null) return;
        super.initViews();

        dataListView.setDividerHeight(CommonActivity.dpToPx(getActivity(), 1));
        dataListView.setDivider(getResources().getDrawable(R.drawable.list_divider));

        requestData(true);
    }

    @Override
    protected void requestData(boolean needLoadingDialog) {
        isLoading = true;
        TransListHandler transListHandler = new TransListHandler(getActivity(), needLoadingDialog, "SYLX", "", curPage, maxLine);
        transListHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                isLoading = false;
                if ("SUCCESS".equals(object)) {
                    try {
                        adapterNotify(response.getJSONArray("list"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                    stopRefresh();
                }
            }
        });
        transListHandler.execute();
    }
}
