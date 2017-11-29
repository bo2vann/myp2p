package com.think.android.p2p.ui.property.recharge;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amarsoft.support.android.ui.CommonFragment;
import com.think.android.p2p.R;
import com.think.android.p2p.base.BaseApplication;
import com.think.android.p2p.ui.CommonActivity;
import com.think.android.p2p.ui.MainActivity;

/**
 * 充值-第一页
 * Created by Think on 2017/10/15.
 */

public class RechargeSuccessFragment extends CommonFragment implements View.OnClickListener {

    TextView rechargeAmounText;
    Button lookDetailsBtn;
    Button investBtn;

    public RechargeSuccessFragment() {
        super(R.layout.fragment_recharge_success);
    }

    @Override
    protected void initViews() {
        super.initViews();

        View view = getView();
        if (view == null) return;

        String amount = getArguments().getString("amount");

        rechargeAmounText = (TextView) view.findViewById(R.id.recharge_amount_text);
        rechargeAmounText.setText(amount);
        lookDetailsBtn = (Button) view.findViewById(R.id.look_details_btn);
        lookDetailsBtn.setOnClickListener(this);
        investBtn = (Button) view.findViewById(R.id.invest_now_btn);
        investBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.look_details_btn:
                ((CommonActivity) getActivity()).finishCurActivity();
                break;
            case R.id.invest_now_btn:
                ((BaseApplication) getActivity().getApplication()).finishAllActivity();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("position", 2);
                startActivity(intent);
                break;
        }
    }


}
