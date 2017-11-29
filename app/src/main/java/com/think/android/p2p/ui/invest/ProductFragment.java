package com.think.android.p2p.ui.invest;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amarsoft.support.android.ui.CommonFragment;
import com.think.android.p2p.R;
import com.think.android.p2p.ui.AutoBackBtnActivity;

import org.json.JSONObject;

/**
 * 项目详情
 * Created by Think on 2017/10/17.
 */

public class ProductFragment extends CommonFragment {

    int position = 1;

    ProductDetailFragment productDetailFragment;
    ProductDescriptionFragment productDescriptionFragment;
    ProductRecordFragment productRecordFragment;

    JSONObject data;

    public ProductFragment() {
        super(R.layout.fragment_product);
    }

    protected void initViews() {
        super.initViews();

        View view = getView();
        if (view == null) return;

        RadioGroup tabs = (RadioGroup) view.findViewById(R.id.product_group);
        tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.project_detail_radio:
                        if (position == 1) return;
                        position = 1;
                        updateUI();
                        break;
                    case R.id.project_description_radio:
                        if (position == 2) return;
                        position = 2;
                        updateUI();
                        break;
                    case R.id.transaction_record_radio:
                        if (position == 3) return;
                        position = 3;
                        updateUI();
                        break;
                }
            }
        });

        position = 1;

        updateUI();
    }

    private void updateUI() {
        updateFragment();
        updateData();
    }

    /**
     * 根据position更新主界面
     */
    private void updateFragment() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        // 隐藏当前Fragment
        if (productDetailFragment != null && productDetailFragment.isAdded()) {
            transaction.hide(productDetailFragment);
        }
        if (productDescriptionFragment != null && productDescriptionFragment.isAdded()) {
            transaction.hide(productDescriptionFragment);
        }
        if (productRecordFragment != null && productRecordFragment.isAdded()) {
            transaction.hide(productRecordFragment);
        }
        // 根据position显示当前Fragment
        switch (position) {
            case 1:
                if (productDetailFragment == null) {
                    productDetailFragment = new ProductDetailFragment();
                }
                showFragment(transaction, productDetailFragment);
                break;
            case 2:
                if (productDescriptionFragment == null) {
                    productDescriptionFragment = new ProductDescriptionFragment();
                }
                showFragment(transaction, productDescriptionFragment);
                break;
            case 3:
                if (productRecordFragment == null) {
                    productRecordFragment = new ProductRecordFragment();
                }
                showFragment(transaction, productRecordFragment);
                break;
        }
        transaction.commit();
    }

    private void updateData() {
        if (data == null) return;
        if (productDetailFragment != null)
            productDetailFragment.setProjectInfo(data);
        if (productDescriptionFragment != null)
            productDescriptionFragment.setProjectInfo(data);
        if (productRecordFragment != null)
            productRecordFragment.setProjectInfo(data);
    }

    public void setProjectInfo(JSONObject data) {
        this.data = data;
        updateData();
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
            transaction.add(R.id.fragment_product_container, fragment, fragment.getClass().getSimpleName());
        }
    }
}
