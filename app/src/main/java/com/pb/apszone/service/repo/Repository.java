package com.pb.apszone.service.repo;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pb.apszone.service.model.LoginResponseModel;
import com.pb.apszone.service.rest.ApiClient;
import com.pb.apszone.service.rest.ApiInterface;
import com.pb.apszone.service.rest.LoginRequestModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private static ApiInterface apiService;

    private static class SingletonHelper {
        private static final Repository INSTANCE = new Repository();
    }

    public static Repository getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private Repository() {
        apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    public MutableLiveData<LoginResponseModel> checkLogin(LoginRequestModel loginRequestModel) {
        final MutableLiveData<LoginResponseModel> data = new MutableLiveData<>();
        Map<String, String> params = new HashMap<>();
        params.put("email", loginRequestModel.getEmail());
        params.put("password", loginRequestModel.getPassword());
        apiService.checkLogin(params)
                .enqueue(new Callback<LoginResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponseModel> call, @Nullable Response<LoginResponseModel> response) {
                        if (response != null && response.isSuccessful()) {
                            data.postValue(response.body());
                            Log.i("Response ", response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponseModel> call, Throwable t) {
                        data.postValue(null);
                    }
                });
        return data;
    }
}
