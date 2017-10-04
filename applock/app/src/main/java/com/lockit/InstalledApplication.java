package com.lockit;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import com.lockit.util.Lists;

import java.util.Collections;
import java.util.List;

public class InstalledApplication implements ApplicationRepo {
    private Context context;
    private LockedApplication lockedApplication;

    public InstalledApplication(Context context) {
        this.context = context;
        lockedApplication = LockedApplication.instance();
    }

    @Override
    public List<Application> all() {
        List<ApplicationInfo> installedApplications = appInfos();
        List<Application> applications = Lists.transform(Lists.filter(installedApplications,
                this::isInstalledApp), this::application);
        Collections.sort(applications, (a1, a2) -> a1.getName().compareTo(a2.getName()));
        applications.addAll(0, SystemApplication.supportedSystemApp(context));
        setLockStatusAndAppType(applications);
        return applications;
    }

    private List<ApplicationInfo> appInfos() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> appResolveInfos = pm().queryIntentActivities(intent, PackageManager.GET_META_DATA);
        return Lists.transform(appResolveInfos, resolveInfo -> resolveInfo.activityInfo.applicationInfo);
    }

    public List<Application> locked() {
        return Lists.filter(all(), (Application::isLocked));
    }

    private PackageManager pm() {
        return context.getPackageManager();
    }

    private void setLockStatusAndAppType(List<Application> applications) {
        for (Application application : applications)
            setLockStatusAndAppType(application);
    }

    @Override
    public void lockApp(String packageName) {
        lockedApplication.add(packageName);
    }

    @Override
    public void unlockApp(String packageName) {
        lockedApplication.remove(packageName);
    }

    private boolean isInstalledApp(ApplicationInfo appInfo) {
        if (context.getPackageName().equals(appInfo.packageName))
            return false;
//        if ((appInfo.flags & appInfo.FLAG_SYSTEM) != 0)
//            return false;
//        if ((appInfo.flags & appInfo.FLAG_UPDATED_SYSTEM_APP) != 0)
//            return false;
        return true;
    }

    private Application application(ApplicationInfo appInfo) {
        try {
            return new Application(appInfo.loadLabel(pm()).toString(), appInfo.packageName, icon(appInfo.packageName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setLockStatusAndAppType(Application application) {
        application.setLocked(lockedApplication.contains(application.getPackageName()));
        application.setAppType(Application.APP_TYPE.appType(application.getPackageName()));
    }

    private Drawable icon(String packageName) throws PackageManager.NameNotFoundException {
        return pm().getApplicationIcon(packageName);
    }

    public Application locked(String currentPackage) {
        for (Application application : locked()) {
            if (application.getPackageName().equalsIgnoreCase(currentPackage))
                return application;
        }

        return null;
    }
}
