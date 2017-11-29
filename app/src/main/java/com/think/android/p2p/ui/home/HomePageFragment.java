package com.think.android.p2p.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amarsoft.support.android.ui.CommonFragment;
import com.amarsoft.support.android.utils.JSONHelper;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.think.android.p2p.R;
import com.think.android.p2p.base.UserInfoUtils;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.PullToRefreshFragment;
import com.think.android.p2p.ui.account.about.PlatformIntroduceActivity;
import com.think.android.p2p.ui.account.about.SecurityGuaranteeActivity;
import com.think.android.p2p.ui.account.invite.InviteActivity;
import com.think.android.p2p.ui.account.message.MessageActivity;
import com.think.android.p2p.ui.account.message.MessageListHandler;
import com.think.android.p2p.ui.ads.AdsImgScroll;
import com.think.android.p2p.ui.ads.VolleyBitmapLruCache;
import com.think.android.p2p.ui.invest.InvestActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 主页-首页
 * Created by Think on 2017/10/11.
 */
public class HomePageFragment extends PullToRefreshFragment implements View.OnClickListener {

    public static Long platformSysTime;
    public static Long currentSysTime;

    AdsImgScroll adViewPager;
    LinearLayout inviteLayout;
    LinearLayout safeLayout;
    LinearLayout messageLayout;

    LinearLayout announceLayout;
    TextView sysAnnounce;

    List<View> adViewList;

    public HomePageFragment() {
        super(R.layout.fragment_homepage);
        this.adapter = "com.think.android.p2p.ui.invest.ProductListAdapter";
    }

    @Override
    protected void initViews() {

        View view = getView();
        if (view == null) return;

        super.initViews();

    }

    @Override
    protected void initListView() {
        View header = getActivity().getLayoutInflater().inflate(R.layout.homepage_header, null);
        dataListView.addHeaderView(header);

        adViewPager = (AdsImgScroll) header.findViewById(R.id.ad_viewpager);

        inviteLayout = (LinearLayout) header.findViewById(R.id.invite_layout);
        safeLayout = (LinearLayout) header.findViewById(R.id.safe_layout);
        messageLayout = (LinearLayout) header.findViewById(R.id.message_layout);

        inviteLayout.setOnClickListener(this);
        safeLayout.setOnClickListener(this);
        messageLayout.setOnClickListener(this);

        announceLayout = (LinearLayout) header.findViewById(R.id.announce_layout);
        sysAnnounce = (TextView) header.findViewById(R.id.system_announce);
        querySystemNotice();
        loadAdvImg();

        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = (int) (position < id ? position : id);
                Intent intent = new Intent(getActivity(),
                        InvestActivity.class);
                JSONObject object = dataList.get(index);
                String projectNo = "";
                try {
                    projectNo = object.getString("projectNo");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if ("".equals(projectNo)) {
                    return;
                }
                intent.putExtra("projectNo", projectNo);
                startActivity(intent);
            }
        });
        super.initListView();
        requestData(true);
    }

    @Override
    protected void requestData(boolean needLoadingDialog) {
        isLoading = false;
        HomeProductListHandler homeProductListHandler = new HomeProductListHandler(getActivity(), needLoadingDialog);
        homeProductListHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                isLoading = true;
                if ("SUCCESS".equals(object)) {
                    String sysDate = JSONHelper.getStringValue(response, "sysDate");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    try {
                        date = simpleDateFormat.parse(sysDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    platformSysTime = date.getTime();
                    currentSysTime = System.currentTimeMillis();
                    try {
                        isAllData = true;
                        adapterNotify(response.getJSONArray("investsList"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        homeProductListHandler.execute();
    }

    Timer timer;

    @Override
    public void onStart() {
        super.onStart();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }, 1000, 1000);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (pullToRefreshAdapter != null) {
                    pullToRefreshAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    /**
     * 加载广告图片
     */
    private void loadAdvImg() {
        RequestQueue imageRequestQueue = Volley.newRequestQueue(getActivity());
        ImageLoader imageLoader = new ImageLoader(imageRequestQueue,
                new VolleyBitmapLruCache(getActivity()));

        ArrayList<JSONObject> adsJsonList = new ArrayList<>();
        try {
            SharedPreferences sp = getActivity().getSharedPreferences("Advertise", 0);
            JSONObject adsObject = new JSONObject(sp.getString("ads", ""));
            if (adsObject.isNull("bannelList")
                    || adsObject.get("bannelList").toString().length() == 0) {
                adsJsonList = null;
            } else {
                JSONArray jsonArray = adsObject.getJSONArray("bannelList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    adsJsonList.add(jsonArray.getJSONObject(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            adsJsonList = null;
        }

        adViewList = new ArrayList<>();

        try {
            if (adsJsonList != null) {
                for (int i = 0; i < adsJsonList.size(); i++) {
                    JSONObject jsonObject = adsJsonList.get(i);
                    NetworkImageView imageView = new NetworkImageView(
                            getActivity());
                    imageView.setDefaultImageResId(R.mipmap.banner1);
                    String picUrl = JSONHelper.getStringValue(jsonObject, "picUrl");
                    if (Patterns.WEB_URL.matcher(picUrl).matches()) {
                        imageView.setImageUrl(picUrl, imageLoader);
                    }
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    adViewList.add(imageView);
                }
                // 开始滚动
                adViewPager.start(getActivity(), adViewList, 4000,
                        (LinearLayout) getView().findViewById(R.id.dot_layout),
                        R.layout.ad_bottom_item, R.id.ad_item_v,
                        R.drawable.dot_focused, R.drawable.dot_normal);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.invite_layout:
                toActivity(InviteActivity.class);
                break;
            case R.id.safe_layout:
                toActivity(SecurityGuaranteeActivity.class);
                break;
            case R.id.message_layout:
                toActivity(MessageActivity.class);
                break;
        }
    }

    private void toActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    private void querySystemNotice() {
        HomePageMessageHandler homePageMessageHandler = new HomePageMessageHandler(getActivity());
        homePageMessageHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    try {
                        JSONArray list = response.getJSONArray("list");
                        if (list.length() > 0) {
                            sysAnnounce.setText(JSONHelper.getStringValue(list.getJSONObject(0), "title"));
                        } else {
                            announceLayout.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        homePageMessageHandler.execute();
    }
}
