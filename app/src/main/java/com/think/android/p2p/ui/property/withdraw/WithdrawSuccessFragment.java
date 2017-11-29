package com.think.android.p2p.ui.property.withdraw;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amarsoft.support.android.ui.CommonFragment;
import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.CommonActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 提现-成功
 * Created by Think on 2017/10/15.
 */

public class WithdrawSuccessFragment extends CommonFragment implements View.OnClickListener {

    TextView bankNameText;
    TextView bankcardNoText;
    TextView withdrawAmountText;
    TextView feeText;
    TextView actualAmountText;

    Button confirmBtn;

    public WithdrawSuccessFragment() {
        super(R.layout.fragment_withdraw_success);
    }

    @Override
    protected void initViews() {
        super.initViews();

        View view = getView();
        if (view == null) return;

        JSONObject data;
        try {
            data = new JSONObject(getArguments().getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        bankNameText = (TextView) view.findViewById(R.id.bank_name_text);
        bankNameText.setText(getArguments().getString("bankName"));
        bankcardNoText = (TextView) view.findViewById(R.id.bankcard_no_text);
        bankcardNoText.setText(getArguments().getString("cardNo"));
        withdrawAmountText = (TextView) view.findViewById(R.id.withdraw_amount_text);
        withdrawAmountText.setText(JSONHelper.getStringValue(data, "amount"));
        feeText = (TextView) view.findViewById(R.id.fee_text);
        feeText.setText(JSONHelper.getStringValue(data, "tcFee"));
        actualAmountText = (TextView) view.findViewById(R.id.actual_amount_text);
        actualAmountText.setText(JSONHelper.getStringValue(data, "realAmount"));

        confirmBtn = (Button) view.findViewById(R.id.confirm_btn);
        confirmBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.confirm_btn:
                getActivity().setResult(Activity.RESULT_OK);
                ((CommonActivity) getActivity()).finishCurActivity();
                break;
        }
    }
}
