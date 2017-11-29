package com.amarsoft.support.android.animate;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.amarsoft.support.android.R;

/**
 * 动画管理类
 * Created by blu on 2015/10/14.
 */
public class AnimationShow {

    /**
     * View抖动效果
     * @param view 需要抖动控件
     */
    public static void shakeView(View view) {
        Animation shake = AnimationUtils.loadAnimation(view.getContext(), R.anim.shake);
        view.startAnimation(shake);
    }
}
