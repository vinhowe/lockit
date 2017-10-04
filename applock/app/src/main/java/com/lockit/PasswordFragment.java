package com.lockit;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lockit.widget.FourPinView;
import com.lockit.widget.PasswordView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import rx.Observable;
import rx.subjects.PublishSubject;

@EFragment(R.layout.password_fragment)
public class PasswordFragment extends BaseFragment {
    private PublishSubject<String> codeEnter = PublishSubject.create();
    private PublishSubject<Void> correctCodeEnter = PublishSubject.create();

    @FragmentArg
    String code = "";

    @FragmentArg
    String title = "";

    @ViewById(R.id.icon)
    ImageView icon;

    @ViewById(R.id.password_view)
    PasswordView passwordView;

    @ViewById(R.id.pin_view)
    FourPinView pinView;

    @ViewById(R.id.prompt)
    TextView prompt;

    @AfterViews
    void registerEvents() {
        passwordView.numKey.subscribe(i -> numKeySelected());
        passwordView.backKey.subscribe(i -> backKeyPressed());
        passwordView.deleteKey.subscribe(i -> deleteKeyPressed());
        passwordView.fourKeyEntered.subscribe((code) -> {
            new Handler().postDelayed(() -> codeEntered(code), 200);
        });
    }

    @AfterViews
    void init() {
        pinView.reset();
        prompt.setVisibility(View.VISIBLE);
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

    private void reset() {
        passwordView.reset();
        pinView.reset();
    }

    private void numKeySelected() {
        pinView.addPin();
    }

    private void backKeyPressed() {
        getActivity().finish();
    }

    private void deleteKeyPressed() {
        pinView.removePin();
    }

    public Observable<Void> correctCodeEntered() {
        return bindObservable(correctCodeEnter.asObservable());
    }

    public Observable<String> codeEntered() {
        return bindObservable(codeEnter.asObservable());
    }
}
