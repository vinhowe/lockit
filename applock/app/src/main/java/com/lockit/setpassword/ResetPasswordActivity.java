package com.lockit.setpassword;

import android.support.annotation.Keep;

import com.lockit.AppPreference;
import com.lockit.BaseActivity;
import com.lockit.Fragments;
import com.lockit.PasswordFragment;
import com.lockit.PasswordFragment_;
import com.lockit.PreferenceType;
import com.lockit.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.fragment_container)
public class ResetPasswordActivity extends BaseActivity {

    @AfterViews
    void init() {
        PasswordFragment passwordFragment = PasswordFragment_.builder().title("Enter password").code(passcode()).build();
        passwordFragment.correctCodeEntered().subscribe(ignored -> resetPasscode());
        setContentFragment(passwordFragment);
    }

    void resetPasscode() {
        PasswordFragment passwordFragment = PasswordFragment_.builder().title("Enter new password").build();
        passwordFragment.codeEntered().subscribe(this::codeEntered);
        setContentFragment(passwordFragment);
    }

    private void codeEntered(String code) {
        PasswordFragment passwordFragment = PasswordFragment_.builder().title("Re-enter password").code(code).build();
        passwordFragment.correctCodeEntered().subscribe(i -> {
            setPasscode(code);
            finish();
        });
        setContentFragment(passwordFragment);
    }

    private String passcode() {
        return AppPreference.prefs().get(PreferenceType.PASSCODE.toString(), String.class);
    }

    private void setPasscode(String code) {
        AppPreference.prefs().put(PreferenceType.PASSCODE.toString(), code);
    }

    private void setContentFragment(PasswordFragment passwordFragment) {
        Fragments.replaceContentFragment(this, R.id.container, passwordFragment, PasswordFragment.class.getSimpleName());
    }
}
