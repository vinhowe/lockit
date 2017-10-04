package com.lockit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lockit.widget.FourPinView;
import com.lockit.widget.PasswordView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import rx.Observable;
import rx.subjects.PublishSubject;

@EViewGroup(R.layout.password_fragment)
public class LockView extends RelativeLayout {
    private PublishSubject<String> codeEnter = PublishSubject.create();
    private PublishSubject<Void> correctCodeEnter = PublishSubject.create();
    private PublishSubject<Void> backKeyPress = PublishSubject.create();

    private String code = "";
    private String title = "";

    @ViewById(R.id.icon)
    ImageView icon;

    @ViewById(R.id.password_view)
    PasswordView passwordView;

    @ViewById(R.id.pin_view)
    FourPinView pinView;

    @ViewById(R.id.prompt)
    TextView prompt;

    public LockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setTitle(String title) {
        this.title = title;
        prompt.setVisibility(View.VISIBLE);
        prompt.setText(title);
    }

    public void setIcon(Drawable iconDrawable) {
        icon.setImageDrawable(iconDrawable);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_HOME) {
            backKeyPress.onNext(null);
        }
        return super.dispatchKeyEvent(event);
    }

    @AfterViews
    void registerEvents() {
        passwordView.numKey.subscribe(i -> numKeySelected());
        passwordView.backKey.subscribe(i -> backKeyPress.onNext(null));
        passwordView.deleteKey.subscribe(i -> deleteKeyPressed());
        passwordView.fourKeyEntered.subscribe((code) -> {
            new Handler().postDelayed(() -> codeEntered(code), 200);
        });
    }

    @AfterViews
    void init() {
        pinView.reset();
        prompt.setText(title);
    }

    private void codeEntered(String enteredCode) {
        new Handler().postDelayed(() -> codeEnter.onNext(enteredCode), 200);

        if (!this.code.equalsIgnoreCase("")) {
            if (this.code.equalsIgnoreCase(enteredCode)) {
                new Handler().postDelayed(() -> correctCodeEnter.onNext(null), 200);
            } else {
                pinView.error();
            }
        }

        new Handler().postDelayed(this::reset, 800);
    }

    public void reset() {
        passwordView.reset();
        pinView.reset();
    }

    private void numKeySelected() {
        pinView.addPin();
    }

    private void deleteKeyPressed() {
        pinView.removePin();
    }

    public Observable<Void> correctCodeEntered() {
        return correctCodeEnter.asObservable();
    }

    public Observable<String> codeEntered() {
        return codeEnter.asObservable();
    }

    public Observable<Void> backKeyPressed() {
        return backKeyPress.asObservable();
    }
}
