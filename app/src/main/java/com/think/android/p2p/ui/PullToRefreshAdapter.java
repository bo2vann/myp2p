package com.think.android.p2p.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Think on 2017/10/14.
 */

public abstract class PullToRefreshAdapter extends BaseAdapter {

    protected Context context;

    protected ArrayList<JSONObject> dataList;

    public PullToRefreshAdapter() {

    }

    public PullToRefreshAdapter(Context context, ArrayList<JSONObject> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void init(Context context, ArrayList<JSONObject> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initViews(position, convertView, parent);
    }

    protected abstract View initViews(int position, View convertView, ViewGroup parent);

}
