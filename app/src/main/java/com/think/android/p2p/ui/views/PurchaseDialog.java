package com.think.android.p2p.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.invest.CalcProfitHandler;
import com.think.android.p2p.ui.invest.PayActivity;

import org.json.JSONObject;

import java.text.Format;

/**
 * 投资弹出框
 * Created by Think on 2017/11/4.
 */

public class PurchaseDialog extends Dialog implements View.OnClickListener {

    Context context;

    String projectNo;
    int maxAmount;
    int minAmount;

    TextView expectedProfitText;
    EditText amountEdit;
    TextView tipsText;
    ImageView minusBtn;
    ImageView plusBtn;
    Button investBtn;
    ImageButton closeBtn;

    Callback callback;

    public PurchaseDialog(Context context, Callback callback, String projectNo, int maxAmount, int minAmount) {
        super(context);
        this.context = context;
        this.callback = callback;
        this.projectNo = projectNo;
        this.maxAmount = maxAmount;
        this.minAmount = minAmount == 0 ? 1000 : minAmount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_purchase);

        setCanceledOnTouchOutside(false);

        initView();
    }

    private void initView() {
        expectedProfitText = (TextView) findViewById(R.id.expected_profit_text);
        tipsText = (TextView) findViewById(R.id.tips_text);
        amountEdit = (EditText) findViewById(R.id.invest_amount_edit);
        minusBtn = (ImageView) findViewById(R.id.minus_btn);
        plusBtn = (ImageView) findViewById(R.id.plus_btn);
        investBtn = (Button) findViewById(R.id.invest_btn);
        closeBtn = (ImageButton) findViewById(R.id.close);

        amountEdit.setText("" + minAmount);
        tipsText.setText(String.format(context.getResources().getString(R.string.invest_dialog_tip), "" + minAmount, "" + minAmount));

        queryExpectedProfit();

        amountEdit.setFocusableInTouchMode(true);

        amountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                queryExpectedProfit();
                amountModify();
            }
        });

        minusBtn.setOnClickListener(this);
        plusBtn.setOnClickListener(this);
        investBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int amount = Integer.parseInt(amountEdit.getText().toString());
        switch (id) {
            case R.id.minus_btn:
                if (amount > 2 * minAmount) {
                    amount -= minAmount;
                } else {
                    amount = minAmount;
                }
                amountEdit.setText("" + amount);
                amountModify();
                break;
            case R.id.plus_btn:
                if (amount < maxAmount - minAmount) {
                    amount += minAmount;
                } else {
                    amount = maxAmount;
                }
                amountEdit.setText("" + amount);
                amountModify();
                break;
            case R.id.invest_btn:
                if (amount < minAmount) {
                    Toast.makeText(getContext().getApplicationContext(), String.format(context.getResources().getString(R.string.invest_dialog_toast1), "" + minAmount), Toast.LENGTH_SHORT).show();
                    amountEdit.setText("" + minAmount);
                    amountModify();
                } else if (amount % minAmount > 0) {
                    Toast.makeText(getContext().getApplicationContext(), String.format(context.getResources().getString(R.string.invest_dialog_toast1), "" + minAmount), Toast.LENGTH_SHORT).show();
                    amountEdit.setText("" + amount / minAmount * minAmount);
                    amountModify();
                } else if (amount > maxAmount) {
                    Toast.makeText(getContext().getApplicationContext(), R.string.invest_dialog_toast2, Toast.LENGTH_SHORT).show();
                    amountEdit.setText("" + maxAmount);
                    amountModify();
                } else {
                    if (callback != null) {
                        dismiss();
                        callback.invest(amount);
                    }
                }
                break;
            case R.id.close:
                this.dismiss();
                break;
        }
    }

    Handler handler = new Handler();

    private void amountModify() {
        handler.removeCallbacks(requestProfitRunnable);
        if (!"".equals(amountEdit.getText().toString())) {
            handler.postDelayed(requestProfitRunnable, 200);
        }
    }

    Runnable requestProfitRunnable = new Runnable() {
        @Override
        public void run() {
            queryExpectedProfit();
        }
    };

    private void queryExpectedProfit() {
        String amount = amountEdit.getText().toString();
        CalcProfitHandler calcProfitHandler = new CalcProfitHandler(context, projectNo, amount);
        calcProfitHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    double income = Double.parseDouble(JSONHelper.getStringValue(response, "inCome"));
                    int temp = (int) (income * 100);
                    double last = ((double) temp) / 100;
                    expectedProfitText.setText("￥" + last);
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        calcProfitHandler.execute();
    }

    public interface Callback {
        public void invest(int amount);
    }
}
