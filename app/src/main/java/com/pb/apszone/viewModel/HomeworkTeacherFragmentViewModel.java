package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.pb.apszone.service.model.ClassSubjectResponseModel;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.HomeworkRequestModel;

public class HomeworkTeacherFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<ClassSubjectResponseModel> classSubjectResponseModelMutableLiveData;
    private Repository repository;
    private HomeworkRequestModel homeworkRequestModel;

    public HomeworkTeacherFragmentViewModel(@NonNull Application application) {
        super(application);
        classSubjectResponseModelMutableLiveData = new MutableLiveData<>();
        repository = Repository.getInstance();
        homeworkRequestModel = new HomeworkRequestModel();
    }

    public void sendRequest(String teacher_id) {
        homeworkRequestModel.setTeacherId(teacher_id);
        repository.getClassSubjectDetail(homeworkRequestModel, classSubjectResponseModelMutableLiveData);
    }

    public LiveData<ClassSubjectResponseModel> getClassSubjectDetail() {
        return classSubjectResponseModelMutableLiveData;
    }

}
