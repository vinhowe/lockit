package com.lockit.apps;

import com.lockit.Application;

import java.util.List;

import rx.Observable;

public interface ApplicationView {
    Observable<Void> initialized();

    void showAllApps(List<Application> all);

    Observable<String> appLocked();

    void showAppLocked();

    Observable<String> appUnlocked();

    void showAppUnlocked();
}
