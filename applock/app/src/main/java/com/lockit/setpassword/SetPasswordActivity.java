package com.lockit.setpassword;

import android.support.annotation.Keep;

import com.lockit.AppPreference;
import com.lockit.BaseActivity;
import com.lockit.Fragments;
import com.lockit.HomeActivity_;
import com.lockit.PasswordFragment;
import com.lockit.PasswordFragment_;
import com.lockit.PreferenceType;
import com.lockit.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.fragment_container)
public class SetPasswordActivity extends BaseActivity {
    @AfterViews
    void init() {
        PasswordFragment passwordFragment = PasswordFragment_.builder().title("Enter password").build();
        passwordFragment.codeEntered().subscribe(this::codeEntered);
        setContentFragment(passwordFragment);
    }

    private void codeEntered(String code) {
        PasswordFragment passwordFragment = PasswordFragment_.builder().title("Re-enter password").code(code).build();
        passwordFragment.correctCodeEntered().subscribe(i -> {
            setPasscode(code);
            navigateHome();
        });
        setContentFragment(passwordFragment);
    }

    private void setPasscode(String code) {
        AppPreference.prefs().put(PreferenceType.PASSCODE.toString(), code);
    }

    private void setContentFragment(PasswordFragment passwordFragment) {
        Fragments.replaceContentFragment(this, R.id.container, passwordFragment, PasswordFragment.class.getSimpleName());
    }

    private void navigateHome() {
        finish();
        HomeActivity_.intent(this).start();
    }
}
