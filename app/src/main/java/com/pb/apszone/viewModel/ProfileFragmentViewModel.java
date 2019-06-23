package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.pb.apszone.service.model.ProfileResponseModel;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.ProfileRequestModel;

public class ProfileFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<ProfileResponseModel> profileResponseModelMutableLiveData;
    private Repository repository;
    private ProfileRequestModel profileRequestModel;

    public ProfileFragmentViewModel(@NonNull Application application) {
        super(application);
        profileResponseModelMutableLiveData = new MutableLiveData<>();
        repository = Repository.getInstance();
        profileRequestModel = new ProfileRequestModel();
    }

    public void sendRequest(String id, String type) {
        profileRequestModel.setId(id);
        profileRequestModel.setType(type);
    }

    public LiveData<ProfileResponseModel> getProfile() {
        profileResponseModelMutableLiveData = repository.getProfile(profileRequestModel);
        return profileResponseModelMutableLiveData;
    }

}