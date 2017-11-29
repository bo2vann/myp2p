package com.think.android.p2p.ui;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.think.android.p2p.R;

/**
 * Created by Think on 2017/10/11.
 */

public class AutoBackBtnActivity extends CommonActivity {


    TextView topleftTextView;

    int leftTextId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        LinearLayout toolbar = (LinearLayout) findViewById(R.id.toolbar);
        initToBackView();
    }

    private void initToBackView() {
        LinearLayout leftLayout = getLeftLayout();
        if (leftLayout == null) return;
        leftLayout.removeAllViews();

        topleftTextView = new TextView(this);
        topleftTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.actionbar_left));
        LinearLayout.LayoutParams leftlLayoutParams = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
        leftlLayoutParams.gravity = Gravity.CENTER;
        topleftTextView.setLayoutParams(leftlLayoutParams);
        if (leftTextId == 0) {
            topleftTextView.setCompoundDrawablesWithIntrinsicBounds(getResources()
                    .getDrawable(R.mipmap.back_btn), null, null, null);
        } else {
            topleftTextView.setText(leftTextId);
            topleftTextView.setPadding(dpToPx(this, 10), dpToPx(this, 5), 0, dpToPx(this, 5));
        }
        topleftTextView.setSingleLine();
//        int backTitleId = R.string.back;
//        if (getIntent().hasExtra("BackTitle")) {
//            backTitleId = getIntent().getIntExtra("BackTitle", R.string.back);
//        }
//        topleftTextView.setText(backTitleId);
        topleftTextView.setGravity(Gravity.CENTER);
//        topleftTextView.setPadding(dpToPx(this, 10), dpToPx(this, 5), 0, dpToPx(this, 5));
        topleftTextView.setTextColor(getResources().getColor(R.color.actionbar_left));
        topleftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyBackDown();
            }
        });
        leftLayout.addView(topleftTextView);
    }

    /**
     * 设置左上角返回按钮的标题
     *
     * @param title
     */
    public void setBackTitle(String title) {
        if (topleftTextView == null || title == null
                || title.trim().length() == 0) {
            return;
        }

        topleftTextView.setText(title);
    }

    /**
     * 设置左上角返回按钮的标题
     *
     * @param titleId
     */
    public void setBackTitle(int titleId) {
        if (titleId <= 0) {
            return;
        }
        leftTextId = titleId;
        if (topleftTextView != null) {
            topleftTextView.setText(titleId);
        }
    }
}
