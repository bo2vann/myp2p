package com.think.android.p2p.ui.account.bankcard;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 选择银行卡
 * Created by Think on 2017/11/26.
 */

public class SelectBankcardActivity extends AutoBackBtnActivity {

    ListView cardList;
    SelectBankCardAdapter selectBankCardAdapter;

    ArrayList<JSONObject> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankcard_manager);
        addTopTitle(R.string.select_bankcard);
        initViews();
    }

    private void initViews() {
        cardList = (ListView) findViewById(R.id.bankcard_list);
        data = new ArrayList<>();
        selectBankCardAdapter = new SelectBankCardAdapter(this, data);
        cardList.setAdapter(selectBankCardAdapter);
        cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = (int) (position < id ? position : id);
                Intent intent = new Intent();
                intent.putExtra("index", index);
                setResult(RESULT_OK, intent);
                finishCurActivity();
            }
        });
        requestData();
    }

    private void requestData() {
        data.clear();
        QueryBindBankcardHandler queryBindBankcardHandler = new QueryBindBankcardHandler(this);
        queryBindBankcardHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    try {
                        JSONArray bankcardArray = (JSONArray) JSONHelper.getValue(response, "list");
                        for (int i = 0; i < bankcardArray.length(); i++) {
                            JSONObject bankcard = bankcardArray.getJSONObject(i);
                            data.add(bankcard);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    selectBankCardAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        queryBindBankcardHandler.execute();
    }
}
