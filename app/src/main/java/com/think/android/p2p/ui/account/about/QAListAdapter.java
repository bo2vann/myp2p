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

public class QAListAdapter extends BaseAdapter {

    ViewHolder viewHolder;

    Context context;
    List<JSONObject> dataList;

    public QAListAdapter(Context context, List<JSONObject> dataList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.qa_item, null);
            viewHolder = new ViewHolder();
            viewHolder.question = (TextView) convertView.findViewById(R.id.question);
            viewHolder.answer = (TextView) convertView.findViewById(R.id.answer);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        JSONObject data = dataList.get(position);
        viewHolder.question.setText(JSONHelper.getStringValue(data, "question"));
        viewHolder.answer.setText(JSONHelper.getStringValue(data, "answer"));
        return convertView;
    }

    class ViewHolder {
        TextView question;
        TextView answer;
    }
}
