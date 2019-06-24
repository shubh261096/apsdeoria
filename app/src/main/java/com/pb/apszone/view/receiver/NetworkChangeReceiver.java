package com.pb.apszone.view.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pb.apszone.utils.CommonUtils;
import com.pb.apszone.view.listener.NetworkChangeListener;

public class NetworkChangeReceiver extends BroadcastReceiver {
    boolean status;
    NetworkChangeListener networkChangeListener;

    public NetworkChangeReceiver(NetworkChangeListener networkChangeListener){
        this.networkChangeListener = networkChangeListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        status = CommonUtils.getConnectivityStatus(context);
        networkChangeListener.getNetworkData(status);
    }



}
