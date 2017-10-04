package com.lockit;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Application implements Serializable {
    public enum APP_TYPE {
        IMPORTANT(0), SYSTEM(1), NORMAL(2);

        List<String> importantApps = Arrays.asList("com.android.vending", "com.android.settings",
                "com.android.packageinstaller");
        List<String> systemApps = Arrays.asList("com.android.vending", "com.android.settings",
                "com.android.packageinstaller");

        private int priority;

        APP_TYPE(int priority) {
            this.priority = priority;
        }

        public int priority() {
            return priority;
        }

        public static APP_TYPE appType(String packageName) {
            return NORMAL;
        }
    }

    private String name;
    private String packageName;
    private Drawable icon;
    private APP_TYPE appType;
    private boolean locked;

    public Application(String name, String packageName, Drawable icon) {
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
        locked = false;
        appType = APP_TYPE.NORMAL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public APP_TYPE getAppType() {
        return appType;
    }

    public void setAppType(APP_TYPE appType) {
        this.appType = appType;
    }

    public void toggleLockState() {
        locked = !locked;
    }
}
