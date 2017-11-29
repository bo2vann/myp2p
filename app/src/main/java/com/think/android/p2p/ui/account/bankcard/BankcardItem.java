package com.think.android.p2p.ui.account.bankcard;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 银行卡显示管理
 * Created by Think on 2017/11/7.
 */

public class BankcardItem {

    ArrayList<JSONObject> dataList;

    Context context;

    int current;

    LinearLayout bankcardLayout;

    ImageView bankImg;
    TextView bankText;
    TextView bankcardText;
    TextView bankcardLimitText;

    String bankName;
    String bankCardId;
    String cardNo;

    public BankcardItem(Context context, View view) {
        this.context = context;
        bankcardLayout = (LinearLayout) view.findViewById(R.id.bankcard_layout);
        bankImg = (ImageView) view.findViewById(R.id.bank_img);
        bankText = (TextView) view.findViewById(R.id.bank_text);
        bankcardText = (TextView) view.findViewById(R.id.bankcard_text);
        bankcardLimitText = (TextView) view.findViewById(R.id.bankcard_limit_text);
        current = 0;
        dataList = new ArrayList<>();
    }

    public void init() {
        requestBankcard();
    }

    public void setIndex(int index) {
        current = index;
    }

    private void requestBankcard() {
        QueryBindBankcardHandler queryBindBankcardHandler = new QueryBindBankcardHandler(context);
        queryBindBankcardHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    try {
                        JSONArray bankcardArray = (JSONArray) JSONHelper.getValue(response, "list");
                        if (bankcardArray.length() > 0) {
                            for (int i = 0; i < bankcardArray.length(); i++) {
                                JSONObject bankcard = bankcardArray.getJSONObject(i);
                                dataList.add(bankcard);
                            }
                            setBankcard(current);
                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        queryBindBankcardHandler.execute();
    }

    public void setBankcard(int index) {
        JSONObject bankcard = dataList.get(index);
        bankImg.setImageResource(context.getResources().getIdentifier("banklogo_" + JSONHelper.getStringValue(bankcard, "bankNo"), "mipmap", context.getPackageName()));
        bankName = JSONHelper.getStringValue(bankcard, "bankName");
        bankText.setText(bankName);
        cardNo = JSONHelper.getStringValue(bankcard, "cardNo");
        bankcardText.setText(cardNo);
        bankCardId = JSONHelper.getStringValue(bankcard, "id");
    }

    public String getBankName() {
        return this.bankName;
    }

    public String getBankCardId() {
        return this.bankCardId;
    }

    public String getCardNo() {
        return this.cardNo;
    }

    public LinearLayout getItem() {
        return bankcardLayout;
    }

}
