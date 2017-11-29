package com.think.android.p2p.ui.property.dueprofit;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.think.android.p2p.R;
import com.think.android.p2p.ui.AutoBackBtnActivity;
import com.think.android.p2p.ui.property.dueproperty.DuePropertyFragment;

/**
 * 待收收益
 * Created by Think on 2017/10/15.
 */

public class DueProfitActivity extends AutoBackBtnActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        addTopTitle(R.string.due_profit);

        DueProfitFragment dueProfitFragment = new DueProfitFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, dueProfitFragment,
                DueProfitFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
}
