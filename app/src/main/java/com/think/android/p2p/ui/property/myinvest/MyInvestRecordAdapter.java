package com.think.android.p2p.ui.property.myinvest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.PullToRefreshAdapter;
import com.think.android.p2p.utils.DpPxUtils;

import org.json.JSONObject;

/**
 * Created by Think on 2017/10/14.
 */

public class MyInvestRecordAdapter extends PullToRefreshAdapter {

    ViewHolder viewHolder;

    @Override
    protected View initViews(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.my_invest_record_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.projectNameText = (TextView) convertView.findViewById(R.id.project_name_text);
            viewHolder.amountText = (TextView) convertView.findViewById(R.id.amount_text);
            viewHolder.timeText = (TextView) convertView.findViewById(R.id.time_text);
            viewHolder.statusText = (TextView) convertView.findViewById(R.id.status_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            convertView.setPadding(DpPxUtils.dpToPx(context, 10), DpPxUtils.dpToPx(context, 14), DpPxUtils.dpToPx(context, 10), DpPxUtils.dpToPx(context, 6));
        } else {
            convertView.setPadding(DpPxUtils.dpToPx(context, 10), DpPxUtils.dpToPx(context, 6), DpPxUtils.dpToPx(context, 10), DpPxUtils.dpToPx(context, 6));
        }

        JSONObject invest = dataList.get(position);

        viewHolder.projectNameText.setText(JSONHelper.getStringValue(invest, "projectName") + "【" + JSONHelper.getStringValue(invest, "projectNo") + "】");
        viewHolder.amountText.setText(JSONHelper.getStringValue(invest, "invAmount") + "元");
        viewHolder.timeText.setText(JSONHelper.getStringValue(invest, "invDate"));
        viewHolder.statusText.setText(JSONHelper.getStringValue(invest, "status"));

        return convertView;
    }

    class ViewHolder {
        TextView projectNameText;
        TextView amountText;
        TextView timeText;
        TextView statusText;
    }
}
