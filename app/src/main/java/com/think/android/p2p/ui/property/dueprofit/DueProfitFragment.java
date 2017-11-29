package com.think.android.p2p.ui.property.dueprofit;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.PullToRefreshFragment;
import com.think.android.p2p.ui.property.PropertyHandler;
import com.think.android.p2p.ui.property.UserProjectListHandler;
import com.think.android.p2p.ui.property.myinvest.MyInvestDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 待收收益
 * Created by Think on 2017/10/15.
 */

public class DueProfitFragment extends PullToRefreshFragment {

    private static final String TAG = "DueProfitFragment";

    TextView profitTotalText;
    TextView duedProfitText;
    TextView undueProfitText;

    public DueProfitFragment() {
        super();
        this.adapter = "com.think.android.p2p.ui.property.dueprofit.DueProfitAdapter";
    }

    @Override
    protected void initViews() {

        if (getView() == null) return;
        super.initViews();


    }

    @Override
    protected void initListView() {

        View header = getActivity().getLayoutInflater().inflate(R.layout.due_profit_head, null);
        dataListView.addHeaderView(header);

        profitTotalText = (TextView) header.findViewById(R.id.profit_total_text);
        duedProfitText = (TextView) header.findViewById(R.id.dued_profit_text);
        undueProfitText = (TextView) header.findViewById(R.id.due_profit_text);

        requestProperty();
        super.initListView();

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
        UserProjectListHandler userProjectListHandler = new UserProjectListHandler(getActivity(), needLoadingDialog, "TZCC", curPage, maxLine);
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

    private void requestProperty() {
        PropertyHandler propertyHandler = new PropertyHandler(getActivity());
        propertyHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    profitTotalText.setText(JSONHelper.getStringValue(response, "totalIncome"));
                    duedProfitText.setText(JSONHelper.getStringValue(response, "takeBackInterest"));
                    undueProfitText.setText(JSONHelper.getStringValue(response, "waitBackInterest"));
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        propertyHandler.execute();
    }
}
