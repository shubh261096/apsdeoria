package com.pb.apszone.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.viewModel.LoginViewModel;


public class LoginActivity extends AppCompatActivity {

    LoginViewModel loginViewModel;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        btnLogin.setOnClickListener(v -> {
            loginViewModel.sendRequest("ansh@gmail.com", "ansh123");
            subscribe();
        });
    }

    public void subscribe() {
        loginViewModel.getUser().observe(LoginActivity.this, loginResponseModel -> {
            if (loginResponseModel != null) {
                Toast.makeText(LoginActivity.this, loginResponseModel.getUser().getType(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}