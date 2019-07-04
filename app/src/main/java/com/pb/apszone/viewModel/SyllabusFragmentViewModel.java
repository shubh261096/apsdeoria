package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.pb.apszone.service.model.SyllabusResponseModel;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.SyllabusRequestModel;

public class SyllabusFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<SyllabusResponseModel> syllabusResponseModelMutableLiveData;
    private Repository repository;
    private SyllabusRequestModel syllabusRequestModel;

    public SyllabusFragmentViewModel(@NonNull Application application) {
        super(application);
        syllabusResponseModelMutableLiveData = new MutableLiveData<>();
        repository = Repository.getInstance();
        syllabusRequestModel = new SyllabusRequestModel();
    }

    public void sendRequest(String class_id) {
        syllabusRequestModel.setClassId(class_id);
        repository.getSyllabus(syllabusRequestModel, syllabusResponseModelMutableLiveData);
    }

    public LiveData<SyllabusResponseModel> getSyllabus() {
        return syllabusResponseModelMutableLiveData;
    }

}
