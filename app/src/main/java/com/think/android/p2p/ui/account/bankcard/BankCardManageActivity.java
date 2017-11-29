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
import com.think.android.p2p.ui.views.ActionSheet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 我的银行卡
 * Created by Think on 2017/10/10.
 */

public class BankCardManageActivity extends AutoBackBtnActivity implements View.OnClickListener {

    private static final int BIND_BANKCARD = 1;

    ListView cardList;
    BankCardListAdapter bankCardListAdapter;

    ArrayList<JSONObject> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankcard_manager);
        addTopTitle(R.string.my_bankcard);
        initViews();
    }

    private void initViews() {
        cardList = (ListView) findViewById(R.id.bankcard_list);
        data = new ArrayList<>();
        bankCardListAdapter = new BankCardListAdapter(this, data);
        cardList.setAdapter(bankCardListAdapter);
        cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int index = (int) (position < id ? position : id);
                if (index < data.size()) {
//                    setTheme(R.style.ActionSheetStyle);
//                    ActionSheet menu = new ActionSheet(BankCardManageActivity.this);
//                    menu.setCancelButtonTitle(R.string.cancel);
//                    menu.addItems("删除");
//                    menu.setItemClickListener(new ActionSheet.MenuItemClickListener() {
//                        @Override
//                        public void onItemClick(int itemPosition) {
//                            unbindBankcard(index);
//                        }
//                    });
//                    menu.setCancelableOnTouchMenuOutside(true);
//                    menu.showMenu();
                } else {
                    Intent intent = new Intent(BankCardManageActivity.this, BindBankCardActivity.class);
                    startActivityForResult(intent, BIND_BANKCARD);
                }
            }
        });
        requestData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

        }
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
                    bankCardListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        queryBindBankcardHandler.execute();
    }

    private void unbindBankcard(final int position) {
        String bankId = JSONHelper.getStringValue(data.get(position), "id");
        UnbindBankcardHandler unbindBankcardHandler = new UnbindBankcardHandler(this, bankId);
        unbindBankcardHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    data.remove(position);
                    bankCardListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        unbindBankcardHandler.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BIND_BANKCARD:
                if (resultCode == RESULT_OK) {
                    requestData();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
