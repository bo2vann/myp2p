package com.think.android.p2p.ui.account.about;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.AutoBackBtnActivity;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.invest.InvestActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 常见问题
 * Created by Think on 2017/10/22.
 */

public class CommonProblemActivity extends AutoBackBtnActivity {

    ListView list;
    CommonProblemAdapter commonProblemAdapter;
    List<JSONObject> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common_problem);

        addTopTitle(R.string.common_problem);
        list = (ListView) findViewById(R.id.list);
        dataList = new ArrayList<>();
        commonProblemAdapter = new CommonProblemAdapter(this, dataList);
        list.setAdapter(commonProblemAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = (int) (position < id ? position : id);
                Intent intent = new Intent(CommonProblemActivity.this, QAListActivity.class);
                JSONObject data = dataList.get(index);
                intent.putExtra("dicName", JSONHelper.getStringValue(data, "dicName"));
                intent.putExtra("dicCode", JSONHelper.getStringValue(data, "dicCode"));
                startActivity(intent);
            }
        });

        requestProblem();
    }

    private void requestProblem() {
        QueryQATypeHandler queryQATypeHandler = new QueryQATypeHandler(this);
        queryQATypeHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    try {
                        JSONArray dataArray = response.getJSONArray("list");
                        for (int i = 0; i < dataArray.length(); i++) {
                            dataList.add(dataArray.getJSONObject(i));
                        }
                        commonProblemAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), R.string.data_exception, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        queryQATypeHandler.execute();
    }
}
