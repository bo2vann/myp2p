package com.think.android.p2p.ui.invest;

import android.view.View;
import android.widget.ListView;

import com.amarsoft.support.android.ui.CommonFragment;
import com.think.android.p2p.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 项目详情-投资列表记录
 * Created by Think on 2017/10/15.
 */

public class ProductRecordFragment extends CommonFragment {

    JSONArray data;

    ListView listView;
    ArrayList<JSONObject> dataList;
    ProductRecordAdapter productRecordAdapter;

    public ProductRecordFragment() {
        super(R.layout.fragment_product_record);
    }

    @Override
    protected void initViews() {
        super.initViews();

        View view = getView();
        if (view == null) return;

        listView = (ListView) view.findViewById(R.id.list);
        if (dataList == null) dataList = new ArrayList();
        productRecordAdapter = new ProductRecordAdapter(getActivity(), dataList);
        listView.setAdapter(productRecordAdapter);
    }

    public void setProjectInfo(JSONObject data) {
        if (dataList == null) dataList = new ArrayList();
        try {
            this.data = data.getJSONArray("list");
            dataList.clear();
            for (int i = 0; i < this.data.length(); i++) {
                dataList.add((JSONObject) this.data.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (productRecordAdapter != null)
            productRecordAdapter.notifyDataSetChanged();
    }
}
