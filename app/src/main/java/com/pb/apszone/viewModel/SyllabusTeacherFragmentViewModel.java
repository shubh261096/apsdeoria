package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.pb.apszone.service.model.CommonResponseModel;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.SyllabusRequestModel;

public class SyllabusTeacherFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<CommonResponseModel> commonResponseModelMutableLiveData;
    private Repository repository;
    private SyllabusRequestModel syllabusRequestModel;

    public SyllabusTeacherFragmentViewModel(@NonNull Application application) {
        super(application);
        commonResponseModelMutableLiveData = new MutableLiveData<>();
        repository = Repository.getInstance();
        syllabusRequestModel = new SyllabusRequestModel();
    }

    public void sendRequest(String subject_id) {
        syllabusRequestModel.setSubjectId(subject_id);
        repository.checkSyllabus(syllabusRequestModel, commonResponseModelMutableLiveData);
    }

    public LiveData<CommonResponseModel> getSubmitResponse() {
        return commonResponseModelMutableLiveData;
    }
}
