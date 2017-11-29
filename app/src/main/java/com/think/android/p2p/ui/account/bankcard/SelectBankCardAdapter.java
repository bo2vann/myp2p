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
 * 选择银行卡适配器
 * Created by Think on 2017/10/15.
 */

public class SelectBankCardAdapter extends BaseAdapter {

    Context context;

    ArrayList<JSONObject> cardList;

    ViewHolder viewHolder;

    public SelectBankCardAdapter(Context context, ArrayList<JSONObject> cardList) {
        this.context = context;
        this.cardList = cardList;
    }

    @Override
    public int getCount() {
        return cardList == null ? 0 : cardList.size();
    }

    @Override
    public Object getItem(int position) {
        return cardList == null ? null : cardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
    }

    class ViewHolder {
        LinearLayout bankcardLayout;
        ImageView bankImg;
        TextView bankText;
        TextView bankcardText;
    }

}
