package com.pb.apszone.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.pb.apszone.service.model.LoginResponseModel;
import com.pb.apszone.service.rest.LoginRequestModel;
import com.pb.apszone.service.repo.Repository;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<LoginResponseModel> loginResponseModelMutableLiveData;
    private Repository repository;
    private LoginRequestModel loginRequestModel;

    public LoginViewModel() {
        loginResponseModelMutableLiveData = new MutableLiveData<>();
        loginRequestModel = new LoginRequestModel();
        repository = Repository.getInstance();
    }

    public void sendRequest(String email, String password) {
        loginRequestModel.setPassword(password);
        loginRequestModel.setEmail(email);
    }

    public LiveData<LoginResponseModel> getUser() {
        loginResponseModelMutableLiveData = repository.checkLogin(loginRequestModel);
        return loginResponseModelMutableLiveData;
    }
}
