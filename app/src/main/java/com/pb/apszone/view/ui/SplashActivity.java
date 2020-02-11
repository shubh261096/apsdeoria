package com.pb.apszone.view.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.pb.apszone.R;
import com.pb.apszone.utils.KeyStorePref;

import static com.pb.apszone.utils.AppConstants.KEY_USER_LOGIN_STATUS;

public class SplashActivity extends AppCompatActivity {

    private KeyStorePref keyStorePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        FirebaseMessaging.getInstance().subscribeToTopic("global"); // This is done to subscribe all app user for topic

        keyStorePref = KeyStorePref.getInstance(this);

        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(() -> {
            if (keyStorePref.getBoolean(KEY_USER_LOGIN_STATUS)) {
                startActivity(new Intent(this, DashboardActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        }, SPLASH_TIME_OUT);
    }
}
