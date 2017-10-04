package com.lockit.setpassword;

import android.support.annotation.Keep;

import com.lockit.BaseActivity;
import com.lockit.Fragments;
import com.lockit.PasswordFragment;
import com.lockit.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.fragment_container)
public class SetPasswordHomeActivity extends BaseActivity {
    @AfterViews
    void init() {
        SetPasswordHomeFragment passwordHomeFragment = SetPasswordHomeFragment_.builder().build();
        Fragments.replaceContentFragment(this, R.id.container, passwordHomeFragment, PasswordFragment.class.getSimpleName());
    }
}
