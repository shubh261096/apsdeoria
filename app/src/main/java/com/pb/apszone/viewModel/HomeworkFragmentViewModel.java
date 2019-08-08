package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.pb.apszone.service.repo.Events;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.HomeworkRequestModel;

public class HomeworkFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<Events.HomeworkResponseEvent> homeworkResponseModelMutableLiveData;
    private Repository repository;
    private HomeworkRequestModel homeworkRequestModel;

    public HomeworkFragmentViewModel(@NonNull Application application) {
        super(application);
        homeworkResponseModelMutableLiveData = new MutableLiveData<>();
        repository = Repository.getInstance();
        homeworkRequestModel = new HomeworkRequestModel();
    }

    public void sendRequest(String class_id, String date) {
        homeworkRequestModel.setClassId(class_id);
        homeworkRequestModel.setDate(date);
        repository.getHomework(homeworkRequestModel, homeworkResponseModelMutableLiveData);
    }

    public LiveData<Events.HomeworkResponseEvent> getHomework() {
        return homeworkResponseModelMutableLiveData;
    }

}
