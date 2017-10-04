package com.lockit.apps;

import com.lockit.ApplicationRepo;

public class AppsPresenter {
    public AppsPresenter(ApplicationRepo repo, ApplicationView view) {
        view.initialized().subscribe(ignored -> view.showAllApps(repo.all()));
        view.appLocked().subscribe(packageName -> {
            repo.lockApp(packageName);
            view.showAppLocked();
        });
        view.appUnlocked().subscribe(packageName -> {
            repo.unlockApp(packageName);
            view.showAppUnlocked();
        });
    }
}
