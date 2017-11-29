package com.think.android.p2p.ui.property.withdraw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amarsoft.support.android.ui.CommonFragment;
import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.account.bankcard.BankcardItem;
import com.think.android.p2p.ui.account.bankcard.SelectBankcardActivity;
import com.think.android.p2p.ui.property.PropertyHandler;
import com.think.android.p2p.ui.property.recharge.RechargeConfirmFragment;

import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;

/**
 * 提现-第一页
 * Created by Think on 2017/10/15.
 */

public class WithdrawFragment extends CommonFragment implements View.OnClickListener {

    public static final int SELECT_BANKCARD = 0x10;

    EditText amountEdit;
    TextView balanceText;
    Button withdrawBtn;

    BankcardItem bankcardItem;
    int index = 0;

    public WithdrawFragment() {
        super(R.layout.fragment_withdraw);
    }

    @Override
    protected void initViews() {
        super.initViews();

        View view = getView();
        if (view == null) return;

        amountEdit = (EditText) view.findViewById(R.id.withdraw_amount_edit);
        balanceText = (TextView) view.findViewById(R.id.balance_text);
        withdrawBtn = (Button) view.findViewById(R.id.withdraw_btn);

        withdrawBtn.setOnClickListener(this);

        bankcardItem = new BankcardItem(getActivity(), view);
        bankcardItem.init();
        bankcardItem.getItem().setOnClickListener(this);

        queryProperty();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.withdraw_btn:
                withdrawApply();
                break;
            case R.id.bankcard_layout:
                Intent intent = new Intent(getActivity(), SelectBankcardActivity.class);
                startActivityForResult(intent, SELECT_BANKCARD);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SELECT_BANKCARD:
                if (resultCode == RESULT_OK) {
                    if (bankcardItem != null) {
                        index = data.getIntExtra("index", 0);
                        bankcardItem.setBankcard(index);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void queryProperty() {
        PropertyHandler propertyHandler = new PropertyHandler(getActivity());
        propertyHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    String balance = JSONHelper.getStringValue(response, "banlance");
                    balanceText.setText(balance);
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        propertyHandler.execute();
    }

    private void withdrawApply() {
        String amount = amountEdit.getText().toString();
        if ("".equals(amount)) {
            Toast.makeText(getActivity().getApplicationContext(), R.string.withdraw_hint, Toast.LENGTH_SHORT).show();
            return;
        }
        String bankCardId = bankcardItem.getBankCardId();

        WithdrawApplyHandler withdrawApplyHandler = new WithdrawApplyHandler(getActivity(), amount, bankCardId);
        withdrawApplyHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    toNext(response.toString());
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        withdrawApplyHandler.execute();
    }

    private void toNext(String data) {
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bundle.putString("bankName", bankcardItem.getBankName());
        bundle.putString("cardNo", bankcardItem.getCardNo());
        WithdrawConfirmFragment withdrawConfirmFragment = new WithdrawConfirmFragment();
        withdrawConfirmFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, withdrawConfirmFragment,
                WithdrawConfirmFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
}
