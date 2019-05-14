package com.pb.apszone.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.pb.apszone.service.model.ProfileResponseModel;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.ProfileRequestModel;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<ProfileResponseModel> profileResponseModelMutableLiveData;
    private Repository repository;
    private ProfileRequestModel profileRequestModel;

    public ProfileViewModel(){
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
