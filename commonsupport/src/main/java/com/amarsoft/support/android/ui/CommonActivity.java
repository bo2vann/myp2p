package com.amarsoft.support.android.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.amarsoft.support.android.CommonApplication;

/**
 * Created by Think on 2017/10/9.
 */

public class CommonActivity extends AppCompatActivity {
    protected CommonApplication app;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.app = ((CommonApplication) getApplication());
        this.app.addActivity(this);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            keyBackDown();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 按下返回键操作，默认是关闭当前页面，若返回键按下时不需要关闭当前页，则可以重写此方法成空方法体
     */
    protected void keyBackDown() {
        setResult(RESULT_CANCELED);
        finishCurActivity();
    }

    /**
     * 关闭当前的Activity
     */
    public void finishCurActivity() {
        app.finishActivity(this);
    }
}
