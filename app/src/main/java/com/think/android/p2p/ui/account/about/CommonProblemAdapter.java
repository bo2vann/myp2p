package com.think.android.p2p.ui.account.about;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Think on 2017/11/18.
 */

public class CommonProblemAdapter extends BaseAdapter {

    ViewHolder viewHolder;

    Context context;
    List<JSONObject> dataList;

    public CommonProblemAdapter(Context context, List<JSONObject> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList == null ? null : dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.common_problem_item, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        JSONObject data = dataList.get(position);
        viewHolder.name.setText(JSONHelper.getStringValue(data, "dicName"));
        return convertView;
    }

    class ViewHolder {
        TextView name;
    }
}
