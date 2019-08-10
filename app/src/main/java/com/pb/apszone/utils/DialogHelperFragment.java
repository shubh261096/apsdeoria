package com.pb.apszone.utils;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

public abstract class DialogHelperFragment extends DialogFragment {
    private FragmentActivity activity;
    private String alertMessage;
    private String alertTitle;

    public void show() {
        this.show(activity.getSupportFragmentManager(), null);
    }

    public void setAlertMessage(String message) {
        this.alertMessage = message;
    }

    protected String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertTitle(String alertTitle) {
        this.alertTitle = alertTitle;
    }

    protected String getAlertTitle() {
        return alertTitle;
    }

    protected void setAlertActivity(FragmentActivity activity) {
        this.activity = activity;
    }

}