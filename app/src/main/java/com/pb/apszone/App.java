package com.pb.apszone;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.crashlytics.android.Crashlytics;
import com.pb.apszone.view.receiver.NetworkChangeReceiver;

import io.fabric.sdk.android.Fabric;

import static com.pb.apszone.BuildConfig.BUILD_TYPE;

@SuppressWarnings("ConstantConditions")
public class App extends Application {
    private static App instance;
    NetworkChangeReceiver networkChangeReceiver;

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
        /* Starting observer of Internet change*/
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void unregisterNetworkChangeReceiver(){
        unregisterReceiver(networkChangeReceiver);
    }
}
