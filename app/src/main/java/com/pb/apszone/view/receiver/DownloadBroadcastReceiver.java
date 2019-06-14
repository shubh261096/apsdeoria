package com.pb.apszone.view.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import static com.pb.apszone.utils.AppConstants.KEY_DOWNLOAD_ID;

public class DownloadBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        //Checking if the received broadcast is for our enqueued download by matching download id
        if (KEY_DOWNLOAD_ID == id) {
            Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
        }
    }
}
