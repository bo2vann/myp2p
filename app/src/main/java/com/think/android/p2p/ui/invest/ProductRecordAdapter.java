package com.think.android.p2p.ui.invest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Think on 2017/11/4.
 */

public class ProductRecordAdapter extends BaseAdapter {

    ViewHolder viewHolder;

    Context context;
    ArrayList<JSONObject> data;

    public ProductRecordAdapter(Context context, ArrayList<JSONObject> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data == null ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_invest_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.nameText = (TextView) convertView.findViewById(R.id.name_text);
            viewHolder.timeText = (TextView) convertView.findViewById(R.id.time_text);
            viewHolder.amountText = (TextView) convertView.findViewById(R.id.amount_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        JSONObject record = data.get(position);

        viewHolder.nameText.setText(JSONHelper.getStringValue(record, "name"));
        viewHolder.timeText.setText(JSONHelper.getStringValue(record, "date"));
        viewHolder.amountText.setText(JSONHelper.getStringValue(record, "amount"));
        return convertView;
    }

    class ViewHolder {
        TextView nameText;
        TextView timeText;
        TextView amountText;
    }
}
