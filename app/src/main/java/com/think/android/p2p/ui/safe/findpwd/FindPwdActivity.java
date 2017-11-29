package com.think.android.p2p.ui.safe.findpwd;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.think.android.p2p.R;
import com.think.android.p2p.ui.AutoBackBtnActivity;

/**
 * Created by Think on 2017/10/10.
 */

public class FindPwdActivity extends AutoBackBtnActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        addTopTitle(R.string.find_logon_password);

        SecurityVerifyFragment securityVerifyFragment = new SecurityVerifyFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, securityVerifyFragment,
                SecurityVerifyFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
}
