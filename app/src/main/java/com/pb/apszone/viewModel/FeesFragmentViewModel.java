package com.pb.apszone.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pb.apszone.service.repo.Events;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.model.FeesRequestModel;

public class FeesFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<Events.FeesResponseEvent> feesResponseModelMutableLiveData;
    private Repository repository;
    private FeesRequestModel feesRequestModel;

    public FeesFragmentViewModel(@NonNull Application application) {
        super(application);
        feesResponseModelMutableLiveData = new MutableLiveData<>();
        repository = Repository.getInstance();
        feesRequestModel = new FeesRequestModel();
    }

    public void sendRequest(String year, String student_id) {
        feesRequestModel.setYear(year);
        feesRequestModel.setStudentId(student_id);
        repository.getFees(feesRequestModel, feesResponseModelMutableLiveData);
    }

    public LiveData<Events.FeesResponseEvent> getFees() {
        return feesResponseModelMutableLiveData;
    }

}
