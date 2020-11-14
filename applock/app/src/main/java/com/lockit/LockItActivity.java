package com.lockit;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.lockit.apps.LockedFragment;
import com.lockit.databinding.FragmentContainerBinding;

public class LockItActivity extends BaseActivity {
    private FragmentContainerBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Uri data = intent.getData();

        boolean isLocked = AppPreference.prefs().get(PreferenceType.IS_APP_LOCKED.toString(), Boolean.TYPE, false);
        Log.d(LockItActivity.class.getSimpleName(), String.valueOf(isLocked));
        Log.d(LockItActivity.class.getSimpleName(), String.valueOf(AppPreference.prefs().hashCode()));

        binding = FragmentContainerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        boolean isTogglingIntent = data != null && data.toString().equals("nlock://toggle");

        if (isTogglingIntent) {
            isLocked = !isLocked;
            AppPreference.prefs().put(PreferenceType.IS_APP_LOCKED.toString(), isLocked);

            Log.d("NfcHandlerActivity", isLocked ? "Locked" : "Unlocked" + " nlock");
        }

        if (isLocked) {
            AppLockService.start(this);
        } else {
            AppLockService.stop(this);
        }

        if (isLocked || isTogglingIntent) {
            LockedFragment lockedFragment = new LockedFragment(isLocked);
            Fragments.replaceContentFragment(this, R.id.container, lockedFragment, LockedFragment.class.getSimpleName());
            // Show for a second before hiding
            new Handler().postDelayed(this::finish, 1000);
            return;
        }

        finish();
        init();
    }

    private Notification createNotification() {
        String notificationChannelId = "nlock_service";

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel channel = new NotificationChannel(
                    notificationChannelId,
                    "nlock blocker service channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription("");

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, LockItActivity.class), 0);

        Notification.Builder builder = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? new Notification.Builder(this, notificationChannelId) : new Notification.Builder(this);

        return builder.setContentTitle("nlock service").setContentIntent(pendingIntent).setSmallIcon(R.mipmap.ic_launcher).setPriority(Notification.PRIORITY_HIGH).build();
    }

    void init() {
        new InstalledApplication(this).lockApp(SystemApplication.installUninstallApp(this).getPackageName());
        navigateHome();
        finish();
    }

    private void navigateHome() {
        finish();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.recreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        boolean isLocked = AppPreference.prefs().get(PreferenceType.IS_APP_LOCKED.toString(), Boolean.TYPE, false);
        if (isLocked) {
            AppLockService.start(this);
        } else {
            AppLockService.stop(this);
        }
    }
}
