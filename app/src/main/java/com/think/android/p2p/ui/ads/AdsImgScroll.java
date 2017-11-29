package com.think.android.p2p.ui.ads;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 广告滚动控件
 * Created by blu on 2015/10/10.
 */
public class AdsImgScroll extends ViewPager {
    Context context; // 上下文
    List<View> listViews; // 图片组
    int mScrollTime = 0;
    Timer timer;
    int oldIndex = 0;
    int curIndex = 0;

    public AdsImgScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 开始广告滚动
     *
     * @param context          显示广告的主界面
     * @param imgList          图片列表, 不能为null ,最少一张
     * @param scrollTime       滚动间隔 ,0为不滚动
     * @param ovalLayout       圆点容器,可为空,LinearLayout类型
     * @param ovalLayoutId     ovalLayout为空时 写0, 圆点layout XMl
     * @param ovalLayoutItemId ovalLayout为空时 写0,圆点layout XMl 圆点XMl下View ID
     * @param focusedId        ovalLayout为空时 写0, 圆点layout XMl 选中时的动画
     * @param normalId         ovalLayout为空时 写0, 圆点layout XMl 正常时背景
     */
    public void start(Context context, List<View> imgList,
                      int scrollTime, LinearLayout ovalLayout, int ovalLayoutId,
                      int ovalLayoutItemId, int focusedId, int normalId) {
        this.context = context;
        listViews = imgList;
        mScrollTime = scrollTime;
        // 设置圆点
        setOvalLayout(ovalLayout, ovalLayoutId, ovalLayoutItemId, focusedId,
                normalId);
        this.setAdapter(new MyPagerAdapter());// 设置适配器
        if (scrollTime != 0 && imgList.size() > 1) {
            // 设置滑动动画时间 ,如果用默认动画时间可不用 ,反射技术实现
            new FixedSpeedScroller(context).setDuration(this, 700);

            startTimer();
            // 触摸时停止滚动
            this.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        startTimer();
                    } else {
                        stopTimer();
                    }
                    return false;
                }
            });
        }
        if (listViews.size() > 1) {
            this.setCurrentItem((Integer.MAX_VALUE / 2)
                    - (Integer.MAX_VALUE / 2) % listViews.size());// 设置选中为中间/图片为和第0张一样
        }
    }

    // 设置圆点
    private void setOvalLayout(final LinearLayout ovalLayout, int ovalLayoutId,
                               final int ovalLayoutItemId, final int focusedId, final int normalId) {
        if (ovalLayout != null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            for (int i = 0; i < listViews.size(); i++) {
                ovalLayout.addView(inflater.inflate(ovalLayoutId, null));

            }
            // 选中第一个
            ovalLayout.getChildAt(0).findViewById(ovalLayoutItemId)
                    .setBackgroundResource(focusedId);
            this.setOnPageChangeListener(new OnPageChangeListener() {
                public void onPageSelected(int i) {
                    curIndex = i % listViews.size();
                    // 取消圆点选中
                    ovalLayout.getChildAt(oldIndex)
                            .findViewById(ovalLayoutItemId)
                            .setBackgroundResource(normalId);
                    // 圆点选中
                    ovalLayout.getChildAt(curIndex)
                            .findViewById(ovalLayoutItemId)
                            .setBackgroundResource(focusedId);
                    oldIndex = curIndex;
                }

                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                public void onPageScrollStateChanged(int arg0) {
                }
            });
        }
    }

    /**
     * 取得当明选中下标
     *
     * @return
     */
    public int getCurIndex() {
        return curIndex;
    }

    /**
     * 停止滚动
     */
    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 开始滚动
     */
    public void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        setCurrentItem(getCurrentItem() + 1);
                    }
                });
            }
        }, mScrollTime, mScrollTime);
    }

    // 适配器 //循环设置
    private class MyPagerAdapter extends PagerAdapter {

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        public int getCount() {
            if (listViews.size() == 1) {// 一张图片时不用流动
                return listViews.size();
            }
            return Integer.MAX_VALUE;
        }

//        @Override
//        public Object instantiateItem(ViewGroup v, int i) {
//            if (v.getChildCount() == listViews.size()) {
//                v.removeView(listViews.get(i % listViews.size()));
//            }
//            v.addView(listViews.get(i % listViews.size()), 0);
//            return listViews.get(i % listViews.size());
//        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //对ViewPager页号求模取出View列表中要显示的项
            position %= listViews.size();
//            switch (position) {
//                case 0:
//                    listViews.get(0).setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View arg0) {//新手指南
//                            Intent intent = new Intent(context, NoviceGuideActivity.class);
//                            intent.putExtra("position", 0);
//                            context.startActivity(intent);
//                        }
//                    });
//                    break;
//                case 1:
//                    listViews.get(1).setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View arg0) {//安心理财
//                            Intent intent = new Intent(context, NoviceGuideActivity.class);
//                            intent.putExtra("position", 1);
//                            context.startActivity(intent);
//                        }
//                    });
//                    break;
//                case 2:
//                    listViews.get(2).setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View arg0) {//操作指南
//                            Intent intent = new Intent(context, NoviceGuideActivity.class);
//                            intent.putExtra("position", 2);
//                            context.startActivity(intent);
//                        }
//                    });
//                    break;
//            }
            if (position < 0) {
                position = listViews.size() + position;
            }
            View view = listViews.get(position);
            //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException
            ViewParent vp = view.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(view);
            }
            container.addView(view);
            //add listeners here if necessary
            return view;
        }


        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        public Parcelable saveState() {
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView(listViews.get(position));
        }
    }

}
