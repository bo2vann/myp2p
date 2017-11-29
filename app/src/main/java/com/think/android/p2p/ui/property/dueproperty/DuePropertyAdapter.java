package com.think.android.p2p.ui.property.dueproperty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.PullToRefreshAdapter;
import com.think.android.p2p.ui.property.UserProjectListHandler;

import org.json.JSONObject;

/**
 * 待回收/已回收资产
 * Created by Think on 2017/10/15.
 */

public class DuePropertyAdapter extends PullToRefreshAdapter {

    String status;

    ViewHolder viewHolder;

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    protected View initViews(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.due_property_item, null);
            viewHolder = new ViewHolder();
            viewHolder.projectNameText = (TextView) convertView.findViewById(R.id.project_name_text);
            viewHolder.investAmountText = (TextView) convertView.findViewById(R.id.invest_amount_text);
            viewHolder.profitTittle = (TextView) convertView.findViewById(R.id.profit_title);
            viewHolder.profitText = (TextView) convertView.findViewById(R.id.profit_text);
            viewHolder.dueDateText = (TextView) convertView.findViewById(R.id.due_date_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        JSONObject project = dataList.get(position);

        viewHolder.projectNameText.setText(JSONHelper.getStringValue(project, "projectName") + "【" + JSONHelper.getStringValue(project, "projectNo") + "】");
        viewHolder.investAmountText.setText(JSONHelper.getStringValue(project, "invAmount"));
        if (UserProjectListHandler.UNDUE_PROPERTY.equals(status)) {
            viewHolder.profitTittle.setText(R.string.due_profit_yuan);
            viewHolder.profitText.setText(JSONHelper.getStringValue(project, "waitIncomeAmt"));
        } else if (UserProjectListHandler.DUED_PROPERTY.equals(status)){
            viewHolder.profitTittle.setText(R.string.dued_profit_yuan);
            viewHolder.profitText.setText(JSONHelper.getStringValue(project, "takeBackIncomeAmt"));
        }
        viewHolder.dueDateText.setText(JSONHelper.getStringValue(project, "invOverDate"));
        return convertView;
    }

    class ViewHolder {
        TextView projectNameText;
        TextView investAmountText;
        TextView profitTittle;
        TextView profitText;
        TextView dueDateText;
    }
}
