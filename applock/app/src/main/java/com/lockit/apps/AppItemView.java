package com.lockit.apps;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.lockit.Application;
import com.lockit.BaseListItemView;
import com.lockit.R;
import com.lockit.databinding.AppItemBinding;

public class AppItemView extends RelativeLayout implements BaseListItemView<Application> {
    private final AppItemBinding binding;

    public AppItemView(Context context) {
        super(context);
        View viewRoot = inflate(getContext(), R.layout.app_item, this);
        binding = AppItemBinding.bind(viewRoot);
    }

    @Override
    public void bind(Application application) {
        binding.icon.setImageDrawable(application.getIcon());
        binding.name.setText(application.getName());

        setSelected(application.isLocked());
    }
}
