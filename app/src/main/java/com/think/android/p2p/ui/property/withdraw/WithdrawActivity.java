package com.think.android.p2p.ui.property.withdraw;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.think.android.p2p.R;
import com.think.android.p2p.ui.AutoBackBtnActivity;

/**
 * 提现
 * Created by Think on 2017/10/10.
 */

public class WithdrawActivity extends AutoBackBtnActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        addTopTitle(R.string.withdraw);

        WithdrawFragment withdrawFragment = new WithdrawFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, withdrawFragment,
                WithdrawFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
}
