package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.pb.apszone.service.repo.Events;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.HomeworkRequestModel;

public class HomeworkTeacherFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<Events.ClassSubjectResponseEvent> classSubjectResponseModelMutableLiveData;
    private MutableLiveData<Events.CommonResponseEvent> commonResponseModelMutableLiveData;
    private Repository repository;
    private HomeworkRequestModel homeworkRequestModel;

    public HomeworkTeacherFragmentViewModel(@NonNull Application application) {
        super(application);
        classSubjectResponseModelMutableLiveData = new MutableLiveData<>();
        commonResponseModelMutableLiveData = new MutableLiveData<>();
        repository = Repository.getInstance();
        homeworkRequestModel = new HomeworkRequestModel();
    }

    public void sendRequest(String teacher_id) {
        homeworkRequestModel.setTeacherId(teacher_id);
        repository.getClassSubjectDetail(homeworkRequestModel, classSubjectResponseModelMutableLiveData);
    }

    public void addHomeworkRequest(HomeworkRequestModel homeworkRequestModel) {
        repository.addHomework(homeworkRequestModel, commonResponseModelMutableLiveData);
    }

    public LiveData<Events.CommonResponseEvent> getSubmitResponse() {
        return commonResponseModelMutableLiveData;
    }

    public LiveData<Events.ClassSubjectResponseEvent> getClassSubjectDetail() {
        return classSubjectResponseModelMutableLiveData;
    }

}
