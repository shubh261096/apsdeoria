package com.pb.apszone.utils;


import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

public abstract class DialogHelperFragment extends DialogFragment {
    private FragmentActivity activity;
    private String alertMessage;
    private String alertTitle;

    public void show() {
        this.show(activity.getSupportFragmentManager(), null);
    }

    void setAlertMessage(String message) {
        this.alertMessage = message;
    }

    String getAlertMessage() {
        return alertMessage;
    }

    void setAlertTitle(String alertTitle) {
        this.alertTitle = alertTitle;
    }

    String getAlertTitle() {
        return alertTitle;
    }

    void setAlertActivity(FragmentActivity activity) {
        this.activity = activity;
    }

}