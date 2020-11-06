package com.lockit;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lockit.databinding.SplashBinding;

public class SplashActivity extends BaseActivity {
    private SplashBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SplashBinding.inflate(getLayoutInflater());
    }
}
