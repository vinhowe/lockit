package com.lockit;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class LockService extends Service {
    public static final String ACTION_LOCK = "com.applock.intent.action_lock";
    public static final String ACTION_UNLOCK = "com.applock.intent.action_unlock";
    public static String EXTRA_PACKAGENAME = "EXTRA_PACKAGENAME";
    private WindowManager windowManager;
    private Intent mIntent;
    private LayoutParams layoutParams;
    private LockView lockView;
    private Application application;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static Intent lockIntent(Context context, String packageName) {
        Intent intent = new Intent(context, LockService.class);
        intent.setAction(ACTION_LOCK);
        intent.putExtra(EXTRA_PACKAGENAME, packageName);
        return intent;
    }

    public static Intent unlockIntent(Context context, String packageName) {
        Intent intent = new Intent(context, LockService.class);
        intent.setAction(ACTION_UNLOCK);
        intent.putExtra(EXTRA_PACKAGENAME, packageName);
        return intent;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("AppLockService", "LockService onStartCommand: ");

        if (intent == null) {
            return START_NOT_STICKY;
        }
        mIntent = intent;
        application = new InstalledApplication(this).locked(intent.getExtras().getString(EXTRA_PACKAGENAME));
        Log.d("AppLockService", "LockService onStartCommand: " + intent.getExtras().getString(EXTRA_PACKAGENAME));
        if (intent.getAction().equals(ACTION_LOCK))
            showLock();
        else if (intent.getAction().equals(ACTION_UNLOCK))
            hideLock();

        return START_NOT_STICKY;
    }

    private void showLock() {
        // TODO: FIx this.
        try {
            rootView();
            windowManager().addView(lockView, layoutParam());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void hideLock() {
        // TODO: FIx this.
        try {
            if (lockView != null)
                windowManager().removeView(lockView);
            lockView = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void exit() {
        final Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

        hideLock();
    }

    private View rootView() {
        if (lockView == null) {
            lockView = new LockView(this, null);
        }

        lockView.setTitle(application.getName());
        lockView.setIcon(application.getIcon());
        lockView.setFocusable(true);
        lockView.setFocusableInTouchMode(true);
        return lockView;
    }

    private ViewGroup.LayoutParams layoutParam() {
        int layoutFlag;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutFlag = WindowManager.LayoutParams.TYPE_PHONE;
        }

        if (layoutParams == null)
            layoutParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT,
                    layoutFlag,
                    LayoutParams.FLAG_ALT_FOCUSABLE_IM
                            | LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | LayoutParams.FLAG_FULLSCREEN
                            | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSLUCENT);
        return layoutParams;
    }

    private WindowManager windowManager() {
        if (windowManager == null)
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        return windowManager;
    }
}
