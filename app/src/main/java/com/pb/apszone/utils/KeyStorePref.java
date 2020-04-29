package com.pb.apszone.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.pb.apszone.viewModel.DashboardViewModel;

public class KeyStorePref {
    private static KeyStorePref store;
    private final SharedPreferences SP;
    private static final String filename = "com.pb.apszone";

    private KeyStorePref(Context context) {
        SP = context.getApplicationContext().getSharedPreferences(filename, 0);
    }

    public static KeyStorePref getInstance(Context context) {
        if (store == null) {
            store = new KeyStorePref(context);
        }
        return store;
    }

    public boolean getBoolean(String key) {
        return SP.getBoolean(key, false);
    }

    public void putBoolean(String key, boolean val) {
        Editor editor = SP.edit();
        editor.putBoolean(key, val);
        editor.apply();
    }

    public void putString(String key, String val) {
        Editor editor = SP.edit();
        editor.putString(key, val);
        editor.apply();
    }

    public String getString(String key) {
        return SP.getString(key, "");
    }

    public void putLong(String key, long val) {
        Editor editor = SP.edit();
        editor.putLong(key, val);
        editor.apply();
    }

    public long getLong(String key) {
        return SP.getLong(key, 0);
    }

    public void clearAllPref() {
        SP.edit().clear().apply();
        DashboardViewModel.unsubscribeFromAllTopic();
    }

}