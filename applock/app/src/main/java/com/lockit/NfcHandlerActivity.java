//package com.lockit;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//
//public class NfcHandlerActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        Intent intent = getIntent();
//        String action = intent.getAction();
//        Uri data = intent.getData();
//
//        if (data != null) {
//            Log.d("NfcHandlerActivity", data.toString());
//        }
//
//        boolean isLocked = AppPreference.prefs().get(PreferenceType.IS_APP_LOCKED.toString(), Boolean.TYPE, true);
//        AppPreference.prefs().put(PreferenceType.IS_APP_LOCKED.toString(), !isLocked);
//
//        if (isLocked) {
//            AppLockService.stop(this);
//        } else {
//            AppLockService.start(this);
//        }
//        Log.d("NfcHandlerActivity", isLocked ? "Locked" : "Unlocked" + " nlock");
//
//        finish();
//    }
//}