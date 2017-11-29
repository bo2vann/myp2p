package com.think.android.p2p.ui.property;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amarsoft.support.android.ui.CommonFragment;
import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.account.UserBaseInfoHandler;
import com.think.android.p2p.ui.account.bankcard.BindBankCardActivity;
import com.think.android.p2p.ui.account.bankcard.QueryBindBankcardHandler;
import com.think.android.p2p.ui.account.security.PayPasswordSetActivity;
import com.think.android.p2p.ui.property.dueprofit.DueProfitActivity;
import com.think.android.p2p.ui.property.dueproperty.DuePropertyActivity;
import com.think.android.p2p.ui.property.myinvest.MyInvestRecordActivity;
import com.think.android.p2p.ui.property.recharge.RechargeActivity;
import com.think.android.p2p.ui.property.transrecord.TransRecordActivity;
import com.think.android.p2p.ui.property.withdraw.WithdrawActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 主页-资产
 * Created by Think on 2017/10/10.
 */

public class PropertyFragment extends CommonFragment implements View.OnClickListener {

    private static final int BIND_BANKCARD = 0x10;
    private static final int SET_PAY_PWD = 0x20;

    private static final String RECHARGE = "recharge";
    private static final String WITHDRAW = "withdraw";

    String currentMode;

    LinearLayout transLayout;

    TextView rechargeText;
    TextView withdrawText;
    Button investRecordBtn;
    Button duePropertyBtn;
    Button dueProfitBtn;

    TextView balanceText;
    TextView duePrincipalText;
    TextView dueProfitText;
    TextView duedPrincipalText;
    TextView duedProfitText;

    public PropertyFragment() {
        super(R.layout.fragment_property);
    }

    @Override
    protected void initViews() {
        super.initViews();

        View view = getView();
        if (view == null) return;

        balanceText = (TextView) view.findViewById(R.id.balance_text);
        duePrincipalText = (TextView) view.findViewById(R.id.due_principal_text);
        dueProfitText = (TextView) view.findViewById(R.id.due_profit_text);
        duedPrincipalText = (TextView) view.findViewById(R.id.dued_principal_text);
        duedProfitText = (TextView) view.findViewById(R.id.dued_profit_text);

        transLayout = (LinearLayout) view.findViewById(R.id.trans_layout);
        rechargeText = (TextView) view.findViewById(R.id.recharge_text);
        withdrawText = (TextView) view.findViewById(R.id.withdraw_text);
        investRecordBtn = (Button) view.findViewById(R.id.invest_record_btn);
        duePropertyBtn = (Button) view.findViewById(R.id.due_property_btn);
        dueProfitBtn = (Button) view.findViewById(R.id.due_profit_btn);

        transLayout.setOnClickListener(this);
        rechargeText.setOnClickListener(this);
        withdrawText.setOnClickListener(this);
        investRecordBtn.setOnClickListener(this);
        duePropertyBtn.setOnClickListener(this);
        dueProfitBtn.setOnClickListener(this);

        requestData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            requestData();
        }
    }

    public void refresh() {
        requestData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.trans_layout:
                toActivity(TransRecordActivity.class);
                break;
            case R.id.recharge_text:
                checkBind(RECHARGE);
                break;
            case R.id.withdraw_text:
                checkBind(WITHDRAW);
                break;
            case R.id.invest_record_btn:
                toActivity(MyInvestRecordActivity.class);
                break;
            case R.id.due_property_btn:
                toActivity(DuePropertyActivity.class);
                break;
            case R.id.due_profit_btn:
                toActivity(DueProfitActivity.class);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BIND_BANKCARD:
                if (resultCode == Activity.RESULT_OK) {
                    checkPayPWD(currentMode);
                }
                break;
            case SET_PAY_PWD:
                if (resultCode == Activity.RESULT_OK) {
                    skip(currentMode);
                }
                break;
        }
    }

    private void checkBind(final String mode) {
        QueryBindBankcardHandler queryBindBankcardHandler = new QueryBindBankcardHandler(getActivity());
        queryBindBankcardHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    try {
                        JSONArray bankList = response.getJSONArray("list");
                        if (bankList.length() == 0) {
                            currentMode = mode;
                            Intent intent = new Intent(getActivity(), BindBankCardActivity.class);
                            startActivityForResult(intent, BIND_BANKCARD);
                        } else {
                            checkPayPWD(mode);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        queryBindBankcardHandler.execute();
    }

    private void checkPayPWD(final String mode) {
        UserBaseInfoHandler userBaseInfoHandler = new UserBaseInfoHandler(getActivity(), true);
        userBaseInfoHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    if ("1".equals(JSONHelper.getStringValue(response, "payPassWordFlag"))) {
                        skip(mode);
                    } else {
                        currentMode = mode;
                        Intent intent = new Intent(getActivity(), PayPasswordSetActivity.class);
                        startActivityForResult(intent, SET_PAY_PWD);
                    }
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        userBaseInfoHandler.execute();
    }

    private void skip(String mode) {
        if (RECHARGE.equals(mode)) {
            Intent intent = new Intent(getActivity(), RechargeActivity.class);
            startActivity(intent);
        } else if (WITHDRAW.equals(mode)) {
            Intent intent = new Intent(getActivity(), WithdrawActivity.class);
            startActivity(intent);
        }
    }

    private void requestData() {
        PropertyHandler propertyHandler = new PropertyHandler(getActivity());
        propertyHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    balanceText.setText(JSONHelper.getStringValue(response, "banlance"));
                    duePrincipalText.setText(JSONHelper.getStringValue(response, "waitBackPrincipal"));
                    dueProfitText.setText(JSONHelper.getStringValue(response, "waitBackInterest"));
                    duedPrincipalText.setText(JSONHelper.getStringValue(response, "takeBackPrincipal"));
                    duedProfitText.setText(JSONHelper.getStringValue(response, "takeBackInterest"));
                }
            }
        });
        propertyHandler.execute();
    }

    private void toActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }
}
