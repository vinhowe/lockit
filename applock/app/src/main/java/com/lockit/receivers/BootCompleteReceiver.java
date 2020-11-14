package com.lockit.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lockit.AppLockService;
import com.lockit.AppPreference;
import com.lockit.PreferenceType;

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isLocked = AppPreference.prefs().get(PreferenceType.IS_APP_LOCKED.toString(), Boolean.TYPE, false);
        if (isLocked) {
            AppLockService.start(context);
        }
    }
}
