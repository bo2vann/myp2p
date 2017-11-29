package com.think.android.p2p.ui.property.dueprofit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.PullToRefreshAdapter;

import org.json.JSONObject;

/**
 * Created by Think on 2017/10/15.
 */

public class DueProfitAdapter extends PullToRefreshAdapter {

    ViewHolder viewHolder;

    @Override
    protected View initViews(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.due_profit_item, null);
            viewHolder = new ViewHolder();
            viewHolder.projectNameText = (TextView) convertView.findViewById(R.id.project_name_text);
            viewHolder.amountText = (TextView) convertView.findViewById(R.id.amount_text);
            viewHolder.timeText = (TextView) convertView.findViewById(R.id.time_text);
            viewHolder.statusText = (TextView) convertView.findViewById(R.id.status_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        JSONObject project = dataList.get(position);
        viewHolder.projectNameText.setText(JSONHelper.getStringValue(project, "projectName") + "【" + JSONHelper.getStringValue(project, "projectNo") + "】");
        viewHolder.amountText.setText(JSONHelper.getStringValue(project, "waitIncomeAmt") + "元");
        viewHolder.timeText.setText(JSONHelper.getStringValue(project, "invDate"));
        viewHolder.statusText.setText(JSONHelper.getStringValue(project, "status"));
        return convertView;
    }

    class ViewHolder {
        TextView projectNameText;
        TextView amountText;
        TextView timeText;
        TextView statusText;
    }
}
