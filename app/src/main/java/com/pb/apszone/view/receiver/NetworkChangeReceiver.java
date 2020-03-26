package com.pb.apszone.view.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pb.apszone.utils.CommonUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private static MutableLiveData<Boolean> networkChange = null;

    public NetworkChangeReceiver() {
        networkChange = new MutableLiveData<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        networkChange.setValue(CommonUtils.getConnectivityStatus(context));
    }

    public static LiveData<Boolean> getStatus() {
        return networkChange;
    }

}
