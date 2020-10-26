package com.pb.apszone;

import android.app.Application;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;
import java.util.Map;


import static com.pb.apszone.BuildConfig.BUILD_TYPE;
import static com.pb.apszone.utils.CommonUtils.getVersionCode;

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

            FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .build();
            remoteConfig.setConfigSettings(configSettings);

            Map<String, Object> defaultValue = new HashMap<>();
            defaultValue.put("update_type", getVersionCode(this) + "#FLEXIBLE#5");

            remoteConfig.setDefaultsAsync(defaultValue);
            remoteConfig.fetchAndActivate()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            remoteConfig.activate();
                            Log.i("App", "Fetch and activate succeeded");
                        } else {
                            Log.i("App", "Fetch failed");
                        }
                    });
        }
    }
}