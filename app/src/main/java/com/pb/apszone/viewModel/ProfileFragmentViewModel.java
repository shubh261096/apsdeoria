package com.pb.apszone.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pb.apszone.service.repo.Events;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.model.ProfileRequestModel;

public class ProfileFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<Events.ProfileResponseEvent> profileResponseModelMutableLiveData;
    private Repository repository;
    private ProfileRequestModel profileRequestModel;

    public ProfileFragmentViewModel(@NonNull Application application) {
        super(application);
        profileResponseModelMutableLiveData = new MutableLiveData<>();
        repository = Repository.getInstance();
        profileRequestModel = new ProfileRequestModel();
    }

    public void sendRequest(String id) {
        profileRequestModel.setId(id);
        repository.getProfile(profileRequestModel, profileResponseModelMutableLiveData);
    }

    public LiveData<Events.ProfileResponseEvent> getProfile() {
        return profileResponseModelMutableLiveData;
    }

}
