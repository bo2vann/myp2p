package com.think.android.p2p.ui.account.security;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.think.android.p2p.R;
import com.think.android.p2p.ui.AutoBackBtnActivity;

/**
 * 支付密码设置
 * Created by Think on 2017/10/15.
 */

public class PayPasswordSetActivity extends AutoBackBtnActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        addTopTitle(R.string.pay_password_set);

        boolean first = getIntent().getBooleanExtra("first", false);

        if (first) {
            PayPwdSetFragment payPwdSetFragment = new PayPwdSetFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, payPwdSetFragment,
                    PayPwdSetFragment.class.getSimpleName());
            fragmentTransaction.commit();
        } else {
            PayPwdSecurityVerifyFragment payPwdSecurityVerifyFragment = new PayPwdSecurityVerifyFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, payPwdSecurityVerifyFragment,
                    PayPwdSecurityVerifyFragment.class.getSimpleName());
            fragmentTransaction.commit();
        }
    }
}
