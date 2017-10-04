package com.lockit;

import android.widget.Toast;

public class Notifier {
    public static void toast(String msg) {
        Toast.makeText(AppLockApplication.instance(), msg, Toast.LENGTH_SHORT).show();
    }
}
