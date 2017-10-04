package com.lockit.widget;

import android.content.Context;
import android.support.annotation.Keep;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.lockit.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;

import rx.subjects.PublishSubject;

@EViewGroup(R.layout.password_view)
public class PasswordView extends LinearLayout {
    public PublishSubject<Void> numKey = PublishSubject.create();
    public PublishSubject<Void> backKey = PublishSubject.create();
    public PublishSubject<Void> deleteKey = PublishSubject.create();
    public PublishSubject<String> fourKeyEntered = PublishSubject.create();

    int index = -1;
    int selected[];

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init() {
        reset();
    }

    @Click(R.id.key0)
    void key0() {
        keyPressed(0);
    }

    @Click(R.id.key1)
    void key1() {
        keyPressed(1);
    }

    @Click(R.id.key2)
    void key2() {
        keyPressed(2);
    }

    @Click(R.id.key3)
    void key3() {
        keyPressed(3);
    }

    @Click(R.id.key4)
    void key4() {
        keyPressed(4);
    }

    @Click(R.id.key5)
    void key5() {
        keyPressed(5);
    }

    @Click(R.id.key6)
    void key6() {
        keyPressed(6);
    }

    @Click(R.id.key7)
    void key7() {
        keyPressed(7);
    }

    @Click(R.id.key8)
    void key8() {
        keyPressed(8);
    }

    @Click(R.id.key9)
    void key9() {
        keyPressed(9);
    }

    @Click(R.id.key_back)
    void backKey() {
        backKey.onNext(null);
    }

    @Click(R.id.key_delete)
    void deleteKey() {
        deleteKey.onNext(null);
        if (index > 3) index = 3;
        if (index < 0) index = 0;
        index--;
    }

    private void keyPressed(int key) {
        index++;
        numKey.onNext(null);
        if (index < 4)
            selected[index] = key;
        if (index == 3)
            fourKeyEntered.onNext(code());
    }

    private String code() {
        String code = "";
        for (int i : selected)
            code += i;
        return code;
    }

    public void reset() {
        index = -1;
        selected = new int[4];
    }
}
