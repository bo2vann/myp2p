package com.think.android.p2p.ui.property.myinvest;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.think.android.p2p.R;
import com.think.android.p2p.ui.AutoBackBtnActivity;

/**
 * 投资记录
 * Created by Think on 2017/10/10.
 */

public class MyInvestRecordActivity extends AutoBackBtnActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        addTopTitle(R.string.my_invest_record);

        MyInvestRecordFragment myInvestRecordFragment = new MyInvestRecordFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, myInvestRecordFragment,
                MyInvestRecordFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
}
