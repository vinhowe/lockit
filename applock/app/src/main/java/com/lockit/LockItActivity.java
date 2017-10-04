package com.lockit;

import com.lockit.setpassword.SetPasswordHomeActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.fragment_container)
public class LockItActivity extends BaseActivity {
    @AfterViews
    void init() {
        String passcode = AppPreference.prefs().get(PreferenceType.PASSCODE.toString(), String.class, "");
        if (!passcode.equalsIgnoreCase("")) {
            PasswordFragment passwordFragment = PasswordFragment_.builder().code(passcode).build();
            passwordFragment.correctCodeEntered().subscribe(i -> navigateHome());
            Fragments.replaceContentFragment(this, R.id.container, passwordFragment, PasswordFragment.class.getSimpleName());
        } else {
            // By default lock install/uninstall package.
            new InstalledApplication(this).lockApp(SystemApplication.installUninstallApp(this).getPackageName());

            SetPasswordHomeActivity_.intent(this).start();
            finish();
        }
    }

    private void navigateHome() {
        finish();
        HomeActivity_.intent(this).start();
    }
}
