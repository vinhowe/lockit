package com.lockit;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class SystemApplication {
    public static List<Application> supportedSystemApp(Context context) {
        List<Application> applications = new ArrayList<>();
        applications.add(installUninstallApp(context));
        applications.add(settingApp(context));
        applications.add(dialerApp(context));
        applications.add(recentApp(context));
        return applications;
    }

    private static Application dialerApp(Context context) {
        Application application = new Application("Call", "com.android.dialer", drawable(context, R.drawable.incoming_call));
        application.setAppType(Application.APP_TYPE.SYSTEM);
        return application;
    }

    private static Application settingApp(Context context) {
        Application application = new Application("Settings", "com.android.settings", drawable(context, R.drawable.setting));
        application.setAppType(Application.APP_TYPE.SYSTEM);
        return application;
    }

    public static Application installUninstallApp(Context context) {
        Application application = new Application("Install/Uninstall", "com.android.packageinstaller", drawable(context, R.drawable.apk));
        application.setAppType(Application.APP_TYPE.SYSTEM);
        return application;
    }

    private static Application recentApp(Context context) {
        Application application = new Application("Recent tasks", "com.android.systemui", drawable(context, R.drawable.recent_tasks));
        application.setAppType(Application.APP_TYPE.SYSTEM);
        return application;
    }

    private static Drawable drawable(Context context, int setting) {
        return context.getResources().getDrawable(setting);
    }
}
