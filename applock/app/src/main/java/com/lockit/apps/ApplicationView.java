package com.lockit.apps;

import androidx.lifecycle.Observer;

import com.lockit.Application;

import java.util.List;

public interface ApplicationView {
    void initialized(Observer<Void> observer);

    void showAllApps(List<Application> all);

    void appLocked(Observer<String> observer);

    void showAppLocked();

    void appUnlocked(Observer<String> observer);

    void showAppUnlocked();
}
