package com.think.android.p2p.ui.safe.gesturepwd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.think.android.p2p.R;
import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.MainActivity;
import com.think.android.p2p.ui.safe.logon.LogonActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 手势界面
 * Created by blu on 2015/8/14.
 */
public class YellowGestureFragment extends Fragment {

    public static final int SET_FIRST_STATUS = 0x10;
    public static final int SET_SECOND_STATUS = 0x20;
    public static final int CHECK_STATUS = 0x30;

    UserInfoUtils userInfoUtils;

    private LockPatternView gesturepwd_create_lockview;
    private String gestureWord;
    private TextView gesturePromptText;
    private TextView gesturepwd_create_text;
    private LinearLayout gesture_pwd_setting_preview;
    private View gesturepwd_setting_preview_0;
    private View gesturepwd_setting_preview_1;
    private View gesturepwd_setting_preview_2;
    private View gesturepwd_setting_preview_3;
    private View gesturepwd_setting_preview_4;
    private View gesturepwd_setting_preview_5;
    private View gesturepwd_setting_preview_6;
    private View gesturepwd_setting_preview_7;
    private View gesturepwd_setting_preview_8;
    private TextView fogetguesterpwd_textview;
    private int flag;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        userInfoUtils = new UserInfoUtils(getActivity());
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        // TODO Auto-generated method stub
        gesturepwd_create_lockview = (LockPatternView) getView().findViewById(
                R.id.gesture_pwd_create_lock);
        gesturePromptText = (TextView) getView().findViewById(R.id.gesture_prompt);
        gesturepwd_create_text = (TextView) getView().findViewById(
                R.id.gesture_pwd_create_text);
        gesture_pwd_setting_preview = (LinearLayout) getView().findViewById(
                R.id.gesture_pwd_setting_preview);
        gesturepwd_setting_preview_0 = (View) getView().findViewById(
                R.id.gesture_pwd_setting_preview_0);
        gesturepwd_setting_preview_1 = (View) getView().findViewById(
                R.id.gesture_pwd_setting_preview_1);
        gesturepwd_setting_preview_2 = (View) getView().findViewById(
                R.id.gesture_pwd_setting_preview_2);
        gesturepwd_setting_preview_3 = (View) getView().findViewById(
                R.id.gesture_pwd_setting_preview_3);
        gesturepwd_setting_preview_4 = (View) getView().findViewById(
                R.id.gesture_pwd_setting_preview_4);
        gesturepwd_setting_preview_5 = (View) getView().findViewById(
                R.id.gesture_pwd_setting_preview_5);
        gesturepwd_setting_preview_6 = (View) getView().findViewById(
                R.id.gesture_pwd_setting_preview_6);
        gesturepwd_setting_preview_7 = (View) getView().findViewById(
                R.id.gesture_pwd_setting_preview_7);
        gesturepwd_setting_preview_8 = (View) getView().findViewById(
                R.id.gesture_pwd_setting_preview_8);
        fogetguesterpwd_textview = (TextView) getView().findViewById(
                R.id.forget_gesture_pwd_text);
    }

    private void initData() {
        flag = getArguments().getInt("flag");
        // TODO Auto-generated method stub

        switch (flag) {
            case SET_FIRST_STATUS:
                gesturepwd_create_text.setText(R.string.gesture_password_set_tips);
                fogetguesterpwd_textview.setVisibility(View.INVISIBLE);
                break;
            case SET_SECOND_STATUS:
                gesturepwd_create_text.setText(R.string.gesture_password_confirm);
                fogetguesterpwd_textview.setVisibility(View.INVISIBLE);
                break;
            case CHECK_STATUS:
                gesturepwd_create_text.setText(R.string.gesture_password_input);
                gesturePromptText.setText(userInfoUtils.getMaskMobile() + "，欢迎回来！");
                gesturePromptText.setVisibility(View.VISIBLE);
                gesture_pwd_setting_preview.setVisibility(View.GONE);
                break;
        }

        ArrayList<String> gestureList;
        gestureList = getGestureList(getArguments().getString("password_string"));
        //提示显示颜色变化
        for (int i = 0; i < gestureList.size(); i++) {
            if (gestureList.get(i).equals("00")) {
                gesturepwd_setting_preview_0.setBackgroundResource(R.drawable.gesture_create_grid_item_bg_select);
            }
            if (gestureList.get(i).equals("01")) {
                gesturepwd_setting_preview_1.setBackgroundResource(R.drawable.gesture_create_grid_item_bg_select);
            }
            if (gestureList.get(i).equals("02")) {
                gesturepwd_setting_preview_2.setBackgroundResource(R.drawable.gesture_create_grid_item_bg_select);
            }
            if (gestureList.get(i).equals("10")) {
                gesturepwd_setting_preview_3.setBackgroundResource(R.drawable.gesture_create_grid_item_bg_select);
            }
            if (gestureList.get(i).equals("11")) {
                gesturepwd_setting_preview_4.setBackgroundResource(R.drawable.gesture_create_grid_item_bg_select);
            }
            if (gestureList.get(i).equals("12")) {
                gesturepwd_setting_preview_5.setBackgroundResource(R.drawable.gesture_create_grid_item_bg_select);
            }
            if (gestureList.get(i).equals("20")) {
                gesturepwd_setting_preview_6.setBackgroundResource(R.drawable.gesture_create_grid_item_bg_select);
            }
            if (gestureList.get(i).equals("21")) {
                gesturepwd_setting_preview_7.setBackgroundResource(R.drawable.gesture_create_grid_item_bg_select);
            }
            if (gestureList.get(i).equals("22")) {
                gesturepwd_setting_preview_8.setBackgroundResource(R.drawable.gesture_create_grid_item_bg_select);
            }
        }

    }

    private void initEvent() {
        // TODO Auto-generated method stub
        fogetguesterpwd_textview.setOnTouchListener(new OnTouchListener() {     //“忘记交易密码”
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        fogetguesterpwd_textview.setTextColor(getResources()
                                .getColor(R.color.gesture_actionbar));
                        break;
                    case MotionEvent.ACTION_UP:
                        fogetguesterpwd_textview.setTextColor(getResources()
                                .getColor(android.R.color.white));
                        // 清除手势信息
                        SharedPreferences gesturePre = getActivity().getSharedPreferences("Gesture", 0);
                        Editor editor = gesturePre.edit();
                        // editor.putBoolean("isSet", false);
                        editor.clear();
                        editor.commit();
                        // finish掉无关的界面
                        ((GestureToUnlockActivity) getActivity()).finishCurActivity();
                        new UserInfoUtils(getActivity()).clearUserInfo("else");
                        // 打开登录界面
                        Intent intent = new Intent(getActivity(), LogonActivity.class);
                        intent.putExtra("way", getActivity().getIntent().getStringExtra("way"));
                        startActivity(intent);
                        // CommonApplication().finishActivityButThis(LogonActivity.class);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        fogetguesterpwd_textview.setTextColor(getResources()
                                .getColor(R.color.gesture_actionbar));
                        break;

                    default:
                        fogetguesterpwd_textview.setTextColor(getResources()
                                .getColor(R.color.gesture_actionbar));
                        break;
                }
                return true;
            }
        });
        gesturepwd_create_lockview.setOnPatternListener(
                new LockPatternView.OnPatternListener() {     //滑动设置手势密码,2种情况（设置和确认）；
                    @Override
                    public void onPatternStart() {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onPatternDetected(List<LockPatternView.Cell> pattern) {
                        // TODO Auto-generated method stub
                        gestureWord = getGesturePassword(pattern);
                        switch (flag) {
                            case SET_FIRST_STATUS:
                                if (isGesturePasswordValid(gestureWord)) {
                                    YellowGestureFragment yellowGestureFragment = new YellowGestureFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("title", R.string.gesture_password_confirm);
                                    bundle.putString("password_string", gestureWord);
                                    bundle.putInt("flag", SET_SECOND_STATUS);
                                    yellowGestureFragment.setArguments(bundle);

                                    gesturepwd_create_lockview.clearPattern();//清除界面，滚动
                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                                            .beginTransaction();
                                    fragmentTransaction.setCustomAnimations(
                                            R.anim.in_from_right,
                                            R.anim.out_to_left,
                                            R.anim.in_from_left,
                                            R.anim.out_to_right)
                                            .replace(
                                                    R.id.gesture_container, yellowGestureFragment, "YellowGestureFragment2")
                                            .addToBackStack(null).commit();
                                }
                                break;
                            case SET_SECOND_STATUS:
                                if (gestureWord.equals(getArguments().getString("password_string"))) {
                                    myToast(R.string.gesture_password_success_toast);
                                    if ("loading".equals(getActivity().getIntent().getStringExtra("way"))) {
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                    userInfoUtils.setUserGesturePsw(gestureWord);
                                    ((GestureToUnlockActivity) getActivity()).finishCurActivity();
                                } else {
                                    myToast(R.string.gesture_password_dif_toast);
                                    gesturepwd_create_lockview.clearPattern();
                                    getActivity().getSupportFragmentManager()
                                            .popBackStack();
                                }
                                break;
                            case CHECK_STATUS:
                                if (userInfoUtils.checkUserGesturePsw(gestureWord)) {
                                    if ("loading".equals(getActivity().getIntent().getStringExtra("way"))) {
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                    ((GestureToUnlockActivity) getActivity()).finishCurActivity();
                                } else {
                                    myToast(R.string.gesture_password_wrong_toast);
                                    gesturepwd_create_lockview.clearPattern();
                                }
                                break;
                        }
                    }

                    @Override
                    public void onPatternCleared() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {
                        // TODO Auto-generated method stub

                    }
                });
    }

    private boolean isGesturePasswordValid(String gesturePassword) {
        if (gesturePassword.length() < 8) {
            myToast(R.string.gesture_password_length_4);
            /**
             * 清除界面
             */
            gesturepwd_create_lockview.clearPattern();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.fragment_gesturepassword, container,
                false);
    }

    /**
     * 将字符串转化成两位一组的array list
     */
    private ArrayList<String> getGestureList(String gestureString) {
        ArrayList<String> gList = new ArrayList<>();
        if (gestureString == null) {
            return gList;
        }
        for (int i = 0; i < gestureString.length() / 2; i++) {
            gList.add(gestureString.substring(i * 2, i * 2 + 2));
        }
        return gList;
    }

    /**
     * 将List<Cell>类型转化成可以识别的字符串
     */
    private String getGesturePassword(List<LockPatternView.Cell> pattern) {
        String pString = "";
        for (int i = 0; i < pattern.size(); i++) {
            pString += pattern.get(i).getRow() + ""
                    + pattern.get(i).getColumn();
        }
        return pString;
    }

    private void myToast(int textId) {
        Toast.makeText(getActivity().getApplicationContext(), textId, Toast.LENGTH_SHORT).show();
    }

}