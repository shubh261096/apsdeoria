package com.pb.apszone.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pb.apszone.service.repo.Events;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.model.SyllabusRequestModel;

public class SyllabusFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<Events.SyllabusResponseEvent> syllabusResponseModelMutableLiveData;
    private Repository repository;
    private SyllabusRequestModel syllabusRequestModel;

    public SyllabusFragmentViewModel(@NonNull Application application) {
        super(application);
        syllabusResponseModelMutableLiveData = new MutableLiveData<>();
        repository = Repository.getInstance();
        syllabusRequestModel = new SyllabusRequestModel();
    }

    public void sendRequest(String student_id) {
        syllabusRequestModel.setStudentID(student_id);
        repository.getSyllabus(syllabusRequestModel, syllabusResponseModelMutableLiveData);
    }

    public LiveData<Events.SyllabusResponseEvent> getSyllabus() {
        return syllabusResponseModelMutableLiveData;
    }

}
