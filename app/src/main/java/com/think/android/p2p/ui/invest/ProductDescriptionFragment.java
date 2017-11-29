package com.think.android.p2p.ui.invest;

import android.view.View;
import android.widget.TextView;

import com.amarsoft.support.android.ui.CommonFragment;
import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目详情-项目说明说
 * Created by Think on 2017/10/15.
 */

public class ProductDescriptionFragment extends CommonFragment {

    JSONObject data;
    boolean initFinish = false;

    TextView projectNameText;
    TextView projectInvestTermText;
    TextView currencyText;
    TextView productTypeText;
    TextView expectedAnnualizedReturnText;
    TextView financingTotalLendAmountText;
    TextView fundraiseTimeText;
    TextView interestDateText;
    TextView dueDateText;
    TextView minInvestUnit_text;
    TextView holidayText;
    TextView taxText;
    TextView profitCalcModeText;
    TextView witnessOrgText;
    TextView payClearingOrgText;

    public ProductDescriptionFragment() {
        super(R.layout.fragment_product_description);
    }


    @Override
    protected void initViews() {
        super.initViews();

        View view = getView();
        if (view == null) return;

        projectNameText = (TextView) view.findViewById(R.id.project_name_text);
        projectInvestTermText = (TextView) view.findViewById(R.id.project_invest_term_text);
        currencyText = (TextView) view.findViewById(R.id.currency_text);
        productTypeText = (TextView) view.findViewById(R.id.product_type_text);
        expectedAnnualizedReturnText = (TextView) view.findViewById(R.id.expected_annualized_return_text);
        financingTotalLendAmountText = (TextView) view.findViewById(R.id.financing_total_lend_amount_text);
        fundraiseTimeText = (TextView) view.findViewById(R.id.fundraise_time_text);
        interestDateText = (TextView) view.findViewById(R.id.interest_date_text);
        dueDateText = (TextView) view.findViewById(R.id.due_date_text);
        minInvestUnit_text = (TextView) view.findViewById(R.id.min_invest_unit_text);
        holidayText = (TextView) view.findViewById(R.id.holiday_text);
        taxText = (TextView) view.findViewById(R.id.tax_text);
        profitCalcModeText = (TextView) view.findViewById(R.id.profit_calc_mode_text);
        witnessOrgText = (TextView) view.findViewById(R.id.witness_org_text);
        payClearingOrgText = (TextView) view.findViewById(R.id.pay_clearing_org_text);

        initFinish = true;
        if (data != null) initData();
    }

    void initData() {

        JSONObject projectInfo;
        try {
            projectInfo = data.getJSONObject("projectInfo");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        projectNameText.setText(JSONHelper.getStringValue(projectInfo, "projectName"));
        projectInvestTermText.setText(JSONHelper.getStringValue(projectInfo, "investMent"));
        currencyText.setText("人民币");
        productTypeText.setText("固定期限网络投融资产品");
        expectedAnnualizedReturnText.setText(JSONHelper.getStringValue(projectInfo, "investRate"));
        financingTotalLendAmountText.setText(JSONHelper.getStringValue(projectInfo, "maxInvTotalAmt"));
        // dueData - startDueData
        fundraiseTimeText.setText(JSONHelper.getStringValue(projectInfo, "startDueDate") + "至" + JSONHelper.getStringValue(projectInfo, "dueDate"));
        interestDateText.setText(JSONHelper.getStringValue(projectInfo, "rateDate"));
        dueDateText.setText(JSONHelper.getStringValue(projectInfo, "rateEndDate"));
        minInvestUnit_text.setText(JSONHelper.getStringValue(projectInfo, "minInvAmt") + "元起投，倍数递增");
        holidayText.setText("本项目节假日期限可投标");
        taxText.setText("投资收益的应纳税款由投资人自行申报及缴纳。");
        profitCalcModeText.setText("期末收益=投资本金X预期年化收益率X产品存续天数/360");
        witnessOrgText.setText("浙江龙湾农村商业银行股份有限公司");
        payClearingOrgText.setText("通联支付有限公司");
    }

    public void setProjectInfo(JSONObject data) {
        this.data = data;
        if (initFinish) initData();
    }
}
