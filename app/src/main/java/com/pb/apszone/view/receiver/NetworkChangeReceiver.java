package com.pb.apszone.view.receiver;

import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.pb.apszone.utils.CommonUtils;
import com.pb.apszone.viewModel.SharedViewModel;

public class NetworkChangeReceiver extends BroadcastReceiver {
    boolean status;
    SharedViewModel sharedViewModel;

    public NetworkChangeReceiver(FragmentActivity activity){
        sharedViewModel = ViewModelProviders.of(activity).get(SharedViewModel.class);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        status = CommonUtils.getConnectivityStatus(context);
        sharedViewModel.setStatus(status);
    }



}
