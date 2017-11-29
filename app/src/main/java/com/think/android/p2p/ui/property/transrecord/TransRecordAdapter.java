package com.think.android.p2p.ui.property.transrecord;

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

public class TransRecordAdapter extends PullToRefreshAdapter {

    ViewHolder viewHolder;

    @Override
    protected View initViews(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.trans_record_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.actionText = (TextView) convertView.findViewById(R.id.action_text);
            viewHolder.amounText = (TextView) convertView.findViewById(R.id.amount_text);
            viewHolder.timeText = (TextView) convertView.findViewById(R.id.time_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        JSONObject item = dataList.get(position);

        viewHolder.actionText.setText(JSONHelper.getStringValue(item, "tranTypeName"));
        viewHolder.amounText.setText(JSONHelper.getStringValue(item, "amount"));
        viewHolder.timeText.setText(JSONHelper.getStringValue(item, "createTime"));
        return convertView;
    }

    class ViewHolder {
        TextView actionText;
        TextView amounText;
        TextView timeText;
    }
}
