package com.lockit.apps;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AppsViewModel extends ViewModel {
    private MutableLiveData<Void> initialization;
    private MutableLiveData<String> appLock;
    private MutableLiveData<String> appUnlock;

    public MutableLiveData<Void> getInitialization() {
        if (initialization == null) {
            initialization = new MutableLiveData<>();
        }
        return initialization;
    }

    public MutableLiveData<String> getAppLock() {
        if (appLock == null) {
            appLock = new MutableLiveData<>();
        }
        return appLock;
    }

    public MutableLiveData<String> getAppUnlock() {
        if (appUnlock == null) {
            appUnlock = new MutableLiveData<>();
        }
        return appUnlock;
    }
}
