package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.pb.apszone.R;
import com.pb.apszone.service.model.LoginResponseModel;
import com.pb.apszone.service.rest.LoginRequestModel;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.utils.KeyStorePref;
import com.pb.apszone.utils.SnackbarMessage;

import static com.pb.apszone.utils.AppConstants.KEY_USER_EMAIL;
import static com.pb.apszone.utils.AppConstants.KEY_USER_ID;
import static com.pb.apszone.utils.AppConstants.KEY_USER_LOGIN_STATUS;
import static com.pb.apszone.utils.AppConstants.KEY_USER_TYPE;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<LoginResponseModel> loginResponseModelMutableLiveData;
    private Repository repository;
    private LoginRequestModel loginRequestModel;
    private SnackbarMessage snackbarMessage;
    private KeyStorePref keyStorePref;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginResponseModelMutableLiveData = new MutableLiveData<>();
        loginRequestModel = new LoginRequestModel();
        repository = Repository.getInstance();
        snackbarMessage = new SnackbarMessage();
        keyStorePref = KeyStorePref.getInstance(application.getApplicationContext());
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

    public void putSharedPrefData(LoginResponseModel loginResponseModel) {
        keyStorePref.putBoolean(KEY_USER_LOGIN_STATUS, true);
        if (loginResponseModel.getUser().getId() != null) {
            keyStorePref.putString(KEY_USER_ID, loginResponseModel.getUser().getId());
        }
        if (loginResponseModel.getUser().getEmail() != null) {
            keyStorePref.putString(KEY_USER_EMAIL, loginResponseModel.getUser().getEmail());
        }
        if (loginResponseModel.getUser().getType() != null) {
            keyStorePref.putString(KEY_USER_TYPE, loginResponseModel.getUser().getType());
        }
    }
}
