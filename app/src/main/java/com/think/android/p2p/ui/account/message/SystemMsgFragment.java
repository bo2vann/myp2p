package com.think.android.p2p.ui.account.message;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.amarsoft.support.android.ui.CommonFragment;
import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.PullToRefreshFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 系统消息
 * Created by Think on 2017/10/14.
 */

public class SystemMsgFragment extends PullToRefreshFragment {

    private static final String MESSAGE_TYPE = "SYME";


    public SystemMsgFragment() {
        super();
        this.adapter = "com.think.android.p2p.ui.account.message.SystemMsgAdapter";
    }

    @Override
    protected void initViews() {

        if (getView() == null) return;
        super.initViews();

        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = (int) (position < id ? position : id);
                Intent intent = new Intent(getActivity(), MessageDetailActivity.class);
                intent.putExtra("data", dataList.get(index).toString());
                intent.putExtra("type", MESSAGE_TYPE);
                startActivity(intent);
            }
        });

        requestData(true);

    }

    @Override
    protected void requestData(boolean needLoadingDialog) {
        MessageListHandler messageListHandler = new MessageListHandler(getActivity(), MESSAGE_TYPE, needLoadingDialog);
        messageListHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    try {
                        adapterNotify(response.getJSONArray("list"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        messageListHandler.execute();
    }

}
