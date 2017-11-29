package com.think.android.p2p.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.think.android.p2p.R;

/**
 * Created by blu on 2015/10/22.
 */
public class NormalDialog extends Dialog {

    /**
     * 是否需要取消按钮
     */
    boolean cancelFlag;

    /**
     * 标题
     */
    TextView titleText;
    /**
     * 内容
     */
    TextView bodyText;
    /**
     * 取消按钮
     */
    Button cancelBtn;
    /**
     * 确认按钮
     */
    Button confirmBtn;

    /**
     * 按键监听
     */
    public interface OnClickListener {

        void onCancel();

        void onConfirm();
    }

    /**
     * 标题字符串资源
     */
    int titleId;
    /**
     * 内容字符串资源
     */
    int bodyId;
    OnClickListener onClickListener;

    public NormalDialog(Context context, int titleId, int bodyId,
                        OnClickListener onClickListener, boolean cancelFlag) {
        super(context);
        this.titleId = titleId;
        this.bodyId = bodyId;
        this.onClickListener = onClickListener;
        this.cancelFlag = cancelFlag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_normal);

        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        titleText = (TextView) findViewById(R.id.dialog_title);
        bodyText = (TextView) findViewById(R.id.dialog_text);
        cancelBtn = (Button) findViewById(R.id.cancel);
        confirmBtn = (Button) findViewById(R.id.confirm);

        if (titleId > 0) {
            titleText.setText(titleId);
        }
        if (bodyId > 0) {
            bodyText.setText(bodyId);
        }

        if (!cancelFlag) cancelBtn.setVisibility(View.GONE);

        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (onClickListener != null)
                    onClickListener.onCancel();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null)
                    onClickListener.onConfirm();
            }
        });
    }

}
