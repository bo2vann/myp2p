package com.think.android.p2p.ui;

import android.view.View;
import android.widget.ListView;

import com.amarsoft.support.android.ui.CommonFragment;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.utils.PullRefreshUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Think on 2017/10/14.
 */
public abstract class PullToRefreshFragment extends CommonFragment {

    protected PullToRefreshListView pullToRefreshListView;
    PullRefreshUtil pullRefreshUtil;

    protected ListView dataListView;
    protected PullToRefreshAdapter pullToRefreshAdapter;
    protected ArrayList<JSONObject> dataList;

    protected int curPage;
    protected int maxLine;
    protected boolean isAllData;
    protected boolean isLoading;

    protected String adapter;

    public PullToRefreshFragment() {
        super(R.layout.fragment_pulltorefresh);
    }

    public PullToRefreshFragment(int layout){
        super(layout);
    }

    @Override
    protected void initViews() {
        super.initViews();
        if (getView() == null) return;

        curPage = 1;
        maxLine = 10;
        isAllData = false;
        isLoading = false;

        dataList = new ArrayList<>();

        pullToRefreshListView = (PullToRefreshListView)
                getView().findViewById(R.id.pull_to_refresh_list);
        pullToRefreshListView.setVisibility(View.GONE);
        dataListView = pullToRefreshListView.getRefreshableView();
        dataListView.setDivider(null);
        initListView();
    }

    protected void refresh() {
        curPage = 1;
        isAllData = false;
        dataList.clear();
        requestData(false);
    }

    protected void initListView() {
        pullRefreshUtil = new PullRefreshUtil(getActivity(), pullToRefreshListView) {
            @Override
            public void refreshList() {
                refresh();
            }

            @Override
            public void loadMore() {
                if (isLoading) return;
                if (dataList == null || isAllData)
                    return;
                curPage ++;
                requestData(false);
            }
        };

        try {
            pullToRefreshAdapter = (PullToRefreshAdapter) Class.forName(adapter).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        pullToRefreshAdapter.init(getActivity(), dataList);
        dataListView.setAdapter(pullToRefreshAdapter);

    }

    protected abstract void requestData(boolean needLoadingDialog);

    protected void adapterNotify(JSONArray jsonArray) {
        pullToRefreshListView.setVisibility(View.VISIBLE);
        if (jsonArray == null || jsonArray.length() > maxLine) {
            isAllData = true;
            stopRefresh();
            return;
        }
        if (dataList == null) {
            dataList = new ArrayList<>();
        }

        if (curPage == 1) {
            dataList.clear();
        }
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                dataList.add(jsonArray.getJSONObject(i));
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        stopRefresh();
        pullToRefreshAdapter.notifyDataSetChanged();
    }

    /**
     * 停止刷新动画
     */
    protected void stopRefresh() {
        if (pullToRefreshListView.isRefreshing()) {
            pullToRefreshListView.onRefreshComplete();
        }
    }
}
