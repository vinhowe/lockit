package com.lockit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.lockit.databinding.AppItemBinding;
import com.lockit.databinding.PasswordFragmentBinding;

public class LockView extends RelativeLayout {
    private String code = "";
    private String title = "";

    PasswordFragmentBinding binding;

    public LockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View viewRoot = inflate(getContext(), R.layout.password_fragment, this);
        binding = PasswordFragmentBinding.bind(viewRoot);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIcon(Drawable iconDrawable) {
        binding.icon.setImageDrawable(iconDrawable);
    }
}
