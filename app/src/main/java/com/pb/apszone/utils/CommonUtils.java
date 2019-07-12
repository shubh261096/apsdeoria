package com.pb.apszone.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;

import com.pb.apszone.BuildConfig;
import com.pb.apszone.R;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.content.Context.DOWNLOAD_SERVICE;

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

    public static String getFormattedDateTime(String dateStr) {

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

    public static Integer getNumOfDaysInMonth(String year, String month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, getMonthNumber(month));
        calendar.set(Calendar.YEAR, Integer.valueOf(year));
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private static Integer getMonthNumber(String month) {
        Date date;
        int monthNumber = 0;
        try {
            Calendar cal = Calendar.getInstance();
            date = new SimpleDateFormat("MMMM", Locale.getDefault()).parse(month);
            cal.setTime(date);
            monthNumber = cal.get(Calendar.MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return monthNumber;
    }

    public static String getStringMonthNumber(String month) {
        Date date;
        int monthNumber = 0;
        try {
            Calendar cal = Calendar.getInstance();
            date = new SimpleDateFormat("MMMM", Locale.getDefault()).parse(month);
            cal.setTime(date);
            monthNumber = cal.get(Calendar.MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        monthNumber = monthNumber + 1; // This is done because monthNumber starts from zero for January
        if (monthNumber <= 9) {
            return "0" + monthNumber; // 0 is added in front to match the request value
        } else {
            return String.valueOf(monthNumber);
        }
    }

    public static Integer getFirstDayOfMonth(String year, String month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.YEAR, Integer.valueOf(year));
        calendar.set(Calendar.MONTH, getMonthNumber(month));
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static String getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.getDefault());
        return month_date.format(cal.getTime());
    }

    public static String getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    public static String getFirstDateValueFromFullDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
        try {
            return (String) android.text.format.DateFormat.format("dd", simpleDateFormat.parse(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static long beginDownload(String url, Context context) {
        File file = new File(Objects.requireNonNull(context).getExternalFilesDir(null), "Download");
        /*
        Create a DownloadManager.Request with all the information necessary to start the download
         */
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                .setMimeType(getMimeFromFileName(getFileNameFromURL(url)))
                .setTitle(getFileNameFromURL(url))// Title of the Download Notification
                .setDescription("Downloading") // Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) // Visibility of the download Notification
                .setDestinationUri(Uri.fromFile(file)) // Uri of the destination file
                .setRequiresCharging(false) // Set if charging is required to begin the download
                .setAllowedOverMetered(true) // Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true); // Set if download is allowed on roaming network
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            return downloadManager.enqueue(request); // enqueue puts the download request in the queue.
        } else {
            return 0;
        }
    }

    private static String getFileNameFromURL(String fileName) {
        int pos = fileName.lastIndexOf("/");
        return fileName.substring(pos + 1);
    }

    private static String getMimeFromFileName(String fileName) {
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(fileName);
        return map.getMimeTypeFromExtension(ext);
    }

    public static String getTodayDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    public static String getPreviousDate(String today) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        try {
            date = dateFormat.parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return dateFormat.format(calendar.getTime());
    }

    public static String getNextDate(String today) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        try {
            date = dateFormat.parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, +1);
        return dateFormat.format(calendar.getTime());
    }

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static void showLateFeeAlertDialog(View view, Context context) {
        //before inflating the custom alert dialog layout, we will get the current activity viewGroup
        ViewGroup viewGroup = view.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_fee_remider, viewGroup, false);

        Button buttonOk = dialogView.findViewById(R.id.buttonOk);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        buttonOk.setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.show();
    }

    public static String getTime(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date past = format.parse(time);
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if (seconds < 60) {
                return seconds + " sec ago";
            } else if (minutes < 60) {
                if (minutes == 1) {
                    return "1 min ago";
                } else {
                    return minutes + " mins ago";
                }
            } else if (hours < 24) {
                if (hours == 1) {
                    return "1 hour ago";
                } else {
                    return hours + " hours ago";
                }
            } else if (days <= 2) {
                if (days == 1) {
                    return "1 day ago";
                } else {
                    return days + " days ago";
                }
            } else {
                Date dt = format.parse(time);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                return dateFormat.format(dt);
            }
        } catch (Exception j) {
            j.printStackTrace();
        }
        return null;
    }

    public static Boolean getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null;
    }

    public static boolean isReadStoragePermissionGranted(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    public static void showPermissionDeniedDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getResources().getString(R.string.app_storage_permission_warning_msg))
                .setCancelable(false)
                .setNegativeButton("Cancel", ((dialog, which) -> dialog.dismiss()))
                .setPositiveButton("Settings", (dialog, id) -> {
                    dialog.dismiss();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", BuildConfig.APPLICATION_ID, null));
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    activity.startActivity(intent);
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static String getUriRealPath(Activity ctx, Uri uri) {
        String ret = "";

        if (ctx != null && uri != null) {

            if (isContentUri(uri)) {
                if (isGooglePhotoDoc(uri.getAuthority())) {
                    ret = uri.getLastPathSegment();
                } else {
                    ret = getImageRealPath(ctx.getContentResolver(), uri, null);
                }
            } else if (isFileUri(uri)) {
                ret = uri.getPath();
            } else if (isDocumentUri(ctx, uri)) {

                // Get uri related document id.
                String documentId = DocumentsContract.getDocumentId(uri);

                // Get uri authority.
                String uriAuthority = uri.getAuthority();

                if (isMediaDoc(uriAuthority)) {
                    String[] idArr = documentId.split(":");
                    if (idArr.length == 2) {
                        // First item is document type.
                        String docType = idArr[0];

                        // Second item is document real id.
                        String realDocId = idArr[1];

                        // Get content uri by document type.
                        Uri mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        if ("image".equals(docType)) {
                            mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        } else if ("video".equals(docType)) {
                            mediaContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        } else if ("audio".equals(docType)) {
                            mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }

                        // Get where clause with real document id.
                        String whereClause = MediaStore.Images.Media._ID + " = " + realDocId;

                        ret = getImageRealPath(ctx.getContentResolver(), mediaContentUri, whereClause);
                    }

                } else if (isDownloadDoc(uriAuthority)) {
                    // Build download uri.
                    Uri downloadUri = Uri.parse("content://downloads/public_downloads");

                    // Append download document id at uri end.
                    Uri downloadUriAppendId = ContentUris.withAppendedId(downloadUri, Long.valueOf(documentId));

                    ret = getImageRealPath(ctx.getContentResolver(), downloadUriAppendId, null);

                } else if (isExternalStoreDoc(uriAuthority)) {
                    String[] idArr = documentId.split(":");
                    if (idArr.length == 2) {
                        String type = idArr[0];
                        String realDocId = idArr[1];

                        if ("primary".equalsIgnoreCase(type)) {
                            ret = Environment.getExternalStorageDirectory() + "/" + realDocId;
                        }
                    }
                }
            }
        }
        return ret;
    }

    /* Check whether this uri represent a document or not. */
    private static boolean isDocumentUri(Context ctx, Uri uri) {
        boolean ret = false;
        if (ctx != null && uri != null) {
            ret = DocumentsContract.isDocumentUri(ctx, uri);
        }
        return ret;
    }

    /** Check whether this uri is a content uri or not.
     *  content uri like content://media/external/images/media/1302716
     **/
    private static boolean isContentUri(Uri uri) {
        boolean ret = false;
        if (uri != null) {
            String uriSchema = uri.getScheme();
            if ("content".equalsIgnoreCase(uriSchema)) {
                ret = true;
            }
        }
        return ret;
    }

    /** Check whether this uri is a file uri or not.
     *  file uri like file:///storage/41B7-12F1/DCIM/Camera/IMG_20180211_095139.jpg
     **/
    private static boolean isFileUri(Uri uri) {
        boolean ret = false;
        if (uri != null) {
            String uriSchema = uri.getScheme();
            if ("file".equalsIgnoreCase(uriSchema)) {
                ret = true;
            }
        }
        return ret;
    }


    /** Check whether this document is provided by ExternalStorageProvider. */
    private static boolean isExternalStoreDoc(String uriAuthority) {
        boolean ret = false;

        if ("com.android.externalstorage.documents".equals(uriAuthority)) {
            ret = true;
        }

        return ret;
    }

    /** Check whether this document is provided by DownloadsProvider. */
    private static boolean isDownloadDoc(String uriAuthority) {
        boolean ret = false;

        if ("com.android.providers.downloads.documents".equals(uriAuthority)) {
            ret = true;
        }

        return ret;
    }

    /** Check whether this document is provided by MediaProvider. */
    private static boolean isMediaDoc(String uriAuthority) {
        boolean ret = false;

        if ("com.android.providers.media.documents".equals(uriAuthority)) {
            ret = true;
        }

        return ret;
    }

    /** Check whether this document is provided by google photos. */
    private static boolean isGooglePhotoDoc(String uriAuthority) {
        boolean ret = false;

        if ("com.google.android.apps.photos.content".equals(uriAuthority)) {
            ret = true;
        }

        return ret;
    }

    /** Return uri represented document file real local path.*/
    private static String getImageRealPath(ContentResolver contentResolver, Uri uri, String whereClause) {
        String ret = "";
        // Query the uri with condition.
        Cursor cursor = contentResolver.query(uri, null, whereClause, null, null);
        if (cursor != null) {
            boolean moveToFirst = cursor.moveToFirst();
            if (moveToFirst) {
                // Get columns name by uri type.
                String columnName = MediaStore.Images.Media.DATA;
                if (uri == MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Images.Media.DATA;
                } else if (uri == MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Audio.Media.DATA;
                } else if (uri == MediaStore.Video.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Video.Media.DATA;
                }

                // Get column index.
                int imageColumnIndex = cursor.getColumnIndex(columnName);

                // Get column value which is the uri related file local path.
                ret = cursor.getString(imageColumnIndex);
            }
        }
        return ret;
    }
}
