package com.think.android.p2p.ui.invest;

import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.PullToRefreshAdapter;
import com.think.android.p2p.ui.views.LineProgressView;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Created by Think on 2017/10/14.
 */
public class ProductListAdapter extends PullToRefreshAdapter {

    private static final String TAG = "ProductListAdapter";

    ViewHolder viewHolder;

    @Override
    protected View initViews(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tag = (ImageView) convertView.findViewById(R.id.tag);
            viewHolder.tag1 = (ImageView) convertView.findViewById(R.id.tag1);
            viewHolder.projectNameText = (TextView) convertView.findViewById(R.id.project_name_text);
            viewHolder.rateText = (TextView) convertView.findViewById(R.id.rate_text);
            viewHolder.termText = (TextView) convertView.findViewById(R.id.term_text);
            viewHolder.investBtn = (TextView) convertView.findViewById(R.id.invest_btn);
            viewHolder.statusText = (TextView) convertView.findViewById(R.id.status_text);
            viewHolder.progress = (LineProgressView) convertView.findViewById(R.id.progress);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        JSONObject product = dataList.get(position);

        String projectType = JSONHelper.getStringValue(product, "type");
        if ("ZSBD".equals(projectType)) {
            viewHolder.tag.setVisibility(View.VISIBLE);
            viewHolder.tag.setImageResource(R.mipmap.product_exclusive_bg);
        } else if ("XSLX".equals(projectType)) {
            viewHolder.tag.setVisibility(View.VISIBLE);
            viewHolder.tag.setImageResource(R.mipmap.product_novice_bg);
        } else {
            viewHolder.tag.setVisibility(View.GONE);
        }

        String invTap = JSONHelper.getStringValue(product, "invTap");
        if ("GWSY".equals(invTap)) {
            viewHolder.tag1.setVisibility(View.VISIBLE);
            viewHolder.tag1.setImageResource(R.mipmap.product_high);
        } else if ("HBCC".equals(invTap)) {
            viewHolder.tag1.setVisibility(View.VISIBLE);
            viewHolder.tag1.setImageResource(R.mipmap.product_hot);
        } else {
            viewHolder.tag1.setVisibility(View.GONE);
        }

        viewHolder.projectNameText.setText(JSONHelper.getStringValue(product, "projectName") + "（" + JSONHelper.getStringValue(product, "projectNo") + "）");
        viewHolder.rateText.setText(JSONHelper.getStringValue(product, "investRate"));
        viewHolder.termText.setText(JSONHelper.getStringValue(product, "investMent"));
        String enablAmt = JSONHelper.getStringValue(product, "enablAmt");

        String statusCode = JSONHelper.getStringValue(product, "statusCode");

        if ("YEPD".equals(statusCode)) {
            long current = System.currentTimeMillis() - ProductListFragment.currentSysTime + ProductListFragment.platformSysTime;
            String readyEnddate = JSONHelper.getStringValue(product, "readyEnddate");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            try {
                date = simpleDateFormat.parse(readyEnddate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long endTime = date.getTime();
            if (endTime <= current) {
                viewHolder.investBtn.setBackgroundResource(R.drawable.btn_bg2);
                viewHolder.investBtn.setTextColor(context.getResources().getColor(android.R.color.white));
                viewHolder.investBtn.setText(String.format(context.getResources().getString(R.string.can_invest_amount_yuan), enablAmt));
            } else {
                viewHolder.investBtn.setBackgroundResource(R.drawable.preheat_invest_bg);
                viewHolder.investBtn.setTextColor(context.getResources().getColor(R.color.preheat));
                long diff = endTime - current;
                long second = diff / 1000 % 60;
                long min = diff / (1000 * 60) % 60;
                long hour = diff / (1000 * 60 * 60) % 24;
                long day = diff / (1000 * 60 * 60 * 24);
                viewHolder.investBtn.setText(Html.fromHtml("距离开始还有<font color='#ff6a00'>" + day + "</font>天<font color='#ff6a00'>" + hour + "</font>小时<font color='#ff6a00'>" + min + "</font>分<font color='#ff6a00'>" + second + "</font>秒"));
            }
        } else if ("TZPC".equals(statusCode)) {
            viewHolder.investBtn.setBackgroundResource(R.drawable.btn_bg2);
            viewHolder.investBtn.setTextColor(context.getResources().getColor(android.R.color.white));
            viewHolder.investBtn.setText(String.format(context.getResources().getString(R.string.can_invest_amount_yuan), enablAmt));
        } else {
            viewHolder.investBtn.setBackgroundResource(R.drawable.full_invest_bg);
            viewHolder.investBtn.setTextColor(context.getResources().getColor(R.color.full));
            viewHolder.investBtn.setText(JSONHelper.getStringValue(product, "projectStatus"));
        }


        String maxInvTotalAmt = JSONHelper.getStringValue(product, "maxInvTotalAmt");
        double enableInt = Double.parseDouble(enablAmt);
        double maxInt = Double.parseDouble(maxInvTotalAmt);
        int percent = 100 - (int) (enableInt * 100 / maxInt);
        viewHolder.statusText.setText(String.format(context.getResources().getString(R.string.remain), percent + "%"));
        viewHolder.progress.setMaxCount(100);
        viewHolder.progress.setCurrentCount(percent);

        viewHolder.investBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        InvestActivity.class);
                JSONObject product = dataList.get(position);
                String projectNo = JSONHelper.getStringValue(product, "projectNo");
                intent.putExtra("projectNo", projectNo);
                context.startActivity(intent);
            }
        });
        return convertView;
    }


    private class ViewHolder {
        ImageView tag;
        ImageView tag1;
        TextView projectNameText;
        TextView rateText;
        TextView termText;
        TextView investBtn;
        TextView statusText;
        LineProgressView progress;
    }
}
