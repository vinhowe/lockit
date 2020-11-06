package com.lockit;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.app.ActivityManager.RunningTaskInfo;

public class AppLockService extends Service {
    private static final String ACTION_START = "com.applock.intent.action.start_lock_service";
    private static final String ACTION_STOP = "com.applock.intent.action.stop_lock_service";
    private static final int REQUEST_CODE = 1001;
    private static PendingIntent appLockServiceIntent;
    private String lastPackage = "";
    private static boolean isAlarmStarted = false;
    private ActivityManager activityManager;
    private boolean destroy = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("AppLockService", "AppLockService: onStartCommand called - ");

        if (intent == null || intent.getAction().equalsIgnoreCase(ACTION_START)) {
            if (!isAlarmStarted) {
                init();
                start(this);
            }
            checkAppChanged();
        } else if (intent.getAction().equalsIgnoreCase(ACTION_STOP)) {
            stopAlarmAndStopSelf();
        }
        return START_STICKY;
    }

    private void init() {
        registerReceiver(new ScreenOnOffReceiver(), screenOnOffFilter());
    }

    @NonNull
    private IntentFilter screenOnOffFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        return filter;
    }

    private void checkAppChanged() {
        String currentPackage = currentPackage();
        Log.d("AppLockService", "Package: " + currentPackage);
        if (!currentPackage.equalsIgnoreCase(lastPackage)) {
            LockedApplication.instance().reload();
            if (LockedApplication.instance().contains(currentPackage))
                lockedAppOpened(currentPackage);
            else lockedAppClosed(currentPackage);
        }
        lastPackage = currentPackage;
    }

    private void lockedAppOpened(String packageName) {
        Log.d("AppLockService", "Lock Package: " + packageName);
        startService(LockService.lockIntent(this, packageName));
    }

    private void lockedAppClosed(String packageName) {
        Log.d("AppLockService", "Unlock Package: " + packageName);
        startService(LockService.unlockIntent(this, packageName));
    }

    private String currentPackage() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                    time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(),
                            usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    return mySortedMap.get(
                            mySortedMap.lastKey()).getPackageName();
                }
            }
        }

        return currentTask().topActivity.getPackageName();
    }

    private RunningTaskInfo currentTask() {
        if (activityManager == null)
            activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        return activityManager.getRunningTasks(1).get(0);
    }

    public static void start(Context context) {
        ScheduledExecutorService scheduleTaskExecutor = Executors.newSingleThreadScheduledExecutor();

        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                try {
                    getRunIntent(context).send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
        isAlarmStarted = true;
    }

    public static void stop(Context context) {
        isAlarmStarted = false;
    }

    private static PendingIntent getRunIntent(Context context) {
        if (appLockServiceIntent == null) {
            Intent intent = new Intent(context, AppLockService.class);
            intent.setAction(ACTION_START);
            appLockServiceIntent = PendingIntent.getService(context, REQUEST_CODE, intent, 0);
        }
        return appLockServiceIntent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("AppLockService", "onDestroy called - " + destroy);
        if (!destroy) {
            start(this);
        }

        destroy = false;
    }

    private void stopAlarmAndStopSelf() {
        destroy = true;
        stop(this);
        stopSelf();
    }

    private class ScreenOnOffReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Log.d("AppLockService", "ACTION_SCREEN_ON");
                start(AppLockService.this);
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.d("AppLockService", "ACTION_SCREEN_OFF");
                lastPackage = "";
                stop(AppLockService.this);
            }
        }
    }
}
