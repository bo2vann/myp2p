package com.think.android.p2p.ui.account.message;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.PullToRefreshFragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 个人消息
 * Created by Think on 2017/10/14.
 */

public class MineMsgFragment extends PullToRefreshFragment {

    private static final String MESSAGE_TYPE = "USME";

    public MineMsgFragment() {
        super();
        this.adapter = "com.think.android.p2p.ui.account.message.MineMsgAdapter";
    }

    @Override
    protected void initViews() {

        if (getView() == null) return;
        super.initViews();
        LinearLayout container = (LinearLayout) getView().findViewById(R.id.container);
        container.setBackgroundColor(getResources().getColor(android.R.color.white));

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
