# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/shivam/android/tool/android-sdk-linux/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep class org.apache.http.** { *; }
-keepclassmembers class org.apache.http.** {*;}
-dontwarn org.apache.**

-keep class android.net.http.** { *; }
-keepclassmembers class android.net.http.** {*;}
-dontwarn android.net.**

-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.app.** { *; }
-keep interface android.support.v7.app.** { *; }

-keep public class com.lockit.** { *; }
-keepclassmembers public class com.lockit.** { *; }
-dontwarn com.lockit.**

-keep public class com.droipp.commons.** { *; }
-keepclassmembers public class com.droipp.commons.** { *; }
-dontwarn com.droipp.commons.**


# -- Android Annotations --
-dontwarn org.springframework.**

# Retrofit 1.X

-keep class com.squareup.okhttp.** { *; }
-keep class retrofit.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-dontwarn retrofit.**
-dontwarn rx.**

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

# If in your rest service interface you use methods with Callback argument.
-keepattributes Exceptions

# If your rest service methods throw custom exceptions, because you've defined an ErrorHandler.
-keepattributes Signature

# Also you must note that if you are using GSON for conversion from JSON to POJO representation, you must ignore those POJO classes from being obfuscated.
# Here include the POJO's that have you have created for mapping JSON response to POJO for example.

-keep public class * extends android.support.v7.widget.RecyclerView$LayoutManager {
    public <init>(...);
}