package com.lockit;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

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
