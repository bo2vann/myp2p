package com.think.android.p2p.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amarsoft.support.android.ui.CommonActivity;
import com.think.android.p2p.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 欢迎页
 * Created by Think on 2017/11/20.
 */

public class WelcomeActivity extends CommonActivity implements View.OnClickListener {

    private int[] welcomeImg = {R.drawable.welcomebg1};
    private List<ImageView> imageViewList;
    private List<View> dotList;

    ViewPager welcomeViewPager;
    LinearLayout dotLayout;
    Button skipBtn;
    Button expBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        imageViewList = new ArrayList<>();
        dotList = new ArrayList<>();
        dotLayout = (LinearLayout) findViewById(R.id.dot_layout);
        int sizePx = dip2px(this, 7);
        int marginPx = dip2px(this, 20);
        for (int i = 0; i < welcomeImg.length; i++) {
            ImageView view = new ImageView(this);
            view.setBackgroundResource(welcomeImg[i]);
            imageViewList.add(view);

            View dotView = new View(this);
            dotView.setBackgroundDrawable(getResources().getDrawable(R.drawable.dot));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(sizePx, sizePx);
            if (i == 0) layoutParams.setMargins(0, 0, 0, 0);
            else layoutParams.setMargins(marginPx, 0, 0, 0);
            dotList.add(dotView);
            dotLayout.addView(dotView, layoutParams);
        }

        welcomeViewPager = (ViewPager) findViewById(R.id.welcome_viewpager);
        skipBtn = (Button) findViewById(R.id.skip_btn);
        expBtn = (Button) findViewById(R.id.exp_btn);
        skipBtn.setOnClickListener(this);
        expBtn.setOnClickListener(this);

        welcomeViewPager.setAdapter(new ViewPagerAdapter());
        welcomeViewPager.setOnPageChangeListener(onPageChangeListener);

        updateDot(0);

        if (welcomeImg.length >= 1) {//只有一张图3秒后自动跳
            skipBtn.setVisibility(View.GONE);
            expBtn.setVisibility(View.GONE);
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    SharedPreferences pref = getSharedPreferences("loading", 0);
                    Intent intent = new Intent();
                    pref.edit().putBoolean("isFirstIn", false).commit();
                    intent.setClass(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finishCurActivity();
                }
            };
            timer.schedule(task, 1000 * 3);
        }
    }

    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (imageViewList == null) return 0;
            return imageViewList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (imageViewList == null) return null;
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(imageViewList.get(position), layoutParams);
            return imageViewList.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }

    /**
     * 监听界面滑动
     */
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == imageViewList.size() - 1) updateBtn(true);
            else updateBtn(false);

            updateDot(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * 是否显示按钮
     */
    private void updateBtn(boolean isShow) {
        if (isShow) {
            skipBtn.setVisibility(View.GONE);
            expBtn.setVisibility(View.VISIBLE);
        } else {
            skipBtn.setVisibility(View.VISIBLE);
            expBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 更新点状态
     *
     * @param position
     */
    private void updateDot(int position) {
        for (int i = 0; i < dotList.size(); i++) {
            dotList.get(i).getBackground().setAlpha(35);
        }
        dotList.get(position).setBackgroundDrawable(
                getResources().getDrawable(R.drawable.dot_change));
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.skip_btn:
            case R.id.exp_btn:
                SharedPreferences pref = getSharedPreferences("loading", 0);
                pref.edit().putBoolean("isFirstIn", false).commit();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finishCurActivity();
                break;
        }
    }
}
