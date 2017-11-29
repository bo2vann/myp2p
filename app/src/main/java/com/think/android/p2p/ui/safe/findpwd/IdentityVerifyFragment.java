package com.think.android.p2p.ui.safe.findpwd;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amarsoft.support.android.ui.CommonFragment;
import com.think.android.p2p.R;

/**
 * 找回登录密码-身份验证
 * Created by Think on 2017/10/10.
 */

public class IdentityVerifyFragment extends CommonFragment implements View.OnClickListener{

    EditText certidEdit;

    public IdentityVerifyFragment() {
        super(R.layout.fragment_identity_verify);
    }

    @Override
    protected void initViews() {
        super.initViews();
        View view = getView();
        if (view == null) return;

        certidEdit = (EditText) view.findViewById(R.id.certid_edit);

        Button nextStepBtn = (Button) view.findViewById(R.id.next_step_btn);
        nextStepBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.next_step_btn:
                requestNext();
                break;
        }
    }

    private void requestNext() {
        toNextFragment();
    }

    private void toNextFragment() {
        ResetPwdFragment resetPwdFragment =
                new ResetPwdFragment();
        FragmentTransaction fragmentTransaction = getActivity().
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,
                resetPwdFragment,
                ResetPwdFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

}
