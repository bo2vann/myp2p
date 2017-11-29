package com.think.android.p2p.ui.property.dueproperty;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
 * Created by Think on 2017/10/15.
 */

public class DuePropertyFragment extends PullToRefreshFragment {

    String currentStatus;

    TextView propertyTotalText;
    TextView duePrincipalText;
    TextView dueProfitText;

    private static final String TAG = "DuePropertyFragment";

    public DuePropertyFragment() {
        super();
        this.adapter = "com.think.android.p2p.ui.property.dueproperty.DuePropertyAdapter";
    }

    @Override
    protected void initViews() {
        currentStatus = UserProjectListHandler.UNDUE_PROPERTY;
        if (getView() == null) return;
        super.initViews();

    }

    @Override
    protected void initListView() {
        View header = getActivity().getLayoutInflater().inflate(R.layout.property_head, null);
        dataListView.addHeaderView(header);

        propertyTotalText = (TextView) header.findViewById(R.id.property_total_text);
        duePrincipalText = (TextView) header.findViewById(R.id.due_principal_text);
        dueProfitText = (TextView) header.findViewById(R.id.due_profit_text);

        RadioGroup propertyGroup = (RadioGroup) header.findViewById(R.id.property_group);
        ;
        propertyGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.undue_property_radio:
                        currentStatus = UserProjectListHandler.UNDUE_PROPERTY;
                        if (pullToRefreshAdapter != null)
                            ((DuePropertyAdapter) pullToRefreshAdapter).setStatus(currentStatus);
                        refresh();
                        break;
                    case R.id.dued_property_radio:
                        currentStatus = UserProjectListHandler.DUED_PROPERTY;
                        if (pullToRefreshAdapter != null)
                            ((DuePropertyAdapter) pullToRefreshAdapter).setStatus(currentStatus);
                        refresh();
                        break;
                }
            }
        });

        requestProperty();

        super.initListView();

        ((DuePropertyAdapter) pullToRefreshAdapter).setStatus(currentStatus);

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
        UserProjectListHandler userProjectListHandler = new UserProjectListHandler(getActivity(), needLoadingDialog, currentStatus, curPage, maxLine);
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
                    propertyTotalText.setText(JSONHelper.getStringValue(response, "totalAmount"));
                    duePrincipalText.setText(JSONHelper.getStringValue(response, "waitBackPrincipal"));
                    dueProfitText.setText(JSONHelper.getStringValue(response, "waitBackInterest"));
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        propertyHandler.execute();
    }
}
