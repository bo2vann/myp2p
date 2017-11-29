package com.think.android.p2p.ui.account;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amarsoft.support.android.ui.CommonFragment;
import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.account.about.AboutActivity;
import com.think.android.p2p.ui.account.bankcard.BankCardManageActivity;
import com.think.android.p2p.ui.account.bankcard.QueryBindBankcardHandler;
import com.think.android.p2p.ui.account.invite.InviteActivity;
import com.think.android.p2p.ui.account.message.MessageActivity;
import com.think.android.p2p.ui.account.message.MessageCountHandler;
import com.think.android.p2p.ui.account.personalinfo.PersonalInfoActivity;
import com.think.android.p2p.ui.account.security.SecurityActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import com.makeramen.roundedimageview.RoundedImageView;

/**
 * 主页-我的
 * Created by Think on 2017/10/10.
 */
public class MineFragment extends CommonFragment implements View.OnClickListener {

    RoundedImageView headImg;
    TextView mobileText;
    TextView realnameStatusText;
    TextView bindText;
    TextView messageCountText;

    LinearLayout bankcardLayout;
    LinearLayout securityLayout;
    LinearLayout personalInfoLayout;
    LinearLayout messageCenterLayout;
    LinearLayout aboutUsLayout;
    LinearLayout recommendToFriendsLayout;

    public MineFragment() {
        super(R.layout.fragment_mine);
    }

    @Override
    protected void initViews() {
        super.initViews();
        View v = getView();
        if (v == null) return;
        bankcardLayout = (LinearLayout) v.findViewById(R.id.bankcard_layout);
        securityLayout = (LinearLayout) v.findViewById(R.id.security_layout);
        personalInfoLayout = (LinearLayout) v.findViewById(R.id.personal_info_layout);
        messageCenterLayout = (LinearLayout) v.findViewById(R.id.message_center_layout);
        aboutUsLayout = (LinearLayout) v.findViewById(R.id.about_us_layout);
        recommendToFriendsLayout = (LinearLayout) v.findViewById(R.id.recommend_to_friends_layout);

        bankcardLayout.setOnClickListener(this);
        securityLayout.setOnClickListener(this);
        personalInfoLayout.setOnClickListener(this);
        messageCenterLayout.setOnClickListener(this);
        aboutUsLayout.setOnClickListener(this);
        recommendToFriendsLayout.setOnClickListener(this);

        headImg = (RoundedImageView) v.findViewById(R.id.head_img);
        mobileText = (TextView) v.findViewById(R.id.mobile_text);
        realnameStatusText = (TextView) v.findViewById(R.id.realname_status_text);
        bindText = (TextView) v.findViewById(R.id.bind_text);
        messageCountText = (TextView) v.findViewById(R.id.message_count_text);

        initData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            initData();
        }
    }

    public void refresh() {
        initData();
    }

    private void initData() {
        UserInfoUtils userInfoUtils = new UserInfoUtils(getActivity());
        mobileText.setText(userInfoUtils.getMaskMobile());

        requestBankcard();
        queryRealnameStatus();
        queryMessageCount();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bankcard_layout:
                toActivity(BankCardManageActivity.class);
                break;
            case R.id.security_layout:
                toActivity(SecurityActivity.class);
                break;
            case R.id.personal_info_layout:
                toActivity(PersonalInfoActivity.class);
                break;
            case R.id.message_center_layout:
                toActivity(MessageActivity.class);
                break;
            case R.id.about_us_layout:
                toActivity(AboutActivity.class);
                break;
            case R.id.recommend_to_friends_layout:
                toActivity(InviteActivity.class);
                break;
        }
    }

    private void toActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    private void requestBankcard() {
        QueryBindBankcardHandler queryBindBankcardHandler = new QueryBindBankcardHandler(getContext());
        queryBindBankcardHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    JSONArray bankcardArray = (JSONArray) JSONHelper.getValue(response, "list");
                    if (bankcardArray.length() == 0) {
                        bindText.setText(R.string.unbind_card);
                    } else {
                        bindText.setText(R.string.bind_card);
                    }
                }
            }
        });
        queryBindBankcardHandler.execute();
    }

    private void queryRealnameStatus() {
        UserBaseInfoHandler userBaseInfoHandler = new UserBaseInfoHandler(getActivity(), true);
        userBaseInfoHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    if ("1".equals(JSONHelper.getStringValue(response, "cerFlag"))) {
                        realnameStatusText.setText(R.string.has_auth);
                    } else {
                        realnameStatusText.setText(R.string.un_auth);
                    }
                }
            }
        });
        userBaseInfoHandler.execute();
    }

    private void queryMessageCount() {
        MessageCountHandler messageCountHandler = new MessageCountHandler(getActivity());
        messageCountHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    String num = JSONHelper.getStringValue(response, "messageNum");
                    if (Integer.parseInt(num) > 0) {
                        messageCountText.setText(num + "条新消息");
                    } else {
                        messageCountText.setText("");
                    }
                }
            }
        });
        messageCountHandler.execute();
    }
}
