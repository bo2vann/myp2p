package com.think.android.p2p.ui;

import android.os.Bundle;
import android.webkit.WebView;

import com.think.android.p2p.R;
import com.think.android.p2p.base.BaseApplication;
import com.think.android.p2p.ui.AutoBackBtnActivity;

/**
 * Created by Think on 2017/11/23.
 */

public class ProtocolActivity extends AutoBackBtnActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_webview);
        addTopTitle(getIntent().getStringExtra("title"));
        webView = (WebView) findViewById(R.id.web);
        String url = ((BaseApplication)getApplication()).WEBSERVICE_URL + "/" + getIntent().getStringExtra("url");

        webView.loadUrl(url);
    }

}
