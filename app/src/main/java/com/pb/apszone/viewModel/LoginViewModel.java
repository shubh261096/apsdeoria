package com.pb.apszone.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import com.pb.apszone.R;
import com.pb.apszone.service.model.LoginResponseModel;
import com.pb.apszone.service.rest.LoginRequestModel;
import com.pb.apszone.service.repo.Repository;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<LoginResponseModel> loginResponseModelMutableLiveData;
    private Repository repository;
    private LoginRequestModel loginRequestModel;
    private MutableLiveData<Boolean> status;
    private SnackbarMessage snackbarMessage;

    public LoginViewModel() {
        loginResponseModelMutableLiveData = new MutableLiveData<>();
        loginRequestModel = new LoginRequestModel();
        repository = Repository.getInstance();
        status = new MutableLiveData<>();
        snackbarMessage = new SnackbarMessage();

    }

    public void sendRequest(String email, String password) {
        loginRequestModel.setPassword(password);
        loginRequestModel.setEmail(email);
    }

    public LiveData<LoginResponseModel> getUser() {
        loginResponseModelMutableLiveData = repository.checkLogin(loginRequestModel);
        return loginResponseModelMutableLiveData;
    }

    public boolean validateLogin(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            snackbarMessage.setValue(R.string.error_email_required);
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            snackbarMessage.setValue(R.string.error_password_required);
            return false;
        }
        return true;
    }

    public SnackbarMessage getSnackbarMessage() {
        return snackbarMessage;
    }
}
