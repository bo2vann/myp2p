package com.think.android.p2p.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.think.android.p2p.R;

/**
 * 横向自定义进度条
 * Created by blu on 2015/7/21.
 */
public class LineProgressView extends View {

    /**
     * 最大进度值
     */
    private float maxCount;

    /**
     * 当前进度值
     */
    private float currentCount;

    /**
     * 当前绘制进度值，用于动态绘制
     */
    private float drawCount;

    /**
     * 是否显示动态效果
     */
    private boolean dynamic;

    /**
     * 背景色
     */
    private int bgColor;

    /**
     * 进度条颜色
     */
    private int progressColor;

    public LineProgressView(Context context) {
        super(context);
        initView();
    }

    public LineProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LineProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        dynamic = false;
        maxCount = 100;
        currentCount = 0;
        drawCount = currentCount;
        bgColor = getContext().getResources().getColor(R.color.progress_bg);
        progressColor = getContext().getResources().getColor(R.color.progress_color);
    }

    @Override
    protected void onDraw(Canvas canvas) {//定义进度条样式
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        int radius = getHeight() / 2;
        paint.setColor(bgColor);
        RectF rectBg = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(rectBg, radius, radius, paint);

        paint.setColor(progressColor);
        float section = drawCount / maxCount;
        float length = (getWidth() - 2) * section;
        if (length >= radius * 2) {
            RectF rectProgress = new RectF(2, 2, length, getHeight() - 2);
            canvas.drawRoundRect(rectProgress, radius - 1, radius - 1, paint);
        } else {
            RectF rectProgress = new RectF(2, 2, radius * 2, getHeight() - 2);
            canvas.drawRoundRect(rectProgress, radius - 1, radius - 1, paint);
        }
    }

    /**
     * 开始绘制
     */
    public void startDraw() {
        handler.removeCallbacks(drawRunnable);
        if (dynamic) {
            increase = maxCount / 120;
            drawCount = 0;
            handler.post(drawRunnable);
        } else {
            drawCount = currentCount;
            postInvalidate();
        }
    }

    /**
     * 绘制增长值
     */
    private float increase;

    /**
     * 刷新时间间隔
     */
    private int time = 20;

    Handler handler = new Handler();
    Runnable drawRunnable = new Runnable() {
        @Override
        public void run() {
            if (drawCount >= currentCount) {
                drawCount = currentCount;
                postInvalidate();
            } else {
                postInvalidate();
                drawCount += increase;
                handler.postDelayed(this, time);
            }
        }
    };

    public void setCurrentCount(float currentCount) {
        if (currentCount < 0) return;
        // 进度值不能超过最大值
        this.currentCount = (currentCount < maxCount) ? currentCount : maxCount;

        startDraw();
    }

    public float getCurrentCount() {
        return currentCount;
    }

    public void setMaxCount(float maxCount) {
        if (maxCount <= 0) return;
        this.maxCount = maxCount;
    }

    public float getMaxCount() {
        return maxCount;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public boolean getDynamic() {
        return dynamic;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public int getProgressColor() {
        return progressColor;
    }

}
