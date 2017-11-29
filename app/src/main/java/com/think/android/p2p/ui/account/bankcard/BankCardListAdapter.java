package com.think.android.p2p.ui.account.bankcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amarsoft.support.android.utils.JSONHelper;
import com.amarsoft.support.android.utils.MaskUtil;
import com.think.android.p2p.R;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Think on 2017/10/15.
 */

public class BankCardListAdapter extends BaseAdapter {

    Context context;

    ArrayList<JSONObject> cardList;

    ViewHolder viewHolder;

    public BankCardListAdapter(Context context, ArrayList<JSONObject> cardList) {
        this.context = context;
        this.cardList = cardList;
    }

    @Override
    public int getCount() {
        return cardList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position < cardList.size())
            return cardList.get(position);
        else return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position < cardList.size()) {
            if (convertView == null || convertView.getTag() == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.bankcard_manage_item, null);
                viewHolder = new ViewHolder();
                viewHolder.bankImg = (ImageView) convertView.findViewById(R.id.bank_img);
                viewHolder.bankText = (TextView) convertView.findViewById(R.id.bank_text);
                viewHolder.bankcardText = (TextView) convertView.findViewById(R.id.bankcard_text);
                viewHolder.bankcardLayout = (LinearLayout) convertView.findViewById(R.id.bankcard_layout);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            JSONObject bankcard = cardList.get(position);

            int bankId = context.getResources().getIdentifier("banklogo_" + JSONHelper.getStringValue(bankcard, "bankNo"), "mipmap", context.getPackageName());
            viewHolder.bankImg.setImageResource(bankId);
            int bankbgId = context.getResources().getIdentifier("bankbg_" + JSONHelper.getStringValue(bankcard, "bankNo"), "drawable", context.getPackageName());
            viewHolder.bankcardLayout.setBackgroundResource(bankbgId);
            viewHolder.bankText.setText(JSONHelper.getStringValue(bankcard, "bankName"));
            viewHolder.bankcardText.setText(MaskUtil.maskCardNo(JSONHelper.getStringValue(bankcard, "cardNo")));
            return convertView;
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.bankcard_item_add, null);
            return convertView;
        }
    }

    class ViewHolder {
        LinearLayout bankcardLayout;
        ImageView bankImg;
        TextView bankText;
        TextView bankcardText;
    }

}
