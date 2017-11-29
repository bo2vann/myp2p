package com.think.android.p2p.ui.invest;

import android.content.res.TypedArray;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.amarsoft.support.android.ui.CommonFragment;
import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目详情-项目详情
 * Created by Think on 2017/10/15.
 */

public class ProductDetailFragment extends CommonFragment {

    JSONObject data;

    boolean initStatus = false;

    TextView productIntroduceText;
    GridView witnessGrid;

    public ProductDetailFragment() {
        super(R.layout.fragment_product_detail);
    }

    @Override
    protected void initViews() {
        super.initViews();

        View view = getView();
        if (view == null) return;

        productIntroduceText = (TextView) view.findViewById(R.id.product_introduce_text);
        witnessGrid = (GridView) view.findViewById(R.id.witness_grid);
        initStatus = true;
        if (data != null) {
            initData();
        }

    }

    public void setProjectInfo(JSONObject data) {
        this.data = data;
        if (initStatus) initData();
    }

    private void initData() {
        String[] witnessText;
        TypedArray witnessImg;
        JSONObject projectInfo;
        try {
            projectInfo = data.getJSONObject("projectInfo");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        if ("1".equals(JSONHelper.getStringValue(projectInfo, "financeType"))) {
            witnessText = getResources().getStringArray(R.array.witness_text_individual);
            witnessImg = getResources().obtainTypedArray(R.array.witness_img_individual);
        } else {
            witnessText = getResources().getStringArray(R.array.witness_text_enterprise);
            witnessImg = getResources().obtainTypedArray(R.array.witness_img_enterprise);
        }
        WitnessAdapter witnessAdapter = new WitnessAdapter(getActivity(), witnessText, witnessImg);
        witnessGrid.setAdapter(witnessAdapter);
    }
}
