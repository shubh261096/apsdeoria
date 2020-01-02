package com.pb.apszone.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

public class DialogHelper extends DialogHelperFragment {
    private SimpleAlertListener listener;
    private String positiveText, negativeText;

    public static DialogHelper build(FragmentActivity activity, String title, String message,
                                     String positiveText, String negativeText,
                                     SimpleAlertListener listener) {
        DialogHelper dialogAlert = new DialogHelper();
        dialogAlert.setAlertTitle(title);
        dialogAlert.setAlertMessage(message);
        dialogAlert.setAlertListener(listener);
        dialogAlert.setAlertActivity(activity);
        dialogAlert.setAlertButtonText(positiveText, negativeText);
        return dialogAlert;
    }


    @SuppressWarnings("unused")
    public void setAlertIcon(int drawable) {
    }

    private void setAlertButtonText(String positiveText, String negativeText) {
        this.positiveText = positiveText;
        this.negativeText = negativeText;
    }

    private void setAlertListener(SimpleAlertListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity())
                        .setTitle(getAlertTitle())
                        .setMessage(getAlertMessage());
        if (positiveText != null) {
            builder.setPositiveButton(positiveText,
                    (dialog, whichButton) -> {
                        listener.onPositive();
                        dialog.dismiss();
                    });
        }

        if (negativeText != null) {
            builder.setNegativeButton(negativeText,
                    (dialog, whichButton) -> {
                        listener.onNegative();
                        dialog.dismiss();
                    });
        }

        return builder.create();
    }

    public interface SimpleAlertListener {
        void onPositive();

        void onNegative();
    }
}