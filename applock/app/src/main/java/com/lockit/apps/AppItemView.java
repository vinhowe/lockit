package com.lockit.apps;

import android.content.Context;
import android.support.annotation.Keep;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lockit.Application;
import com.lockit.BaseListItemView;
import com.lockit.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import static com.lockit.BusProvider.bus;

@EViewGroup(R.layout.app_item)
public class AppItemView extends RelativeLayout implements BaseListItemView<Application> {
    @ViewById
    TextView name;

    @ViewById
    ImageView icon;
    private Application application;

    @ViewById(R.id.item)
    public View item;

    public AppItemView(Context context) {
        super(context);
    }

    @Click(R.id.item)
    void appSelected() {
        bus().post(application);
    }

    @Override
    public void bind(Application application) {
        this.application = application;

        icon.setImageDrawable(application.getIcon());
        name.setText(application.getName());

        setSelected(application.isLocked());
    }
}
