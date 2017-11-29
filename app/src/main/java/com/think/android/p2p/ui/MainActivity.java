package com.think.android.p2p.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amarsoft.support.android.ui.CommonFragment;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.account.MineFragment;
import com.think.android.p2p.ui.home.HomePageFragment;
import com.think.android.p2p.ui.invest.ProductListFragment;
import com.think.android.p2p.ui.property.PropertyFragment;
import com.think.android.p2p.ui.views.NormalDialog;
import com.think.android.p2p.utils.VersionUtils;

import java.io.File;

/**
 * 主页
 * Created by Think on 2017/10/10.
 */
public class MainActivity extends CommonActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    /**
     * 当前页面时显示的tab（默认显示第1个）
     */
    private int position = 1;

    private TextView mainTab1;
    private TextView mainTab2;
    private TextView mainTab3;
    private TextView mainTab4;

    private HomePageFragment homePageFragment;
    private ProductListFragment productListFragment;
    private PropertyFragment propertyFragment;
    private MineFragment mineFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            homePageFragment = (HomePageFragment) getSupportFragmentManager()
                    .findFragmentByTag(HomePageFragment.class.getSimpleName());
            productListFragment = (ProductListFragment) getSupportFragmentManager()
                    .findFragmentByTag(ProductListFragment.class.getSimpleName());
            propertyFragment = (PropertyFragment) getSupportFragmentManager()
                    .findFragmentByTag(PropertyFragment.class.getSimpleName());
            mineFragment = (MineFragment) getSupportFragmentManager()
                    .findFragmentByTag(MineFragment.class.getSimpleName());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (homePageFragment != null)
                transaction.remove(homePageFragment);
            if (productListFragment != null)
                transaction.remove(productListFragment);
            if (propertyFragment != null)
                transaction.remove(propertyFragment);
            if (mineFragment != null)
                transaction.remove(mineFragment);
            transaction.commit();
            homePageFragment = null;
            productListFragment = null;
            propertyFragment = null;
            mineFragment = null;
        }

        if (getIntent().hasExtra("position"))
            position = getIntent()
                    .getIntExtra("position", position);
        else
            position = 1;

        initView();
        updateUI();
        versionCheck();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (propertyFragment != null && !propertyFragment.isHidden()) {
            propertyFragment.refresh();
        }
        if (mineFragment != null && !mineFragment.isHidden()) {
            mineFragment.refresh();
        }
    }

    private void versionCheck() {
        SharedPreferences sp = getSharedPreferences("Version", 0);
        int status = sp.getInt("status", 0);
        String url = sp.getString("url", "");
        if ("".equals(url)) return;
        if (status == VersionUtils.NEED_UPDATE) {
            showVersionDialog(2, Uri.parse(url));
        } else if (status == VersionUtils.SHOULD_UPDATE){
            showVersionDialog(3, Uri.parse(url));
        }
        sp.edit().clear().apply();
    }

    NormalDialog normalDialog;

    private void showVersionDialog(final int type, final Uri uri) {
        int titleId = 0;
        int bodyId = 0;
        boolean cancelFlag = true;
        switch (type) {
            case 1:
                titleId = R.string.version_update;
                bodyId = R.string.no_version_update;
                cancelFlag = true;
                break;
            case 2:
                titleId = R.string.version_update;
                bodyId = R.string.whether_version_update;
                cancelFlag = true;
                break;
            case 3:
                titleId = R.string.version_update;
                bodyId = R.string.whether_version_update;
                cancelFlag = false;
                break;
        }

        normalDialog = new NormalDialog(this, titleId, bodyId,
                new NormalDialog.OnClickListener() {
                    @Override
                    public void onCancel() {
                        switch (type) {
                            case 3:
                                finishCurActivity();
                                break;
                        }
                    }

                    @Override
                    public void onConfirm() {
                        switch (type) {
                            case 1:
                                normalDialog.dismiss();
                                break;
                            case 2:
                            case 3:
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                                finishCurActivity();
                                normalDialog.dismiss();
                                break;
                        }
                    }
                }, cancelFlag);
        normalDialog.setCanceledOnTouchOutside(false);
        normalDialog.show();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mainTab1 = (TextView) findViewById(R.id.main_tab1);
        mainTab2 = (TextView) findViewById(R.id.main_tab2);
        mainTab3 = (TextView) findViewById(R.id.main_tab3);
        mainTab4 = (TextView) findViewById(R.id.main_tab4);
        mainTab1.setOnClickListener(this);
        mainTab2.setOnClickListener(this);
        mainTab3.setOnClickListener(this);
        mainTab4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.main_tab1:
                if (position == 1) return;
                position = 1;
                updateUI();
                break;
            case R.id.main_tab2:
                if (position == 2) return;
                position = 2;
                updateUI();
                break;
            case R.id.main_tab3:
                if (position == 3) return;
                position = 3;
                updateUI();
                break;
            case R.id.main_tab4:
                if (position == 4) return;
                position = 4;
                updateUI();
                break;
        }
    }

    private void updateUI() {
        updateTab();
//        updateActionBar();
        updateFragment();
    }

    /**
     * 更新tab按键
     */
    private void updateTab() {

        // 重置图标
        mainTab1.setCompoundDrawablesWithIntrinsicBounds(
                null, getResources().getDrawable(R.mipmap.tabbar_icon_home),
                null, null);
        mainTab2.setCompoundDrawablesWithIntrinsicBounds(
                null, getResources().getDrawable(R.mipmap.tabbar_icon_invest),
                null, null);
        mainTab3.setCompoundDrawablesWithIntrinsicBounds(
                null, getResources().getDrawable(R.mipmap.tabbar_icon_money),
                null, null);
        mainTab4.setCompoundDrawablesWithIntrinsicBounds(
                null, getResources().getDrawable(R.mipmap.tabbar_icon_my),
                null, null);

        // 重置字颜色
        mainTab1.setTextColor(getResources().getColor(R.color.main_tab_unselect));
        mainTab2.setTextColor(getResources().getColor(R.color.main_tab_unselect));
        mainTab3.setTextColor(getResources().getColor(R.color.main_tab_unselect));
        mainTab4.setTextColor(getResources().getColor(R.color.main_tab_unselect));

        switch (position) {
            case 1:
                mainTab1.setCompoundDrawablesWithIntrinsicBounds(
                        null, getResources().getDrawable(R.mipmap.tabbar_icon_home_active),
                        null, null);
                mainTab1.setTextColor(getResources().getColor(R.color.main_tab_select));
                break;
            case 2:
                mainTab2.setCompoundDrawablesWithIntrinsicBounds(
                        null, getResources().getDrawable(R.mipmap.tabbar_icon_invest_active),
                        null, null);
                mainTab2.setTextColor(getResources().getColor(R.color.main_tab_select));
                break;
            case 3:
                mainTab3.setCompoundDrawablesWithIntrinsicBounds(
                        null, getResources().getDrawable(R.mipmap.tabbar_icon_money_active),
                        null, null);
                mainTab3.setTextColor(getResources().getColor(R.color.main_tab_select));
                break;
            case 4:
                mainTab4.setCompoundDrawablesWithIntrinsicBounds(
                        null, getResources().getDrawable(R.mipmap.tabbar_icon_my_active),
                        null, null);
                mainTab4.setTextColor(getResources().getColor(R.color.main_tab_select));
                break;
        }
    }

    /**
     * 根据position更新主界面
     */
    private void updateFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 隐藏当前Fragment
        if (homePageFragment != null && homePageFragment.isAdded()) {
            transaction.hide(homePageFragment);
        }
        if (productListFragment != null && productListFragment.isAdded()) {
            transaction.hide(productListFragment);
        }
        if (propertyFragment != null && propertyFragment.isAdded()) {
            transaction.hide(propertyFragment);
        }
        if (mineFragment != null && mineFragment.isAdded()) {
            transaction.hide(mineFragment);
        }
        // 根据position显示当前Fragment
        switch (position) {
            case 1:
                if (homePageFragment == null) {
                    homePageFragment = new HomePageFragment();
                }
//                investFragment.setArguments(bundle);
                showFragment(transaction, homePageFragment);
                break;
            case 2:
                if (productListFragment == null) {
                    productListFragment = new ProductListFragment();
                }
                showFragment(transaction, productListFragment);
                break;
            case 3:
                if (propertyFragment == null) {
                    propertyFragment = new PropertyFragment();
                }
                showFragment(transaction, propertyFragment);
                break;
            case 4:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                }
                showFragment(transaction, mineFragment);
                break;
        }
        transaction.commit();
    }

    /**
     * 显示fragment
     *
     * @param transaction FragmentTransaction
     * @param fragment    CommonFragment
     */
    private void showFragment(FragmentTransaction transaction, CommonFragment fragment) {
        if (fragment == null) return;
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());
        }
    }

}
