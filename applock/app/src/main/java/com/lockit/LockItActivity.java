package com.lockit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lockit.databinding.FragmentContainerBinding;

public class LockItActivity extends BaseActivity {
    private FragmentContainerBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentContainerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    void init() {
        new InstalledApplication(this).lockApp(SystemApplication.installUninstallApp(this).getPackageName());
        navigateHome();
        finish();
    }

    private void navigateHome() {
        finish();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
