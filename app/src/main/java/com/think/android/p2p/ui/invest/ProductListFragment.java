package com.think.android.p2p.ui.invest;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.CommonRemoteHandler;
import com.think.android.p2p.ui.PullToRefreshFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 主页-投资列表
 * Created by Think on 2017/10/10.
 */
public class ProductListFragment extends PullToRefreshFragment {

    private static final String TAG = "ProductListFragment";

    public static long platformSysTime;
    public static long currentSysTime;

    public ProductListFragment() {
        super(R.layout.fragment_product_list);
        this.adapter = "com.think.android.p2p.ui.invest.ProductListAdapter";
    }

    @Override
    protected void initViews() {

        if (getView() == null) return;
        super.initViews();

        addTopTitle(R.string.invest);

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

        requestData(true);
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
                Log.d("", "handler");
                if (pullToRefreshAdapter != null) {
                    pullToRefreshAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    private LinearLayout midLayout;

    /**
     * 获取顶部功能条
     *
     * @return actionBarView
     */
    public View getToolbarView() {
        return getView().findViewById(R.id.toolbar);
    }

    /**
     * 获取功能条中间布局
     *
     * @return midLayout
     */
    public LinearLayout getMidLayout() {
        if (getToolbarView() == null) return null;
        if (midLayout == null) {
            midLayout = (LinearLayout) getToolbarView().findViewById(R.id.middle_layout);
        }
        return midLayout;
    }

    public void addTopTitle(int titleId) {
        /**
         * 中间的linearlanyout
         */
        LinearLayout midLayout = getMidLayout();
        if (midLayout == null) return;
        midLayout.removeAllViews();
        // 中间的textview
        TextView textView = new TextView(getActivity());
        textView.setSingleLine();
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.actionbar_title));
        textView.setText(titleId);
        textView.setTextColor(getResources().getColor(R.color.actionbar_title));
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        midLayout.addView(textView);
    }

    @Override
    protected void requestData(boolean needLoadingDialog) {
        isLoading = true;
        ProductListHandler productListHandler = new ProductListHandler(getActivity(), needLoadingDialog, curPage, maxLine);
        productListHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                isLoading = false;
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
                        adapterNotify(response.getJSONArray("investsList"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext().getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                    stopRefresh();
                }
            }
        });
        productListHandler.execute();

    }

}
