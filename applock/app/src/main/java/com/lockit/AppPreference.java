package com.lockit;

import com.github.pwittchen.prefser.library.Prefser;

public class AppPreference {
    private static Prefser prefser;

    private AppPreference() {
    }

    public static Prefser prefs() {
        if (prefser == null) prefser = new Prefser(AppLockApplication.instance());
        return prefser;
    }
}
