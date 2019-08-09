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
-keepattributes SourceFile,LineNumberTable,*Annotation*

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


####### SUPPORT LIBRARIES #######
# Hide warnings about references to newer platforms in the library
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-keep public class android.support.v7..design.R$* { *; }
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }
-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

# restrict obfuscation of classes extended from Activity
-keep public class * extends android.app.Activity

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
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }



-dontwarn com.squareup.okhttp.**
-keep public class * extends java.lang.Exception

####### SERVICES #######
-keep class com.pb.apszone.service.model.** {*;}
-keep class com.pb.apszone.service.model.*$** {*;}
-keep class com.pb.apszone.service.rest.** {*;}
-keep class com.pb.apszone.service.rest.*$** {*;}
-keep class com.pb.apszone.service.repo.** {*;}
-keep class com.pb.apszone.service.repo.*$** {*;}
-keep class com.pb.apszone.viewModel.** {*;}
-keep class com.pb.apszone.viewModel.*$** {*;}


-assumenosideeffects class android.util.Log {
     public static * d(...);
     public static * w(...);
     public static * v(...);
     public static * i(...);
}

-keep class com.google.** { *; }
-keep class com.github.** { *; }
-keep class org.apache.** { *; }
-keep class com.android.** { *; }
-keep interface android.support.** { *; }
-keep class android.support.** { *; }


## Android architecture components: Lifecycle
# LifecycleObserver's empty constructor is considered to be unused by proguard
-keepclassmembers class * implements android.arch.lifecycle.LifecycleObserver {
    <init>(...);
}
# ViewModel's empty constructor is considered to be unused by proguard
-keepclassmembers class * extends android.arch.lifecycle.ViewModel {
    <init>(...);
}
# keep Lifecycle State and Event enums values
-keepclassmembers class android.arch.lifecycle.Lifecycle$State { *; }
-keepclassmembers class android.arch.lifecycle.Lifecycle$Event { *; }
# keep methods annotated with @OnLifecycleEvent even if they seem to be unused
# (Mostly for LiveData.LifecycleBoundObserver.onStateChange(), but who knows)
-keepclassmembers class * {
    @android.arch.lifecycle.OnLifecycleEvent *;
}

-dontnote android.arch.lifecycle.**