package com.think.android.p2p.ui.invest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amarsoft.support.android.ui.CommonFragment;
import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.ProtocolActivity;
import com.think.android.p2p.ui.account.UserBaseInfoHandler;
import com.think.android.p2p.ui.account.bankcard.BankcardItem;
import com.think.android.p2p.ui.property.PropertyHandler;
import com.think.android.p2p.ui.views.PayPasswordDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 投资-支付
 * Created by Think on 2017/10/15.
 */

public class PayFragment extends CommonFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    JSONObject projectInfo;
    String amount;
    double need;

    Button investBtn;
    TextView orderAmountText;
    TextView interestDateText;
    TextView dueDateText;
    TextView expectedProfitText;
    TextView balanceText;
//    Switch balanceSwitch;
//    TextView needPayText;

    CheckBox protocolCheck;
    TextView protocol;

    public PayFragment() {
        super(R.layout.fragment_invest_pay);
    }

    @Override
    protected void initViews() {
        super.initViews();

        View view = getView();
        if (view == null) return;

        investBtn = (Button) view.findViewById(R.id.invest_btn);
        orderAmountText = (TextView) view.findViewById(R.id.order_amount_text);
        interestDateText = (TextView) view.findViewById(R.id.interest_date_text);
        dueDateText = (TextView) view.findViewById(R.id.due_date_text);
        expectedProfitText = (TextView) view.findViewById(R.id.expected_profit_text);
        balanceText = (TextView) view.findViewById(R.id.balance_text);
//        balanceSwitch = (Switch) view.findViewById(R.id.balance_switch);
//        needPayText = (TextView) view.findViewById(R.id.need_pay_text);

        investBtn.setOnClickListener(this);
//        balanceSwitch.setOnCheckedChangeListener(this);
        protocolCheck = (CheckBox) view.findViewById(R.id.protocol_check);
        protocol = (TextView) view.findViewById(R.id.protocol);
        protocol.setOnClickListener(this);
//        BankcardItem bankcardItem = new BankcardItem(getActivity(), view);
//        bankcardItem.init();

        initData();
    }

    private void initData() {
        try {
            projectInfo = new JSONObject(getActivity().getIntent().getStringExtra("projectInfo"));
            interestDateText.setText(JSONHelper.getStringValue(projectInfo, "rateDate"));
            dueDateText.setText(JSONHelper.getStringValue(projectInfo, "rateEndDate"));
            amount = getActivity().getIntent().getStringExtra("amount");
            orderAmountText.setText("￥" + amount + "元");
            if ("ZRBD".equals(JSONHelper.getStringValue(projectInfo, "projectType"))) {
                protocol.setText("转让协议");
            } else {
                protocol.setText("借款协议");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        queryExpectedProfit();
        requestBalance();
    }

    PayPasswordDialog.OnPayListener onPayListener = new PayPasswordDialog.OnPayListener() {
        @Override
        public void onCall(String pwd) {
            // 交易请求
            checkPwd(pwd);
        }
    };

    PayPasswordDialog payPasswordDialog;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.invest_btn:
                if (protocolCheck.isChecked()) {
                    payPasswordDialog = new PayPasswordDialog(getActivity(), onPayListener);
                    payPasswordDialog.show();
                } else {
                    String toast = "请阅读并同意《";
                    if ("ZRBD".equals(JSONHelper.getStringValue(projectInfo, "projectType"))) {
                        toast += "转让协议";
                    } else {
                        toast += "借款协议";
                    }
                    toast += "》";
                    Toast.makeText(getActivity().getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.protocol:
                Intent intent = new Intent(getActivity(), ProtocolActivity.class);
                if ("ZRBD".equals(JSONHelper.getStringValue(projectInfo, "projectType"))) {
                    intent.putExtra("url", "loan/protocol/transfer.html");
                } else {
                    intent.putExtra("url", "loan/protocol/loan.html");
                }
                intent.putExtra("title", "在线投资服务协议书");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
//            case R.id.balance_switch:
//                if (isChecked) {
//                    needPayText.setText("" + need);
//                } else {
//                    needPayText.setText("" + amount);
//                }
//                break;
        }
    }

    private void checkPwd(String password) {
        InvestCheckPwdHandler investCheckPwdHandler = new InvestCheckPwdHandler(getActivity(), password);
        investCheckPwdHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    if (payPasswordDialog != null) payPasswordDialog.dismiss();
                    invest();
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        investCheckPwdHandler.execute();
    }

    private void invest() {
        InvestHandler investHandler = new InvestHandler(getActivity(), JSONHelper.getStringValue(projectInfo, "projectNo"), amount);
        investHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    toNext();
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        investHandler.execute();
    }

    private void queryExpectedProfit() {
        CalcProfitHandler calcProfitHandler = new CalcProfitHandler(getActivity(), JSONHelper.getStringValue(projectInfo, "projectNo"), amount);
        calcProfitHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    double income = Double.parseDouble(JSONHelper.getStringValue(response, "inCome"));
                    int temp = (int) (income * 100);
                    double last = ((double) temp) / 100;
                    expectedProfitText.setText("￥" + last);
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        calcProfitHandler.execute();
    }

    private void requestBalance() {
        PropertyHandler propertyHandler = new PropertyHandler(getActivity());
        propertyHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    String balance = JSONHelper.getStringValue(response, "banlance");
                    balanceText.setText(balance + "元");

//                    need = Double.parseDouble(amount) - Double.parseDouble(balance);
//                    if (need < 0) need = 0;
//                    needPayText.setText("" + need);
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        propertyHandler.execute();
    }

    private void toNext() {
        PaySuccessFragment paySuccessFragment = new PaySuccessFragment();
        Bundle bundle = new Bundle();
        bundle.putString("amount", amount);
        bundle.putString("rateDate", JSONHelper.getStringValue(projectInfo, "rateDate"));
        bundle.putString("rateEndDate", JSONHelper.getStringValue(projectInfo, "rateEndDate"));
        paySuccessFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, paySuccessFragment,
                PaySuccessFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
}
