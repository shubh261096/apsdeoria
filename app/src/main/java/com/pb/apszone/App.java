package com.pb.apszone;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

import static com.pb.apszone.BuildConfig.BUILD_TYPE;

@SuppressWarnings("ConstantConditions")
public class App extends Application {
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (BUILD_TYPE.equals("release")) {
            Fabric.with(this, new Crashlytics());
        }
    }
}
