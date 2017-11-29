package com.think.android.p2p.ui.account.invite;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amarsoft.support.android.utils.JSONHelper;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.AutoBackBtnActivity;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.PullToRefreshAdapter;
import com.think.android.p2p.ui.account.UserBaseInfoHandler;
import com.think.android.p2p.ui.utils.PullRefreshUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 我的邀请
 * Created by Think on 2017/10/22.
 */

public class MyInviteActivity extends AutoBackBtnActivity {

    TextView codeText;
    TextView numInviteText;
    TextView awardAmountText;

    PullToRefreshListView pullToRefreshListView;
    PullRefreshUtil pullRefreshUtil;

    protected ListView dataListView;
    protected MyInviteAdapter myInviteAdapter;
    protected ArrayList<JSONObject> dataList;

    protected boolean isAllData;
    protected boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_invite);

        View header = getLayoutInflater().inflate(R.layout.my_invite_header, null);

        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.list);

        addTopTitle(R.string.my_invite);

        codeText = (TextView) header.findViewById(R.id.code_text);
        numInviteText = (TextView) header.findViewById(R.id.num_invite_text);
        awardAmountText = (TextView) header.findViewById(R.id.award_amount_text);

        dataListView = pullToRefreshListView.getRefreshableView();
        dataListView.addHeaderView(header);
        dataListView.setDivider(null);

        pullRefreshUtil = new PullRefreshUtil(this, pullToRefreshListView) {
            @Override
            public void refreshList() {
                refresh();
            }

            @Override
            public void loadMore() {
                return;
            }
        };

        myInviteAdapter = new MyInviteAdapter(this, dataList);
        dataListView.setAdapter(myInviteAdapter);

        requestData(true);
    }

    protected void refresh() {
        isAllData = false;
        dataList.clear();
        requestData(false);
    }

    /**
     * 停止刷新动画
     */
    protected void stopRefresh() {
        if (pullToRefreshListView.isRefreshing()) {
            pullToRefreshListView.onRefreshComplete();
        }
    }

    private void requestData(boolean needLoadingDialog) {
        isLoading = true;
        UserBaseInfoHandler userBaseInfoHandler = new UserBaseInfoHandler(this, needLoadingDialog);
        userBaseInfoHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                isLoading = false;
                if ("SUCCESS".equals(object)) {
                    codeText.setText(JSONHelper.getStringValue(response, "myInviteCode"));
                    numInviteText.setText(JSONHelper.getStringValue(response, "inviteCount") + "人");
                    awardAmountText.setText(JSONHelper.getStringValue(response, "inviteAmount") + "元");
                    try {
                        adapterNotify(response.getJSONArray("list"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        userBaseInfoHandler.execute();
    }

    protected void adapterNotify(JSONArray jsonArray) {
        pullToRefreshListView.setVisibility(View.VISIBLE);
        if (jsonArray == null) {
            isAllData = true;
            stopRefresh();
            return;
        }
        if (dataList == null) {
            dataList = new ArrayList<>();
        }

        dataList.clear();
        isAllData = true;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                dataList.add(jsonArray.getJSONObject(i));
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        stopRefresh();
        myInviteAdapter.notifyDataSetChanged();
    }
}
