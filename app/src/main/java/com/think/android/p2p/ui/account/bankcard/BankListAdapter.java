package com.think.android.p2p.ui.account.bankcard;

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
 * Created by Think on 2017/11/2.
 */

public class BankListAdapter extends BaseAdapter {

    Context context;
    ArrayList<JSONObject> bankList;

    ViewHolder viewHolder;

    public BankListAdapter(Context context, ArrayList<JSONObject> bankList) {
        super();
        this.context = context;
        this.bankList = bankList;
    }

    @Override
    public int getCount() {
        return bankList.size();
    }

    @Override
    public Object getItem(int position) {
        return bankList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_bank, null);
            viewHolder = new ViewHolder();
            viewHolder.bankName = (TextView) convertView.findViewById(R.id.bank);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        JSONObject bank = bankList.get(position);
        viewHolder.bankName.setText(JSONHelper.getStringValue(bank, "bankName"));
        return convertView;
    }

    class ViewHolder {
        TextView bankName;
    }
}
