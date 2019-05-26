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

    public static boolean isTimeBetweenTwoTime(String argStartTime,
                                               String argEndTime) throws ParseException {
        String reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
        if (argStartTime.matches(reg) && argEndTime.matches(reg)
                && getCurrentTime().matches(reg)) {
            boolean valid;
            // Start Time
            java.util.Date startTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    .parse(argStartTime);
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startTime);

            // Current Time
            java.util.Date currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    .parse(getCurrentTime());
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(currentTime);

            // End Time
            java.util.Date endTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    .parse(argEndTime);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endTime);

            if (currentTime.compareTo(endTime) < 0) {
                currentCalendar.add(Calendar.DATE, 1);
                currentTime = currentCalendar.getTime();
            }

            if (startTime.compareTo(endTime) < 0) {

                startCalendar.add(Calendar.DATE, 1);
                startTime = startCalendar.getTime();

            }
            if (currentTime.before(startTime)) {
                System.out.println(" Time is Lesser ");
                valid = false;
            } else {
                if (currentTime.after(endTime)) {
                    endCalendar.add(Calendar.DATE, 1);
                    endTime = endCalendar.getTime();
                }
                System.out.println("Comparing , Start Time /n " + startTime);
                System.out.println("Comparing , End Time /n " + endTime);
                System.out.println("Comparing , Current Time /n " + currentTime);
                if (currentTime.before(endTime)) {
                    System.out.println("RESULT, Time lies b/w");
                    valid = true;
                } else {
                    valid = false;
                    System.out.println("RESULT, Time does not lies b/w");
                }
            }
            return valid;
        } else {
            throw new IllegalArgumentException("Not a valid time, expecting HH:MM:SS format");
        }
    }
}
