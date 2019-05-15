package com.pb.apszone.view.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.pb.apszone.R;
import com.pb.apszone.utils.KeyStorePref;

import static com.pb.apszone.utils.AppConstants.KEY_USER_LOGIN_STATUS;

public class SplashActivity extends AppCompatActivity {

    KeyStorePref keyStorePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

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
