package com.think.android.p2p.ui.property.recharge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amarsoft.support.android.ui.CommonFragment;
import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.account.bankcard.BankcardItem;
import com.think.android.p2p.ui.account.bankcard.QueryBindBankcardHandler;
import com.think.android.p2p.ui.account.bankcard.SelectBankcardActivity;
import com.think.android.p2p.ui.property.PropertyHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;

/**
 * 充值-第一页
 * Created by Think on 2017/10/15.
 */

public class RechargeFragment extends CommonFragment implements View.OnClickListener {

    public static final int SELECT_BANKCARD = 0x10;

    int index = 0;

    EditText amountEdit;
    TextView balanceText;
    Button rechargeBtn;

    BankcardItem bankcardItem;

    public RechargeFragment() {
        super(R.layout.fragment_recharge);
    }

    @Override
    protected void initViews() {
        super.initViews();

        View view = getView();
        if (view == null) return;

        amountEdit = (EditText) view.findViewById(R.id.recharge_amount_edit);
        balanceText = (TextView) view.findViewById(R.id.balance_text);
        rechargeBtn = (Button) view.findViewById(R.id.recharge_btn);

        rechargeBtn.setOnClickListener(this);

        bankcardItem = new BankcardItem(getActivity(), view);
        bankcardItem.init();
        bankcardItem.getItem().setOnClickListener(this);
        queryProperty();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.recharge_btn:
                requestRechargeApply();
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

    private void requestRechargeApply() {
        String amount = amountEdit.getText().toString();
        if ("".equals(amount) && Integer.parseInt(amount) < 100) {
            Toast.makeText(getActivity().getApplicationContext(), R.string.recharge_hint, Toast.LENGTH_SHORT).show();
            return;
        }

        String bandCardId = bankcardItem.getBankCardId();

        RechargeApplyHandler rechargeApplyHandler = new RechargeApplyHandler(getActivity(), bandCardId, amount);
        rechargeApplyHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    String data = response.toString();
                    toNext(data);
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        rechargeApplyHandler.execute();
    }

    private void toNext(String data) {
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bundle.putInt("index", index);
        RechargeConfirmFragment rechargeConfirmFragment = new RechargeConfirmFragment();
        rechargeConfirmFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, rechargeConfirmFragment,
                RechargeConfirmFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
}
