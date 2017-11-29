package com.think.android.p2p.ui.account.message;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.PullToRefreshAdapter;

import org.json.JSONObject;

/**
 * Created by Think on 2017/10/22.
 */

public class MineMsgAdapter extends PullToRefreshAdapter {

    ViewHolder viewHolder;

    @Override
    protected View initViews(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.mine_msg_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.titleText = (TextView) convertView.findViewById(R.id.title_text);
            viewHolder.timeText = (TextView) convertView.findViewById(R.id.time_text);
            viewHolder.contentText = (TextView) convertView.findViewById(R.id.content_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        JSONObject msg = dataList.get(position);
        viewHolder.titleText.setText(JSONHelper.getStringValue(msg, "title"));
        viewHolder.contentText.setText(Html.fromHtml(JSONHelper.getStringValue(msg, "msgContent")));
        viewHolder.timeText.setText(JSONHelper.getStringValue(msg, "createTime"));

        return convertView;
    }

    class ViewHolder {
        TextView titleText;
        TextView timeText;
        TextView contentText;
    }
}
