package com.pb.apszone.view.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pb.apszone.utils.CommonUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {
    boolean status;
    private final static MutableLiveData<Boolean> networkChange = new MutableLiveData<>();

    public void setStatus(Boolean status) {
        networkChange.postValue(status);
    }

    public static LiveData<Boolean> getStatus() {
        return networkChange;
    }

    public NetworkChangeReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        status = CommonUtils.getConnectivityStatus(context);
        setStatus(status);
    }

}
