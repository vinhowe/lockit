package com.lockit;


import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Keep;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lockit.apps.AppsFragment;
import com.lockit.apps.AppsFragment_;
import com.lockit.apps.AppsPresenter;
import com.lockit.setpassword.ResetPasswordActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.applock)
public class HomeActivity extends BaseActivity {
    @ViewById(R.id.drawer)
    DrawerLayout drawer;

    @ViewById(R.id.navigation)
    NavigationView navigation;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.lock)
    SwitchCompat lockSwitch;

    ActionBarDrawerToggle mDrawerToggle;
    private MaterialDialog.Builder usageAccessDialog;

    @Override
    protected void onResume() {
        super.onResume();
        checkUsageAccess();
    }

    private void checkUsageAccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!hasUsageAccess()) {
                if (usageAccessDialog == null)
                    usageAccessDialog = new MaterialDialog.Builder(this)
                            .backgroundColor(Color.WHITE)
                            .titleColor(getResources().getColor(R.color.textColorBlack))
                            .contentColor(getResources().getColor(R.color.textColorBlack))
                            .positiveColor(getResources().getColor(R.color.primaryColor))
                            .title("Enable lock")
                            .content("Allow \"Usage Access\" to start protecting your applications.")
                            .positiveText("Enable")
                            .cancelable(false)
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    super.onPositive(dialog);
                                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                                }
                            });
                usageAccessDialog.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(navigation))
            closeDrawer();
        else if (!AppPreference.prefs()
                .get(PreferenceType.IS_APP_RATED.toString(), Boolean.class, Boolean.FALSE)) {
            rateApp();
        } else super.onBackPressed();
    }

    private void rateApp() {
        DroippUtil.rateDialog(this)
                .backgroundColor(Color.WHITE)
                .titleColor(getResources().getColor(R.color.textColorBlack))
                .contentColor(getResources().getColor(R.color.textColorBlack))
                .positiveColor(getResources().getColor(R.color.primaryColor))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        finish();
                        DroippUtil.rate(HomeActivity.this);

                        AppPreference.prefs().put(PreferenceType.IS_APP_RATED.toString(), Boolean.TRUE);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        finish();
                    }
                }).show();
    }

    @AfterViews
    void initHeader() {
//        setSupportActionBar(toolbar);
        title.setText(getString(R.string.app_name));
        toolbar.setNavigationOnClickListener(ignored -> openDrawer());
    }

    @Click(R.id.lock)
    void lockClicked() {
        if (lockSwitch.isChecked())
            AppLockService.start(this);
        else
            AppLockService.stop(this);
        setAppLocked(lockSwitch.isChecked());
    }

    private void setAppLocked(boolean isLocked) {
        AppPreference.prefs().put(PreferenceType.IS_APP_LOCKED.toString(), isLocked);
    }

    @AfterViews
    void setupNavigationView() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(mDrawerToggle);
        navigation.setNavigationItemSelectedListener(this::navigationItemSelected);
        navigation.getMenu().getItem(0).setChecked(true);
    }

    private boolean navigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        closeDrawer();

        switch (menuItem.getItemId()) {
            case R.id.apps:
                appsPresenter();
                break;
            case R.id.passcode:
                ResetPasswordActivity_.intent(this).start();
                break;
            case R.id.feedback:
                DroippUtil.feedback(this);
                break;
            case R.id.share:
                DroippUtil.share(this, "App Lock", "Hey, Checkout this cool app to protect your applications. https://play.google.com/store/apps/details?id=" + getPackageName());
                break;
            case R.id.rate:
                DroippUtil.rate(this);
                break;
        }

        return true;
    }

    private void openDrawer() {
        drawer.openDrawer(navigation);
    }

    private void closeDrawer() {
        drawer.closeDrawer(navigation);
    }

    @AfterViews
    void init() {
        if (appLocked()) {
            lockSwitch.setChecked(true);
            AppLockService.start(this);
        }
        appsPresenter();
    }

    private Boolean appLocked() {
        return AppPreference.prefs().get(PreferenceType.IS_APP_LOCKED.toString(), Boolean.TYPE, true);
    }

    private void appsPresenter() {
        AppsFragment appsFragment = AppsFragment_.builder().build();
        Fragments.replaceContentFragment(this, R.id.container, appsFragment,
                AppsFragment.class.getSimpleName());
        new AppsPresenter(new InstalledApplication(this), appsFragment);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean hasUsageAccess() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
