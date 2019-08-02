package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.pb.apszone.service.model.CommonResponseModel;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.LoginRequestModel;

public class ResetPasswordFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<CommonResponseModel> validateCommonResponseModelMutableLiveData;
    private MutableLiveData<CommonResponseModel> resetCommonResponseModelMutableLiveData;
    private Repository repository;
    private LoginRequestModel loginRequestModel;

    public ResetPasswordFragmentViewModel(@NonNull Application application) {
        super(application);
        validateCommonResponseModelMutableLiveData = new MutableLiveData<>();
        resetCommonResponseModelMutableLiveData = new MutableLiveData<>();
        repository = Repository.getInstance();
        loginRequestModel = new LoginRequestModel();
    }

    public void validateResetPasswordRequest(String id, String dob) {
        loginRequestModel.setId(id);
        loginRequestModel.setDob(dob);
        repository.validateResetPassword(loginRequestModel, validateCommonResponseModelMutableLiveData);
    }

    public void resetPasswordRequest(String id, String password) {
        loginRequestModel.setId(id);
        loginRequestModel.setPassword(password);
        repository.resetPassword(loginRequestModel, resetCommonResponseModelMutableLiveData);
    }

    public LiveData<CommonResponseModel> validateResetPasswordResponse() {
        return validateCommonResponseModelMutableLiveData;
    }

    public LiveData<CommonResponseModel> resetPassword() {
        return resetCommonResponseModelMutableLiveData;
    }

}
