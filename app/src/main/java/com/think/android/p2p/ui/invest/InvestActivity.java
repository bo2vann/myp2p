package com.think.android.p2p.ui.invest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.amarsoft.support.android.ui.CommonFragment;
import com.amarsoft.support.android.utils.JSONHelper;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.AutoBackBtnActivity;
import com.think.android.p2p.ui.CommonRemoteHandler;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 项目详情
 * Created by Think on 2017/10/10.
 */
public class InvestActivity extends AutoBackBtnActivity {

    String projectNo;

    InvestDetailFragment investDetailFragment;
    ProductFragment productFragment;

    public static long platformSysTime;
    public static long currentSysTime;

    JSONObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invest);

        initViews();

        projectNo = getIntent().getStringExtra("projectNo");

        investDetailFragment = new InvestDetailFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, investDetailFragment,
                InvestDetailFragment.class.getSimpleName());
        fragmentTransaction.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        requestData();
    }

    private void initViews() {
        RadioGroup investGroup = (RadioGroup) findViewById(R.id.invest_group);
        investGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.project_radio:
                        if (investDetailFragment == null) {
                            investDetailFragment = new InvestDetailFragment();
                        }
                        if (data != null) {
                            investDetailFragment.setProjectInfo(data);
                        }
                        showFragment(investDetailFragment);
                        investDetailFragment.setProjectInfo(data);
                        break;
                    case R.id.detail_radio:
                        if (productFragment == null) {
                            productFragment = new ProductFragment();
                        }
                        if (data != null) {
                            productFragment.setProjectInfo(data);
                        }
                        showFragment(productFragment);
                        productFragment.setProjectInfo(data);
                        break;
                }
            }
        });
    }

    private void showFragment(CommonFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (investDetailFragment != null && investDetailFragment.isAdded()) {
            transaction.hide(investDetailFragment);
        }

        if (productFragment != null && productFragment.isAdded()) {
            transaction.hide(productFragment);
        }

        if (fragment == null) return;
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());
        }
        transaction.commit();
    }

    private void requestData() {
        ProductInfoHandler productInfoHandler = new ProductInfoHandler(this, projectNo);
        productInfoHandler.setResponseListener(new CommonRemoteHandler.ResponseListener() {
            @Override
            public void responseCallback(Object object, JSONObject response) {
                if ("SUCCESS".equals(object)) {
                    data = response;

                    String sysDate = JSONHelper.getStringValue(response, "sysDate");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    try {
                        date = simpleDateFormat.parse(sysDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    platformSysTime = date.getTime();
                    currentSysTime = System.currentTimeMillis();

                    Log.d("", "fragment:" + investDetailFragment);
                    if (investDetailFragment != null) {
                        investDetailFragment.setProjectInfo(response);
                    }
                }
            }
        });
        productInfoHandler.execute();
    }

}
