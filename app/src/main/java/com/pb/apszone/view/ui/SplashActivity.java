package com.pb.apszone.view.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.pb.apszone.R;
import com.pb.apszone.utils.AppConstants;
import com.pb.apszone.utils.InAppUpdateManager;
import com.pb.apszone.utils.KeyStorePref;

import java.util.Calendar;

import static com.pb.apszone.utils.AppConstants.KEY_APP_UPDATE_LAST_SHOWN_DAY;
import static com.pb.apszone.utils.AppConstants.KEY_USER_LOGIN_STATUS;
import static com.pb.apszone.utils.AppConstants.REQ_CODE_VERSION_UPDATE_FLEXIBLE;
import static com.pb.apszone.utils.AppConstants.REQ_CODE_VERSION_UPDATE_IMMEDIATE;

public class SplashActivity extends AppCompatActivity implements InAppUpdateManager.InAppUpdateHandler {
    private KeyStorePref keyStorePref;
    private static final String TAG = "SplashActivity";
    private InAppUpdateManager inAppUpdateManager;
    private AppConstants.UpdateMode updateMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        keyStorePref = KeyStorePref.getInstance(this);
        initAppUpdate();
    }


    private void initAppUpdate() {
        Log.d(TAG, "initAppUpdate: ");
        inAppUpdateManager = InAppUpdateManager.Builder(this)
                .handler(this);
        inAppUpdateManager.checkForAppUpdate();
    }


    public void moveToDashboardActivity() {
        Log.d(TAG, "moveToDashboardActivity: ");
        FirebaseMessaging.getInstance().subscribeToTopic("global"); // This is done to subscribe all app user for topic

        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(() -> {
            if (keyStorePref.getBoolean(KEY_USER_LOGIN_STATUS)) {
                startActivity(new Intent(this, DashboardActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_CODE_VERSION_UPDATE_IMMEDIATE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                // If the update is cancelled by the user,
                // you can request to start the update again.
                Toast.makeText(this, "Immediate Update cancelled by user", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Update flow failed! Result code: " + resultCode);
                finishAndRemoveTask();
            }
        } else if (requestCode == REQ_CODE_VERSION_UPDATE_FLEXIBLE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                // If the update is cancelled by the user,
                // you can request to start the update again.
                Toast.makeText(this, "Flexible Update cancelled by user", Toast.LENGTH_SHORT).show();
                keyStorePref.putLong(KEY_APP_UPDATE_LAST_SHOWN_DAY, Calendar.getInstance().getTimeInMillis());
                moveToDashboardActivity();
                Log.d(TAG, "Update flow failed! Result code: " + resultCode);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onInAppUpdateError(int code, Throwable error) {
        /*
         * Called when some error occurred. See Constants class for more details
         */
        Log.d(TAG, "code: " + code, error);
        Toast.makeText(this, "Update Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInAppUpdateStatus(AppConstants.InstallStat status) {
        Log.d(TAG, "onInAppUpdateStatus: ");
        if (status == AppConstants.InstallStat.DOWNLOADED) {
            Toast.makeText(this, "Downloaded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onInAppUpdateAvailable(boolean isAvailable) {
        Log.d(TAG, "onInAppUpdateAvailable:");
        if (!isAvailable) {
            moveToDashboardActivity();
        }
    }

    @Override
    public void onDialogShown(AppConstants.UpdateMode updateMode) {
        Log.d(TAG, "onDialogShown: ");
        this.updateMode = updateMode;
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume:");
        if (inAppUpdateManager != null) inAppUpdateManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        if (inAppUpdateManager != null) inAppUpdateManager.onDestroy();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (updateMode == AppConstants.UpdateMode.IMMEDIATE) {
            finishAndRemoveTask();
        } else {
            keyStorePref.putLong(KEY_APP_UPDATE_LAST_SHOWN_DAY, Calendar.getInstance().getTimeInMillis());
            moveToDashboardActivity();
        }
    }
}
