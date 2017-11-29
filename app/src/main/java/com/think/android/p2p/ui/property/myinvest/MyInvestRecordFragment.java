package com.think.android.p2p.ui.property.myinvest;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.PullToRefreshFragment;
import com.think.android.p2p.ui.property.UserProjectListHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Think on 2017/10/14.
 */

public class MyInvestRecordFragment extends PullToRefreshFragment {
    private static final String TAG = "MyInvestRecordFragment";

    public MyInvestRecordFragment() {
        super();
        this.adapter = "com.think.android.p2p.ui.property.myinvest.MyInvestRecordAdapter";
    }

    @Override
    protected void initViews() {

        if (getView() == null) return;
        super.initViews();
        dataListView.setDividerHeight(0);

        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = (int) (position < id ? position : id);
                Intent intent = new Intent(getActivity(),
                        MyInvestDetailActivity.class);
                JSONObject object = dataList.get(index);
                intent.putExtra("detail", object.toString());
                startActivity(intent);
            }
        });
        requestData(true);
    }

    @Override
    protected void requestData(boolean needLoadingDialog) {
        isLoading = true;
        UserProjectListHandler userProjectListHandler = new UserProjectListHandler(getActivity(), needLoadingDialog, "", curPage, maxLine);
        userProjectListHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
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
        userProjectListHandler.execute();
    }
}
