package com.think.android.p2p.ui.account.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amarsoft.android.ui.VersionManager;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.AutoBackBtnActivity;

/**
 * 关于我们
 * Created by Think on 2017/10/10.
 */

public class AboutActivity extends AutoBackBtnActivity implements View.OnClickListener {

    LinearLayout appLayout;
    LinearLayout safeLayout;
    LinearLayout problemLayout;

    TextView versionText;

    LinearLayout call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        addTopTitle(R.string.about_us);

        appLayout = (LinearLayout) findViewById(R.id.app_layout);
        safeLayout = (LinearLayout) findViewById(R.id.safe_layout);
        problemLayout = (LinearLayout) findViewById(R.id.problem_layout);

        versionText = (TextView) findViewById(R.id.version_text);
        versionText.setText(VersionManager.getVersionManager(getPackageManager(), getPackageName()).getVersionName());
        appLayout.setOnClickListener(this);
        safeLayout.setOnClickListener(this);
        problemLayout.setOnClickListener(this);

        call = (LinearLayout) findViewById(R.id.call);
        call.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.app_layout:
                skipActivity(PlatformIntroduceActivity.class);
                break;
            case R.id.safe_layout:
                skipActivity(SecurityGuaranteeActivity.class);
                break;
            case R.id.problem_layout:
                skipActivity(CommonProblemActivity.class);
                break;
            case R.id.call:
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:1802124226"));
//                startActivity(intent);
                break;
        }
    }

    private void skipActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
