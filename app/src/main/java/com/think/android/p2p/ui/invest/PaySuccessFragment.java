package com.think.android.p2p.ui.invest;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amarsoft.support.android.ui.CommonActivity;
import com.amarsoft.support.android.ui.CommonFragment;
import com.think.android.p2p.R;
import com.think.android.p2p.base.BaseApplication;
import com.think.android.p2p.ui.MainActivity;
import com.think.android.p2p.ui.account.invite.InviteActivity;
import com.think.android.p2p.ui.property.myinvest.MyInvestRecordActivity;

/**
 * 支付-支付成功
 * Created by Think on 2017/10/15.
 */

public class PaySuccessFragment extends CommonFragment implements View.OnClickListener {

    TextView interestText;
    TextView payInterestText;
    Button lookInvestBtn;
    Button inviteBtn;

    public PaySuccessFragment() {
        super(R.layout.fragment_pay_success);
    }

    @Override
    protected void initViews() {
        super.initViews();

        View view = getView();
        if (view == null) return;

        String rateDate = getArguments().getString("rateDate");
        String rateEndDate = getArguments().getString("rateEndDate");
        String amount = getArguments().getString("amount");

        interestText = (TextView) view.findViewById(R.id.interest_text);
        interestText.setText(String.format(getResources().getString(R.string.pay_success_tip1), rateDate));
        payInterestText = (TextView) view.findViewById(R.id.pay_interest_text);
        payInterestText.setText(String.format(getResources().getString(R.string.pay_success_tip2), amount, rateEndDate));

        lookInvestBtn = (Button) view.findViewById(R.id.look_invest);
        inviteBtn = (Button) view.findViewById(R.id.invite);
        lookInvestBtn.setOnClickListener(this);
        inviteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.look_invest:
                ((BaseApplication) getActivity().getApplication()).finishActivityButThis(MainActivity.class);
                Intent intent1 = new Intent(getActivity(), MyInvestRecordActivity.class);
                startActivity(intent1);
                break;
            case R.id.invite:
                ((BaseApplication) getActivity().getApplication()).finishActivityButThis(MainActivity.class);
                Intent intent2 = new Intent(getActivity(), InviteActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
