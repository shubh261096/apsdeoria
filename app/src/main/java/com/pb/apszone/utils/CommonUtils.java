package com.pb.apszone.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class CommonUtils {
    private static ProgressDialog progressDialog;

    public static void showProgress(Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void hideProgress() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

}
