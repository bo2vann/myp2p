package com.think.android.p2p.ui.utils;

import android.content.Context;
import android.text.format.DateUtils;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshMyListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.handmark.pulltorefresh.library.mylistview.MyListView;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.views.NoDataView;

/**
 * Created by blu on 2017/10/14
 */
public abstract class PullRefreshUtil {

    private Context context;

    /**
     * 可刷新列表助手
     *
     * @param context
     * @param pullToRefreshListView
     */
    public PullRefreshUtil(Context context,
                           PullToRefreshListView pullToRefreshListView) {
        // TODO Auto-generated constructor stub
        this.context = context;
        initRefreshListView(pullToRefreshListView);
    }

    public PullRefreshUtil(Context context,
                           PullToRefreshScrollView pullToRefreshScrollView) {
        this.context = context;
        initRefreshScrllow(pullToRefreshScrollView);
    }

    public PullRefreshUtil(Context context,
                           PullToRefreshMyListView pullToRefreshMyListView) {
        this.context = context;
        initRefreshMyListView(pullToRefreshMyListView);
    }

    public PullRefreshUtil(Context context,
                           PullToRefreshWebView pullToRefreshWebView) {
        this.context = context;
        initRefreshWebView(pullToRefreshWebView);
    }

    private void initRefreshWebView(PullToRefreshWebView pullToRefreshWebView) {
        if (context == null || pullToRefreshWebView == null) {
            return;
        }
        pullToRefreshWebView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshWebView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {

                    @Override
                    public void onRefresh(PullToRefreshBase<WebView> refreshView) {
                        // TODO Auto-generated method stub
                        refreshList();
                    }
                });
    }

    /**
     * 初始化PullToRefreshScrollView
     *
     * @param pullToRefreshScrollView
     */
    private void initRefreshScrllow(
            PullToRefreshScrollView pullToRefreshScrollView) {
        if (context == null || pullToRefreshScrollView == null) {
            return;
        }
        pullToRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshScrollView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {

                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ScrollView> refreshView) {
                        refreshList();
                    }
                });
    }

    /**
     * 初始化PullToRefreshListView
     *
     * @param pullToRefreshListView
     */
    private void initRefreshListView(
            final PullToRefreshListView pullToRefreshListView) {
        if (context == null || pullToRefreshListView == null) {
            return;
        }
//        pullToRefreshListView.setEmptyView(NoDataView.getEmptyView(context));
        // pullToRefreshListView.setMode(Mode.BOTH);//设置刷新操作的模式，这里设置为下拉刷新、上拉加载更多
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);// 设置只可为下拉刷新
        pullToRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        // TODO Auto-generated method stub
                        String label = DateUtils.formatDateTime(context,
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);
                        refreshView.getLoadingLayoutProxy(true, false)
                                .setLastUpdatedLabel(label);
                        refreshView.getLoadingLayoutProxy(false, true)
                                .setReleaseLabel(context.getResources().
                                        getString(R.string.release_to_load_more));
                        if (PullToRefreshBase.Mode.PULL_FROM_START == pullToRefreshListView
                                .getCurrentMode()) {
                            refreshList();
                        } else if (PullToRefreshBase.Mode.PULL_FROM_END == pullToRefreshListView
                                .getCurrentMode()) {
                            loadMore();
                        }
                    }
                });
        if (pullToRefreshListView.getMode() == PullToRefreshBase.Mode.PULL_FROM_START) {// 只有在下拉刷新模式下才允许ListView滑动到最后一条记录自动加载更多，其他模式不允许使用
            pullToRefreshListView
                    .setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

                        @Override
                        public void onLastItemVisible() {
                            loadMore();
                        }
                    });
        }
    }

    /**
     * 初始化PullToRefreshListView
     *
     * @param pullToRefreshMyListView
     */
    private void initRefreshMyListView(
            final PullToRefreshMyListView pullToRefreshMyListView) {
        if (context == null || pullToRefreshMyListView == null) {
            return;
        }
//        pullToRefreshMyListView.setEmptyView(NoDataView.getEmptyView(context));
        // pullToRefreshListView.setMode(Mode.BOTH);//设置刷新操作的模式，这里设置为下拉刷新、上拉加载更多
        pullToRefreshMyListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);// 设置只可为下拉刷新
        pullToRefreshMyListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<MyListView>() {

                    @Override
                    public void onRefresh(
                            PullToRefreshBase<MyListView> refreshView) {
                        // TODO Auto-generated method stub
                        String label = DateUtils.formatDateTime(context,
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);
                        refreshView.getLoadingLayoutProxy(true, false)
                                .setLastUpdatedLabel(label);
                        refreshView.getLoadingLayoutProxy(false, true)
                                .setReleaseLabel(context.getResources().
                                        getString(R.string.release_to_load_more));
                        if (PullToRefreshBase.Mode.PULL_FROM_START == pullToRefreshMyListView
                                .getCurrentMode()) {
                            refreshList();
                        } else if (PullToRefreshBase.Mode.PULL_FROM_END == pullToRefreshMyListView
                                .getCurrentMode()) {
                            loadMore();
                        }
                    }
                });
        if (pullToRefreshMyListView.getMode() == PullToRefreshBase.Mode.PULL_FROM_START) {// 只有在下拉刷新模式下才允许ListView滑动到最后一条记录自动加载更多，其他模式不允许使用
            pullToRefreshMyListView
                    .setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

                        @Override
                        public void onLastItemVisible() {
                            loadMore();
                        }
                    });
        }
    }

    /**
     * 下拉刷新
     */
    public abstract void refreshList();

    /**
     * 加载更多
     */
    public abstract void loadMore();

    /**
     * 停止刷新动画
     *
     * @param pullToRefreshBase
     */
    public static void stopRefresh(PullToRefreshBase pullToRefreshBase) {
        if (pullToRefreshBase != null && pullToRefreshBase.isRefreshing())
            pullToRefreshBase.onRefreshComplete();
    }
}
