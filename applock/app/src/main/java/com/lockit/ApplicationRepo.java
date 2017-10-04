package com.lockit;

import java.util.List;

public interface ApplicationRepo {
    List<Application> all();

    void lockApp(String packageName);

    void unlockApp(String packageName);
}
