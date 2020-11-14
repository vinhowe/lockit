package com.lockit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.lockit.databinding.AppItemBinding;
import com.lockit.databinding.LockedFragmentBinding;
import com.lockit.databinding.PasswordFragmentBinding;

public class LockView extends ConstraintLayout {
    LockedFragmentBinding binding;

    public LockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View viewRoot = inflate(getContext(), R.layout.locked_fragment, this);
        binding = LockedFragmentBinding.bind(viewRoot);
    }

    @SuppressLint("SetTextI18n")
    public void setAppName(@NonNull String appName) {
        binding.title.setText(appName + " is locked");
    }
}
