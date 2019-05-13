package com.pb.apszone.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.service.model.LoginResponseModel;
import com.pb.apszone.viewModel.LoginViewModel;


public class LoginActivity extends AppCompatActivity {

    LoginViewModel loginViewModel;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = findViewById(R.id.btnLogin);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.init();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.sendRequest("ansh@gmail.com", "ansh123");
                loginViewModel.getUser().observe(LoginActivity.this, new Observer<LoginResponseModel>() {
                    @Override
                    public void onChanged(@Nullable LoginResponseModel model) {
                        if (model != null) {
                            Toast.makeText(LoginActivity.this, model.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

}