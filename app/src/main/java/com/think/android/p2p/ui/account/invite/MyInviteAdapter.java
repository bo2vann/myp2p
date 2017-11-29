package com.think.android.p2p.ui.account.invite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.PullToRefreshAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Think on 2017/11/25.
 */

public class MyInviteAdapter extends PullToRefreshAdapter {

    ViewHolder viewHolder;

    public MyInviteAdapter(Context context, ArrayList<JSONObject> dataList) {
        super(context, dataList);
    }

    @Override
    protected View initViews(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.my_invite_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mobile = (TextView) convertView.findViewById(R.id.mobile);
            viewHolder.registerTime = (TextView) convertView.findViewById(R.id.register_time);
            viewHolder.investTime = (TextView) convertView.findViewById(R.id.invest_time);
            viewHolder.awardAmount = (TextView) convertView.findViewById(R.id.amount);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        JSONObject invite = dataList.get(position);

        viewHolder.mobile.setText(JSONHelper.getStringValue(invite, "mobile"));
        viewHolder.registerTime.setText(JSONHelper.getStringValue(invite, "registime"));
        viewHolder.investTime.setText(JSONHelper.getStringValue(invite, "invtime"));
        viewHolder.awardAmount.setText(JSONHelper.getStringValue(invite, "amount"));

        return convertView;
    }

    class ViewHolder {
        TextView mobile;
        TextView registerTime;
        TextView investTime;
        TextView awardAmount;
    }
}
