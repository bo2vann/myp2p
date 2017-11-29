package com.think.android.p2p.ui.property.transrecord;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.think.android.p2p.R;
import com.think.android.p2p.ui.AutoBackBtnActivity;

/**
 * 交易记录
 * Created by Think on 2017/10/10.
 */

public class TransRecordActivity extends AutoBackBtnActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        addTopTitle(R.string.trans_record);

        TransRecordFragment transRecordFragment = new TransRecordFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, transRecordFragment,
                TransRecordFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
}
