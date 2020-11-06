package com.lockit.apps;

import com.lockit.ApplicationRepo;

public class AppsPresenter {
    public AppsPresenter(ApplicationRepo repo, ApplicationView view) {
        view.initialized(__ -> view.showAllApps(repo.all()));
        view.appLocked(packageName -> {
            repo.lockApp(packageName);
            view.showAppLocked();
        });
        view.appUnlocked(packageName -> {
            repo.unlockApp(packageName);
            view.showAppUnlocked();
        });
    }
}
