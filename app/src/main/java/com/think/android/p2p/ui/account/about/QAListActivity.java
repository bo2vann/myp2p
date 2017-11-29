package com.think.android.p2p.ui.account.about;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.AutoBackBtnActivity;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题列表
 * Created by Think on 2017/10/22.
 */

public class QAListActivity extends AutoBackBtnActivity {

    ListView list;
    String dicCode;
    QAListAdapter qaListAdapter;
    List<JSONObject> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_qa_list);

        String title = getIntent().getStringExtra("dicName");
        addTopTitle(title);

        dicCode = getIntent().getStringExtra("dicCode");

        list = (ListView) findViewById(R.id.list);
        dataList = new ArrayList<>();
        qaListAdapter = new QAListAdapter(this, dataList);
        list.setAdapter(qaListAdapter);
        queryQA();
    }

    private void queryQA() {
        QueryQAHandler queryQAHandler = new QueryQAHandler(this, dicCode);
        queryQAHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    try {
                        JSONArray dataArray = response.getJSONArray("list");
                        for (int i = 0; i < dataArray.length(); i++) {
                            dataList.add(dataArray.getJSONObject(i));
                        }
                        qaListAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), R.string.data_exception, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        queryQAHandler.execute();
    }

}
