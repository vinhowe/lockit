package com.lockit;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LockedApplication {
    private static LockedApplication lockedApplication;
    private Set<String> lockedApplications;

    private LockedApplication() {
        reload();
    }

    public void reload() {
        lockedApplications = new HashSet<>(lockedAppsFromPrefs());
    }

    private List<String> lockedAppsFromPrefs() {
        return GsonProvider.gson().fromJson(lockedAppsFromPrefsAsJson(), stringListType());
    }

    private Type stringListType() {
        return new TypeToken<List<String>>() {
        }.getType();
    }

    private String lockedAppsFromPrefsAsJson() {
        return AppPreference.prefs().get(PreferenceType.LOCKED_APPLICATIONS.toString(), String.class, "[]");
    }

    public static LockedApplication instance() {
        if (lockedApplication == null)
            lockedApplication = new LockedApplication();
        return lockedApplication;
    }

    public void add(String packageName) {
        lockedApplications.add(packageName);
        updatePrefs();
    }

    public void remove(String packageName) {
        lockedApplications.remove(packageName);
        updatePrefs();
    }

    public boolean contains(String packageName) {
        return lockedApplications.contains(packageName);
    }

    private void updatePrefs() {
        AppPreference.prefs().put(PreferenceType.LOCKED_APPLICATIONS.toString(), GsonProvider.gson().toJson(lockedApplications));
    }
}
