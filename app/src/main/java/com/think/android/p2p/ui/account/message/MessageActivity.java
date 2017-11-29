package com.think.android.p2p.ui.account.message;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amarsoft.support.android.ui.CommonFragment;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.AutoBackBtnActivity;

/**
 * 消息中心
 * Created by Think on 2017/10/10.
 */

public class MessageActivity extends AutoBackBtnActivity {

    int position;

    SystemMsgFragment systemMsgFragment;
    MineMsgFragment mineMsgFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        addTopTitle(R.string.message_center);
        position = 1;
        updateUI();
        RadioGroup messageGroup = (RadioGroup) findViewById(R.id.message_group);
        messageGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.system_radio:
                        if (position == 1) return;
                        position = 1;
                        updateUI();
                        break;
                    case R.id.mine_radio:
                        if (position == 2) return;
                        position = 2;
                        updateUI();
                        break;
                }
            }
        });

    }

    private void updateUI() {
        updateFragment();
    }

    /**
     * 根据position更新主界面
     */
    private void updateFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 隐藏当前Fragment
        if (systemMsgFragment != null && systemMsgFragment.isAdded()) {
            transaction.hide(systemMsgFragment);
        }
        if (mineMsgFragment != null && mineMsgFragment.isAdded()) {
            transaction.hide(mineMsgFragment);
        }
        // 根据position显示当前Fragment
        switch (position) {
            case 1:
                if (systemMsgFragment == null) {
                    systemMsgFragment = new SystemMsgFragment();
                }
                showFragment(transaction, systemMsgFragment);
                break;
            case 2:
                if (mineMsgFragment == null) {
                    mineMsgFragment = new MineMsgFragment();
                }
                showFragment(transaction, mineMsgFragment);
                break;
        }
        transaction.commit();
    }

    /**
     * 显示fragment
     *
     * @param transaction FragmentTransaction
     * @param fragment    CommonFragment
     */
    private void showFragment(FragmentTransaction transaction, CommonFragment fragment) {
        if (fragment == null) return;
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());
        }
    }

}
