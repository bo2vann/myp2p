package com.think.android.p2p.ui.property.dueproperty;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.think.android.p2p.R;
import com.think.android.p2p.ui.AutoBackBtnActivity;
/**
 * 待收资产
 * Created by Think on 2017/10/15.
 */

public class DuePropertyActivity extends AutoBackBtnActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        addTopTitle(R.string.undue_property);

        DuePropertyFragment duePropertyFragment = new DuePropertyFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, duePropertyFragment,
                DuePropertyFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
}
