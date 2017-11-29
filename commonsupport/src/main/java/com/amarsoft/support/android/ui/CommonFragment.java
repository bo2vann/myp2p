package com.amarsoft.support.android.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Think on 2017/10/9.
 */
@SuppressLint("ValidFragment")
public class CommonFragment extends Fragment {
    private View view;
    private int resource;
    public static int screenwidth;
    public static int screenheight;

    public CommonFragment(int layoutid) {
        this.resource = layoutid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (savedInstanceState == null && view == null && resource != 0) {
            view = inflater.inflate(resource, container, false);
            initViews();
            setListener(0, 0);
        }
        return view;
    }

    /**
     * 初始化页面
     */
    protected void initViews() {
        screenwidth = getscreenWidth(getActivity());
        screenheight = getscreenHeight(getActivity());
    }

    /**
     * 获取Fragment中的View
     */
    public View getView() {
        return view;
    }

    /**
     * 监听回调，当param1=0&&param2=0时，则Fragment中的view已经初始化
     *
     * @param param1
     * @param param2
     */
    protected void setListener(Object param1, Object param2) {
        if (getActivity() instanceof BaseFragmentListener) {
            ((BaseFragmentListener) getActivity()).baseFragmentCallBack(
                    param1, param2);
        }
    }

    /**
     * Fragment的监听器
     *
     */
    public interface BaseFragmentListener {
        /**
         * 回调方法（特别说明：若param1=0.param2=0，则说明Fragment中View初始化完成）
         *
         * @param param1 回调参数1，
         * @param param2 回调参数2
         */
        void baseFragmentCallBack(Object param1, Object param2);
    }

    /**
     * 接受来自所在Activity的调用
     *
     * @param object1
     * @param object2
     */
    public void responseActivity(Object object1, Object object2) {

    }

    public void finishCurActivity() {
        if (getActivity() instanceof CommonActivity) {
            ((CommonActivity) getActivity()).finishCurActivity();
        } else {
            getActivity().finish();
        }
    }

    public static int getscreenWidth(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = metric.heightPixels;
        float density = metric.density;
        int densityDpi = metric.densityDpi;
        return width;
    }

    public static int getscreenHeight(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = metric.heightPixels;
        float density = metric.density;
        int densityDpi = metric.densityDpi;
        return height;
    }
}
