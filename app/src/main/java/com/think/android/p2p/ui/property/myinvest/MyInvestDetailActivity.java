package com.think.android.p2p.ui.property.myinvest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.AutoBackBtnActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Think on 2017/11/14.
 */

public class MyInvestDetailActivity extends AutoBackBtnActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invest_detail);

        addTopTitle(R.string.invest_project_detail);
        JSONObject detail = null;
        try {
            detail = new JSONObject(getIntent().getStringExtra("detail"));
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (detail == null) {
                finishCurActivity();
                return;
            }
        }

        TextView projectNameText = (TextView) findViewById(R.id.project_name_text);
        projectNameText.setText(JSONHelper.getStringValue(detail, "projectName") + "【" + JSONHelper.getStringValue(detail, "projectNo") + "】");
        TextView investStatusText = (TextView) findViewById(R.id.invest_status_text);
        investStatusText.setText(JSONHelper.getStringValue(detail, "status"));
        TextView investAmountText = (TextView) findViewById(R.id.invest_amount_text);
        investAmountText.setText(JSONHelper.getStringValue(detail, "invAmount") + "元");
        TextView expectedIncomeText = (TextView) findViewById(R.id.expected_income_text);
        expectedIncomeText.setText(JSONHelper.getStringValue(detail, "incomeAmount") + "元");
        TextView investTimeText = (TextView) findViewById(R.id.invest_time_text);
        investTimeText.setText(JSONHelper.getStringValue(detail, "invDate"));
        TextView financingAmountText = (TextView) findViewById(R.id.financing_amount_text);
        financingAmountText.setText(JSONHelper.getStringValue(detail, "totalAmt"));
        TextView annualizedReturnText = (TextView) findViewById(R.id.annualized_return_text);
        annualizedReturnText.setText(JSONHelper.getStringValue(detail, "invRate"));
        TextView interestDateText = (TextView) findViewById(R.id.interest_date_text);
        interestDateText.setText(JSONHelper.getStringValue(detail, "firstIntDate"));
        TextView dueDateText = (TextView) findViewById(R.id.due_date_text);
        dueDateText.setText(JSONHelper.getStringValue(detail, "invOverDate"));

        final String url = JSONHelper.getStringValue(detail, "agreeUrl");
        TextView protocol = (TextView) findViewById(R.id.protocol);
        if (url == null || "".equals(url)) {
            protocol.setVisibility(View.GONE);
        }
        protocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

}
