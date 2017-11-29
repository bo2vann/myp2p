package com.think.android.p2p.ui.invest;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.think.android.p2p.R;

/**
 * Created by Think on 2017/11/9.
 */

public class WitnessAdapter extends BaseAdapter {

    Context context;
    String[] witnessText;
    TypedArray witnessImg;

    ViewHolder viewHolder;

    public WitnessAdapter(Context context, String[] witnessText, TypedArray witnessImg) {
        this.context = context;
        this.witnessText = witnessText;
        this.witnessImg = witnessImg;
    }

    @Override
    public int getCount() {
        return witnessText.length;
    }

    @Override
    public Object getItem(int position) {
        return witnessText[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.witness_item, null);
            viewHolder = new ViewHolder();
            viewHolder.witnessImg = (ImageView) convertView.findViewById(R.id.witness_img);
            viewHolder.witnessText = (TextView) convertView.findViewById(R.id.witness_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.witnessImg.setImageResource(witnessImg.getResourceId(position, R.mipmap.witness_1));
        viewHolder.witnessText.setText(witnessText[position]);
        return convertView;
    }

    class ViewHolder {
        ImageView witnessImg;
        TextView witnessText;
    }
}
