package com.think.android.p2p.ui.account.message;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.AutoBackBtnActivity;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 个人消息详情
 * Created by Think on 2017/11/25.
 */

public class MessageDetailActivity extends AutoBackBtnActivity {

    TextView title;
    TextView time;
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        addTopTitle(R.string.message_detail);
        title = (TextView) findViewById(R.id.title);
        time = (TextView) findViewById(R.id.time);
        content = (TextView) findViewById(R.id.content);

        String data = getIntent().getStringExtra("data");
        String type = getIntent().getStringExtra("type");
        try {
            JSONObject dataObject = new JSONObject(data);
            title.setText(JSONHelper.getStringValue(dataObject, "title"));
            time.setText(JSONHelper.getStringValue(dataObject, "createTime"));
            content.setText(Html.fromHtml(JSONHelper.getStringValue(dataObject, "msgContent")));
            if ("USME".equals(type)) {
                setRead(JSONHelper.getStringValue(dataObject, "id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            finishCurActivity();
        }
    }

    private void setRead(String id) {
        MessageUpdateHandler messageUpdateHandler = new MessageUpdateHandler(this, id);
        messageUpdateHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {

            }
        });
        messageUpdateHandler.execute();
    }


}
