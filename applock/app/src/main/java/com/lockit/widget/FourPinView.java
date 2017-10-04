package com.lockit.widget;

import android.content.Context;
import android.support.annotation.Keep;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lockit.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.four_pin_view)
public class FourPinView extends LinearLayout {

    @ViewById(R.id.pin1)
    View pin1;

    @ViewById(R.id.pin2)
    View pin2;

    @ViewById(R.id.pin3)
    View pin3;

    @ViewById(R.id.pin4)
    View pin4;

    @ViewById(R.id.prompt)
    TextView prompt;

    @ViewById(R.id.pin_view)
    View pinView;

    View[] pin = new View[4];
    private Context context;

    public FourPinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @AfterViews
    void init() {
        pin[0] = pin1;
        pin[1] = pin2;
        pin[2] = pin3;
        pin[3] = pin4;
    }

    public void addPin() {
        for (int i = 0; i < 4; i++)
            if (!pin[i].isSelected()) {
                pin[i].setSelected(true);
                pin[i].startAnimation(AnimationUtils.loadAnimation(context, R.anim.pin_enter));
                break;
            }
    }

    public void removePin() {
        for (int i = 3; i > -1; i--)
            if (pin[i].isSelected()) {
                pin[i].setSelected(false);
                pin[i].startAnimation(AnimationUtils.loadAnimation(context, R.anim.pin_remove));
                break;
            }
    }

    public void reset() {
        for (View view : pin) {
            view.setSelected(false);
            view.setEnabled(true);
        }
    }

    public void error() {
        for (View view : pin)
            view.setEnabled(false);
        pinView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.pin_error));
    }
}
