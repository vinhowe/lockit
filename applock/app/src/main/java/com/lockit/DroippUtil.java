package com.lockit;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.UUID;

public class DroippUtil {
    public static MaterialDialog.Builder rateDialog(Activity context) {
        return new MaterialDialog.Builder(context)
                .title("Rate " + context.getString(R.string.app_name)).content(
                        "Seems you have liked this app. Can you please put you valuable time to rate this app. " +
                                "Your rating and reviews will help us to improve the app and provide new features." +
                                "Thanks for your support.").positiveText("Rate Now")
                .negativeText("Later");
    }

    public static void rate(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + context.getPackageName())));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static void share(Context context, String title, String text) {
        Intent share = new Intent("android.intent.action.SEND");
        share.setType("text/plain");
        share.putExtra("android.intent.extra.EMAIL", new String[]{});
        share.putExtra("android.intent.extra.SUBJECT",
                title);
        share.putExtra("android.intent.extra.TEXT",
                text + " https://play.google.com/store/apps/details?id=" +
                        context.getPackageName());
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(share,
                "Share " + context.getResources().getString(R.string.app_name) + " :"));
    }

    public static void feedback(Context context) {
        Intent feedback = new Intent(Intent.ACTION_SENDTO);
        feedback.setType("text/plain");
        feedback.putExtra("android.intent.extra.EMAIL", new String[]{"droipp.me@gmail.com"});
        feedback.putExtra("android.intent.extra.SUBJECT",
                "Feedback for " + context.getResources().getString(R.string.app_name));
        feedback.putExtra("android.intent.extra.TEXT", "");
        feedback.setData(Uri.parse("mailto:droipp.me@gmail.com"));
        context.startActivity(Intent.createChooser(feedback,
                "Feedback for" + context.getResources().getString(R.string.app_name) + " :"));
    }

    public static String deviceId(Context context) {
        final TelephonyManager tm =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }

    public static Intent appIntent(String packageName) {
        try {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
        } catch (ActivityNotFoundException e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + packageName));
        }
    }
}
