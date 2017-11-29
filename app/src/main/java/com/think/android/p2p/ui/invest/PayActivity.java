package com.think.android.p2p.ui.invest;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.think.android.p2p.R;
import com.think.android.p2p.ui.AutoBackBtnActivity;

/**
 * 支付
 * Created by Think on 2017/10/10.
 */

public class PayActivity extends AutoBackBtnActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common);
        addTopTitle(R.string.confirm_pay);

        PayFragment payFragment = new PayFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, payFragment,
                PayFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
}
