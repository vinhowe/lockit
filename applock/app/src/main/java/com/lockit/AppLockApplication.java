package com.lockit;

import android.content.Context;
import androidx.multidex.MultiDexApplication;

public class AppLockApplication extends MultiDexApplication {
    private static AppLockApplication application;

    public static Context instance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}
