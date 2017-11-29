package com.think.android.p2p.ui.safe.gesturepwd;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import com.think.android.p2p.R;
import com.think.android.p2p.ui.AutoBackBtnActivity;
import com.think.android.p2p.ui.CommonActivity;


/**
 * 绘制手势密码界面
 * Created by blu on 2015/7/23.
 */
public class GestureToUnlockActivity extends AutoBackBtnActivity {
    public static final int FIRST_SET_STATUS = 0x00;
    public static final int SET_STATUS = 0x10;
    public static final int CHECK_STATUS = 0x20;

    private LinearLayout toolbar;
    private YellowGestureFragment yellowGestureFragment;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesturetounlock);
        addTopTitle(R.string.gesture_password_set);
        if (getIntent().getExtras() != null) {
            flag = getIntent().getExtras().getInt("flag");
        }

        toolbar = (LinearLayout) findViewById(R.id.toolbar);

        if (savedInstanceState == null) {
            if (yellowGestureFragment == null) {
                Bundle bundle = new Bundle();
                switch (flag) {
                    case FIRST_SET_STATUS:
                        bundle.putInt("flag", YellowGestureFragment.SET_FIRST_STATUS);
                        toolbar.setVisibility(View.GONE);
                        break;
                    case SET_STATUS:
                        bundle.putInt("flag", YellowGestureFragment.SET_FIRST_STATUS);
                        break;
                    case CHECK_STATUS:
                        toolbar.setVisibility(View.GONE);
                        bundle.putInt("flag", YellowGestureFragment.CHECK_STATUS);
                        break;
                }
                yellowGestureFragment = new YellowGestureFragment();
                yellowGestureFragment.setArguments(bundle);
            }
            if (!yellowGestureFragment.isAdded()) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                        .beginTransaction();
                fragmentTransaction
                        .setCustomAnimations(R.anim.in_from_right,
                                R.anim.out_to_left, R.anim.in_from_left,
                                R.anim.out_to_right)
                        .add(R.id.gesture_container, yellowGestureFragment,
                                "YellowGestureFragment1").commit();
            }
        }
    }

    @Override
    protected void topRightTxtBtnClick() {
        keyBackDown();
    }

    @Override
    protected void keyBackDown() {
        if (flag == SET_STATUS) {//处于设置手势时候-取消
            finishCurActivity();
        } else {//属于灭屏点亮的时候-取消
//            app.finishAllActivity();
        }
    }

}

