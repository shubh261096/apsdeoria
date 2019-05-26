package com.pb.apszone.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public static String getDayOfWeek() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        Date date = new Date();
        return sdf.format(date);
    }

    public static String getFormatedDateTime(String dateStr) {

        String formattedDate = dateStr;

        DateFormat readFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        DateFormat writeFormat = new SimpleDateFormat("HH:mm a", Locale.getDefault());

        Date date = null;

        try {
            date = readFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            formattedDate = writeFormat.format(date);
        }

        return formattedDate;
    }

    private static String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Log.i("Time Now", df.format(Calendar.getInstance().getTime()));
        return df.format(Calendar.getInstance().getTime());
    }

    public static boolean isTimeBetweenTwoTime(String initialTime, String finalTime) throws ParseException {
        try {
            Date time1 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(initialTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);

            Date time2 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(finalTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);

            Date d = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(getCurrentTime());
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();
            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
