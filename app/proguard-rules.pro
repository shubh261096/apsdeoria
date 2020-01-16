# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.

-dontwarn com.squareup.okhttp.**

# restrict obfuscation of data members which has parcelable implemention
-keepclassmembers class * implements android.os.Parcelable {
 static ** CREATOR;
 }

####### ButterKnife ########
-keep public class * implements butterknife.internal.ViewBinder { public <init>(); }
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }
-dontwarn butterknife.internal.Utils

####### CRASHLYTICS #######
-keep class com.crashlytics.** { *; }
-keep class com.crashlytics.android.**
-keepattributes SourceFile, LineNumberTable, *Annotation*
-printmapping mapping.txt
-keep public class * extends java.lang.Exception

####### RETROFIT ########
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-dontwarn okhttp3.**
-dontwarn okio.**

####### GSON ########
-keepattributes EnclosingMethod
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }


####### MODELS #######
-keep class com.pb.apszone.service.model.** {*;}
-keep class com.pb.apszone.service.model.*$** {*;}
-keep class com.pb.apszone.service.rest.model** {*;}
-keep class com.pb.apszone.service.rest.model*$** {*;}