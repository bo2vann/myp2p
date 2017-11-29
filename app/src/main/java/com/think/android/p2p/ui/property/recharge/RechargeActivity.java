package com.think.android.p2p.ui.property.recharge;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.think.android.p2p.R;
import com.think.android.p2p.ui.AutoBackBtnActivity;

/**
 * 充值
 * Created by Think on 2017/10/10.
 */

public class RechargeActivity extends AutoBackBtnActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        addTopTitle(R.string.recharge);

        RechargeFragment rechargeFragment = new RechargeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, rechargeFragment,
                RechargeFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
}
