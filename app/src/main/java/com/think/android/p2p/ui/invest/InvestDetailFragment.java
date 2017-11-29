package com.think.android.p2p.ui.invest;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amarsoft.support.android.ui.CommonFragment;
import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.account.UserBaseInfoHandler;
import com.think.android.p2p.ui.account.bankcard.BindBankCardActivity;
import com.think.android.p2p.ui.account.bankcard.QueryBindBankcardHandler;
import com.think.android.p2p.ui.account.security.PayPasswordSetActivity;
import com.think.android.p2p.ui.home.HomePageFragment;
import com.think.android.p2p.ui.views.LineProgressView;
import com.think.android.p2p.ui.views.PurchaseDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 项目详情-投资详情
 * Created by Think on 2017/10/15.
 */

public class InvestDetailFragment extends CommonFragment implements View.OnClickListener {

    private static final int BIND_BANKCARD = 0x10;
    private static final int SET_PAY_PWD = 0x20;

    ImageView tag;

    TextView projectNameText;
    TextView rateText;
    TextView termText;
    TextView statusText;
    TextView financingAmountText;
    TextView remainInvestText;
    TextView minInvestAmountText;

    TextView interestDateText;
    TextView dueDateText;
    TextView repaymentMethodText;
    TextView witnessOrgText;

    LineProgressView progress;

    TextView timeRemainText;

    Button investBtn;

    JSONObject response;

    boolean viewInitFinish = false;
    boolean dataInitFinish = false;

    public InvestDetailFragment() {
        super(R.layout.fragment_invest_detail);
    }

    @Override
    protected void initViews() {
        super.initViews();

        View view = getView();
        if (view == null) return;

        tag = (ImageView) view.findViewById(R.id.tag);

        projectNameText = (TextView) view.findViewById(R.id.project_name_text);
        rateText = (TextView) view.findViewById(R.id.rate_text);
        termText = (TextView) view.findViewById(R.id.term_text);
        statusText = (TextView) view.findViewById(R.id.status_text);
        progress = (LineProgressView) view.findViewById(R.id.progress);
        financingAmountText = (TextView) view.findViewById(R.id.financing_amount_text);
        remainInvestText = (TextView) view.findViewById(R.id.remain_invest_text);
        minInvestAmountText = (TextView) view.findViewById(R.id.min_invest_amount_text);

        interestDateText = (TextView) view.findViewById(R.id.interest_date_text);
        dueDateText = (TextView) view.findViewById(R.id.due_date_text);
        repaymentMethodText = (TextView) view.findViewById(R.id.repayment_method_text);
        witnessOrgText = (TextView) view.findViewById(R.id.witness_org_text);

        progress = (LineProgressView) view.findViewById(R.id.progress);

        timeRemainText = (TextView) view.findViewById(R.id.time_remain_text);

        investBtn = (Button) view.findViewById(R.id.invest_btn);
        investBtn.setOnClickListener(this);
        viewInitFinish = true;

        if (dataInitFinish) {
            initData();
        }
    }

    Timer timer;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    long current = System.currentTimeMillis() - HomePageFragment.currentSysTime + HomePageFragment.platformSysTime;
                    String rateEndDate = JSONHelper.getStringValue(projectInfo, "rateEndDate");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    try {
                        date = simpleDateFormat.parse(rateEndDate);
                    } catch (ParseException e) {
//                        e.printStackTrace();
                    }
                    long endTime = date.getTime();
                    long diff = endTime - current;
                    long second = diff / 1000 % 60;
                    long min = diff / (1000 * 60) % 60;
                    long hour = diff / (1000 * 60 * 60) % 24;
                    long day = diff / (1000 * 60 * 60 * 24);
                    timeRemainText.setText(Html.fromHtml("距离抽标结束还有<font color='#ff6a00'>" + day + "</font>天<font color='#ff6a00'>" + hour + "</font>小时<font color='#ff6a00'>" + min + "</font>分<font color='#ff6a00'>" + second + "</font>秒"));

            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.invest_btn:
                checkBind();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BIND_BANKCARD:
                if (resultCode == Activity.RESULT_OK) {
                    checkPayPWD();
                }
                break;
            case SET_PAY_PWD:
                if (resultCode == Activity.RESULT_OK) {
                    showPurchase();
                }
                break;
        }
    }

    PurchaseDialog.Callback callback = new PurchaseDialog.Callback() {
        @Override
        public void invest(int amount) {
            Intent intent = new Intent(getActivity(), PayActivity.class);
            try {
                JSONObject projectInfo = response.getJSONObject("projectInfo");
                intent.putExtra("projectInfo", projectInfo.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
            intent.putExtra("amount", "" + amount);
            startActivity(intent);
        }
    };

    public void setProjectInfo(JSONObject response) {
        this.response = response;
        Log.d("", response.toString());
        dataInitFinish = true;
        if (viewInitFinish) {
            initData();
        }
    }

    JSONObject projectInfo;

    private void initData() {
        try {
            projectInfo = response.getJSONObject("projectInfo");
            projectNameText.setText(JSONHelper.getStringValue(projectInfo, "projectName") + "（" + JSONHelper.getStringValue(projectInfo, "projectNo") + "）");
            if ("ZSBD".equals(JSONHelper.getStringValue(projectInfo, "projectType"))) {
                tag.setImageResource(R.mipmap.product_exclusive_bg);
            } else if ("XSLX".equals(JSONHelper.getStringValue(projectInfo, "projectType"))) {
                tag.setImageResource(R.mipmap.product_novice_bg);
            } else {
                tag.setVisibility(View.GONE);
            }
            rateText.setText(JSONHelper.getStringValue(projectInfo, "investRate"));
            termText.setText(JSONHelper.getStringValue(projectInfo, "investMent"));
            String maxInvTotalAmt = JSONHelper.getStringValue(projectInfo, "maxInvTotalAmt");
            financingAmountText.setText(maxInvTotalAmt + "元");
            String enablAmt = JSONHelper.getStringValue(projectInfo, "enablAmt");
            remainInvestText.setText(enablAmt + "元");
            minInvestAmountText.setText(JSONHelper.getStringValue(projectInfo, "minInvAmt") + "元");
            interestDateText.setText(JSONHelper.getStringValue(projectInfo, "rateDate"));
            dueDateText.setText(JSONHelper.getStringValue(projectInfo, "rateEndDate"));
            repaymentMethodText.setText(JSONHelper.getStringValue(projectInfo, "到期一次性还本付息"));
//            witnessOrgText.setText(JSONHelper.getStringValue(projectInfo, "agreement"));

            double enableInt = Double.parseDouble(enablAmt);
            double maxInt = Double.parseDouble(maxInvTotalAmt);
            int percent = 100 - (int) (enableInt * 100 / maxInt);
            progress.setMaxCount(100);
            progress.setCurrentCount(percent);

            String statusCode = JSONHelper.getStringValue(projectInfo, "statusCode");

            if ("YEPD".equals(statusCode)) {
                investBtn.setTextColor(getResources().getColor(R.color.full));
                investBtn.setBackgroundResource(R.color.full_bg);
                investBtn.setClickable(false);
                investBtn.setText(JSONHelper.getStringValue(projectInfo, "projectStatus"));
                setTask();
            } else if ("TZPC".equals(statusCode)) {
                investBtn.setClickable(true);
                setTask();
            } else {
                timeRemainText.setVisibility(View.GONE);
                investBtn.setTextColor(getResources().getColor(R.color.full));
                investBtn.setBackgroundResource(R.color.full_bg);
                investBtn.setClickable(false);
                investBtn.setText(JSONHelper.getStringValue(projectInfo, "projectStatus"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTask() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        }, 1000, 1000);
    }

    private void checkBind() {
        QueryBindBankcardHandler queryBindBankcardHandler = new QueryBindBankcardHandler(getActivity());
        queryBindBankcardHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    try {
                        JSONArray bankList = response.getJSONArray("list");
                        if (bankList.length() == 0) {
                            Intent intent = new Intent(getActivity(), BindBankCardActivity.class);
                            startActivityForResult(intent, BIND_BANKCARD);
                        } else {
                            checkPayPWD();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        queryBindBankcardHandler.execute();
    }

    private void checkPayPWD() {
        UserBaseInfoHandler userBaseInfoHandler = new UserBaseInfoHandler(getActivity(), true);
        userBaseInfoHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    if ("1".equals(JSONHelper.getStringValue(response, "payPassWordFlag"))) {
                        showPurchase();
                    } else {
                        Intent intent = new Intent(getActivity(), PayPasswordSetActivity.class);
                        startActivityForResult(intent, SET_PAY_PWD);
                    }
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        userBaseInfoHandler.execute();
    }

    private void showPurchase() {
        try {
            JSONObject projectInfo = response.getJSONObject("projectInfo");
            PurchaseDialog purchaseDialog = new PurchaseDialog(getActivity(), this.callback, JSONHelper.getStringValue(projectInfo, "projectNo"), (int) Double.parseDouble(JSONHelper.getStringValue(projectInfo, "enablAmt")), (int) Double.parseDouble(JSONHelper.getStringValue(projectInfo, "minInvAmt")));
            purchaseDialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
