package com.think.android.p2p.ui.property.withdraw;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amarsoft.support.android.ui.CommonFragment;
import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.views.PayPasswordDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 提现-确认
 * Created by Think on 2017/10/15.
 */

public class WithdrawConfirmFragment extends CommonFragment implements View.OnClickListener {

    TextView withdrawAmountText;
    TextView feeText;
    TextView actualAmountText;
    TextView accountNameText;
    TextView bankcardText;

    Button withdrawBtn;

    String tcNo;

    String bankName;
    String cardNo;
    String dataString;

    public WithdrawConfirmFragment() {
        super(R.layout.fragment_withdraw_confirm);
    }

    @Override
    protected void initViews() {
        super.initViews();

        View view = getView();
        if (view == null) return;

        dataString = getArguments().getString("data");
        bankName = getArguments().getString("bankName");
        cardNo = getArguments().getString("cardNo");
        JSONObject data;
        try {
            data = new JSONObject(dataString);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        if (data == null) return;
        withdrawAmountText = (TextView) view.findViewById(R.id.withdraw_amount_text);
        withdrawAmountText.setText(JSONHelper.getStringValue(data, "amount"));
        feeText = (TextView) view.findViewById(R.id.fee_text);
        feeText.setText(JSONHelper.getStringValue(data, "tcFee"));
        actualAmountText = (TextView) view.findViewById(R.id.actual_amount_text);
        actualAmountText.setText(JSONHelper.getStringValue(data, "realAmount"));
        accountNameText = (TextView) view.findViewById(R.id.account_name_text);
        accountNameText.setText(JSONHelper.getStringValue(data, "name"));
        bankcardText = (TextView) view.findViewById(R.id.bankcard_text);
        bankcardText.setText(JSONHelper.getStringValue(data, "bankName"));
        withdrawBtn = (Button) view.findViewById(R.id.withdraw_btn);
        withdrawBtn.setOnClickListener(this);

        tcNo = JSONHelper.getStringValue(data, "tcNo");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.withdraw_btn:
                showDialog();
                break;
        }
    }

    private void toNext() {
        Bundle bundle = new Bundle();
        bundle.putString("data", dataString);
        bundle.putString("bankName", bankName);
        bundle.putString("cardNo", cardNo);
        WithdrawSuccessFragment withdrawSuccessFragment = new WithdrawSuccessFragment();
        withdrawSuccessFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, withdrawSuccessFragment,
                WithdrawSuccessFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    PayPasswordDialog payPasswordDialog;

    private void showDialog() {
        payPasswordDialog = new PayPasswordDialog(getActivity(), new PayPasswordDialog.OnPayListener() {
            @Override
            public void onCall(String pwd) {
                withdrawConfirm(pwd);
            }
        });
        payPasswordDialog.show();
    }

    private void withdrawConfirm(String pwd) {
        WithdrawConfirmHandler withdrawConfirmHandler = new WithdrawConfirmHandler(getActivity(), tcNo, pwd);
        withdrawConfirmHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    if (payPasswordDialog != null) {
                        payPasswordDialog.dismiss();
                    }
                    toNext();
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        withdrawConfirmHandler.execute();
    }
}
