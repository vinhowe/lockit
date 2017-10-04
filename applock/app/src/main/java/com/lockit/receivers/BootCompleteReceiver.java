package com.lockit.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lockit.AppLockService;

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AppLockService.start(context);
    }
}
