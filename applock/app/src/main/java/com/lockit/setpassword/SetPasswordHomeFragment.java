package com.lockit.setpassword;

import android.support.annotation.Keep;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.lockit.BaseFragment;
import com.lockit.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.set_password_home)
public class SetPasswordHomeFragment extends BaseFragment {

    @ViewById(R.id.logo)
    ImageView logo;

    @AfterViews
    void init() {
        logo.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.splash_logo));
    }

    @Click(R.id.passcode)
    void passcode() {
        SetPasswordActivity_.intent(getActivity()).start();
        getActivity().finish();
    }
}
