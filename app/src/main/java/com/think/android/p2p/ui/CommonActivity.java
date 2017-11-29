package com.think.android.p2p.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.GestureUtils;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.think.android.p2p.R;
import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.safe.gesturepwd.GestureToUnlockActivity;

/**
 * Created by Think on 2017/10/10.
 */

public class CommonActivity extends com.amarsoft.support.android.ui.CommonActivity {
    // 左布局
    private LinearLayout leftLayout;
    // 右布局
    private LinearLayout rightLayout;
    // 中间布局
    private LinearLayout midLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获取顶部功能条
     *
     * @return actionBarView
     */
    public View getToolbarView() {
        return findViewById(R.id.toolbar);
    }

    /**
     * 隐藏顶部功能条
     */
    public void hideActionBarView() {
        getToolbarView().setVisibility(View.GONE);
    }

    /**
     * 显示顶部功能条
     */
    public void showActionBarView() {
        getToolbarView().setVisibility(View.VISIBLE);
    }

    /**
     * 获取功能条左布局
     *
     * @return leftLayout
     */
    public LinearLayout getLeftLayout() {
        if (getToolbarView() == null) return null;
        if (leftLayout == null) {
            leftLayout = (LinearLayout) getToolbarView().findViewById(R.id.left_layout);
        }
        return leftLayout;
    }

    /**
     * 获取功能条右布局
     *
     * @return rightLayout
     */
    public LinearLayout getRightLayout() {
        if (getToolbarView() == null) return null;
        if (rightLayout == null) {
            rightLayout = (LinearLayout) getToolbarView().findViewById(R.id.right_layout);
        }
        return rightLayout;
    }

    /**
     * 获取功能条中间布局
     *
     * @return midLayout
     */
    public LinearLayout getMidLayout() {
        if (getToolbarView() == null) return null;
        if (midLayout == null) {
            midLayout = (LinearLayout) getToolbarView().findViewById(R.id.middle_layout);
        }
        return midLayout;
    }

    /**
     * 添加标题
     *
     * @param titleId 标题资源Id
     */
    public void addTopTitle(int titleId) {
        /**
         * 中间的linearlanyout
         */
        LinearLayout midLayout = getMidLayout();
        if (midLayout == null) return;
        midLayout.removeAllViews();
        // 中间的textview
        TextView textView = new TextView(this);
        textView.setSingleLine();
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.actionbar_title));
        textView.setText(titleId);
        textView.setTextColor(getResources().getColor(R.color.actionbar_title));
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        midLayout.addView(textView);
    }

    /**
     * 添加标题
     *
     * @param title 标题
     */
    public void addTopTitle(String title) {
        /**
         * 中间的linearlanyout
         */
        LinearLayout midLayout = getMidLayout();
        if (midLayout == null) return;
        midLayout.removeAllViews();
        // 中间的textview
        TextView textView = new TextView(this);
        textView.setSingleLine();
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.actionbar_title));
        textView.setText(title);
        textView.setTextColor(getResources().getColor(R.color.actionbar_title));
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        midLayout.addView(textView);
    }

    /**
     * 清除标题
     */
    public void clearTopTitle() {
        LinearLayout midLayout = getMidLayout();
        if (midLayout == null) return;
        midLayout.removeAllViews();
    }

    /**
     * 添加右上角的TextView按钮
     *
     * @param titleId 按钮文字id
     */
    public void addTopRightTxtBtn(int titleId) {
        LinearLayout rightLayout = getRightLayout();
        if (rightLayout == null) return;
        rightLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.rightMargin = 10;
        TextView topRightTextView = new TextView(this);
        topRightTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.actionbar_right));
        topRightTextView.setLayoutParams(params);
        topRightTextView.setText(titleId);
        topRightTextView.setGravity(Gravity.CENTER);
        topRightTextView.setTextColor(getResources().getColor(R.color.actionbar_right));
        topRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topRightTxtBtnClick();
            }
        });
        rightLayout.addView(topRightTextView);
    }

    protected void topRightTxtBtnClick() {
//打开协议

    }

    public void clearTopRight() {
        LinearLayout rightLayout = getRightLayout();
        if (rightLayout == null) return;
        rightLayout.removeAllViews();
    }

    /**
     * dp px转换
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dpToPx(Context context, int dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    /**
     * 登录断开
     */
    protected void logout() {
        app.finishActivityButThis(MainActivity.class);
    }

    private static long startedActivityCount = 0l;

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        startedActivityCount++;
        if (1 == startedActivityCount) {
            applicationDidEnterForeground();
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        startedActivityCount--;
        if (0 == startedActivityCount) {
            applicationDidEnterBackground();
        }
    }

    /**
     * Activity的回调函数。当application进入前台时，该函数会被自动调用。
     */
    protected void applicationDidEnterForeground() {
        UserInfoUtils userInfoUtils = new UserInfoUtils(this);
        if (userInfoUtils.needCheck()) {
            Intent intent = new Intent(this, GestureToUnlockActivity.class);
            intent.putExtra("flag", GestureToUnlockActivity.CHECK_STATUS);
            startActivity(intent);
        }
    }

    /**
     * Activity的回调函数。当application进入后台时，该函数会被自动调用。
     */
    protected void applicationDidEnterBackground() {
        UserInfoUtils userInfoUtils = new UserInfoUtils(this);
        userInfoUtils.setLastTime();
    }
}
