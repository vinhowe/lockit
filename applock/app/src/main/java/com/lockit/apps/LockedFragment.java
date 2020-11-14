package com.lockit.apps;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lockit.BaseFragment;
import com.lockit.databinding.LockedFragmentBinding;

public class LockedFragment extends BaseFragment {
    LockedFragmentBinding binding;
    private final boolean showLocked;

    public LockedFragment(boolean showLocked) {
        this.showLocked = showLocked;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LockedFragmentBinding.inflate(inflater, container, false);
        binding.title.setText(showLocked ? "Locked" : "Unlocked");
        return binding.getRoot();
    }
}
