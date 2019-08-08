package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.pb.apszone.service.repo.Events;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.FeesRequestModel;

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

    public void sendRequest(String class_id, String year, String student_id) {
        feesRequestModel.setClassId(class_id);
        feesRequestModel.setYear(year);
        feesRequestModel.setStudentId(student_id);
        repository.getFees(feesRequestModel, feesResponseModelMutableLiveData);
    }

    public LiveData<Events.FeesResponseEvent> getFees() {
        return feesResponseModelMutableLiveData;
    }

}
