package com.pb.apszone.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.pb.apszone.R;
import com.pb.apszone.viewModel.ProfileViewModel;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        profileViewModel.sendRequest("P101", "parent");
        subscribe();
    }

    private void subscribe() {
        profileViewModel.getProfile().observe(this, profileResponseModel -> {
            if (profileResponseModel != null) {
                Toast.makeText(this, profileResponseModel.getProfile().getFullname(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
