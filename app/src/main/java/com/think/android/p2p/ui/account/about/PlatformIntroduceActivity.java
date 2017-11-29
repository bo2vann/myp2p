package com.think.android.p2p.ui.account.about;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.base.BaseApplication;
import com.think.android.p2p.ui.AutoBackBtnActivity;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 平台介绍
 * Created by Think on 2017/10/15.
 */

public class PlatformIntroduceActivity extends AutoBackBtnActivity {

    private static final String CLIENT_TYPE = "APNA";
    String path = "loan/protocol/platfrom.html";

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common_webview);

        addTopTitle(R.string.platform_introduce);
        webView = (WebView) findViewById(R.id.web);
        webView.loadUrl(((BaseApplication) getApplication()).WEBSERVICE_URL + "/" + path);
//        queryApp();
    }

    private void queryApp() {
        QueryAboutHandler queryAboutHandler = new QueryAboutHandler(this, CLIENT_TYPE);
        queryAboutHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("<html>");
                    sb.append("<head></head");
                    sb.append("<body>");
                    try {
                        JSONArray list = response.getJSONArray("list");
                        for (int i = 0; i < list.length(); i++) {
                            sb.append(JSONHelper.getStringValue(list.getJSONObject(i), "content"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sb.append("</body>");
                    sb.append("</html>");
                    webView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
                } else {
                    Toast.makeText(getApplicationContext(), JSONHelper.getStringValue(response, "resultMsg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        queryAboutHandler.execute();
    }
}
