package com.lockit;


import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lockit.apps.AppsFragment;
import com.lockit.databinding.ApplockBinding;

public class HomeActivity extends BaseActivity {

    private MaterialDialog.Builder usageAccessDialog;

    private ApplockBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ApplockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initHeader();
    }

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
                            .onPositive((dialog, which) -> startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)));
                usageAccessDialog.show();
            }
        }
    }

    void initHeader() {
        setSupportActionBar(binding.appBarLayout.toolbar);
        binding.appBarLayout.title.setText(getString(R.string.app_name));
    }

//    void lockClicked() {
//        if (binding.appBarLayout.lock.isChecked()) {
//            AppLockService.start(this);
//        } else {
//            AppLockService.stop(this);
//        }
//        setAppLocked(binding.appBarLayout.lock.isChecked());
//    }

    private void setAppLocked(boolean isLocked) {
        AppPreference.prefs().put(PreferenceType.IS_APP_LOCKED.toString(), isLocked);
    }

    void init() {
//        if (appLocked()) {
//            binding.appBarLayout.lock.setChecked(true);
//            AppLockService.start(this);
//        }
//        binding.appBarLayout.lock.setOnClickListener(__ -> lockClicked());
        appsPresenter();
    }

    private Boolean appLocked() {
        return AppPreference.prefs().get(PreferenceType.IS_APP_LOCKED.toString(), Boolean.TYPE, true);
    }

    private void appsPresenter() {
        AppsFragment appsFragment = new AppsFragment();
        Fragments.replaceContentFragment(this, R.id.container, appsFragment,
                AppsFragment.class.getSimpleName());
    }

    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean hasUsageAccess() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            assert appOpsManager != null;
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
