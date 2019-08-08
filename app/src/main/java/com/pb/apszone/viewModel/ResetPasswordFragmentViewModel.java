package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.pb.apszone.service.repo.Events;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.LoginRequestModel;

public class ResetPasswordFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<Events.CommonResponseEvent> validateCommonResponseModelMutableLiveData;
    private MutableLiveData<Events.CommonResponseEvent> resetCommonResponseModelMutableLiveData;
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

    public LiveData<Events.CommonResponseEvent> validateResetPasswordResponse() {
        return validateCommonResponseModelMutableLiveData;
    }

    public LiveData<Events.CommonResponseEvent> resetPassword() {
        return resetCommonResponseModelMutableLiveData;
    }

}
