package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.pb.apszone.service.model.TimetableResponseModel;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.TimetableRequestModel;

public class StudentTimetableFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<TimetableResponseModel> timetableResponseModelMutableLiveData;
    private Repository repository;
    private TimetableRequestModel timetableRequestModel;

    public StudentTimetableFragmentViewModel(@NonNull Application application) {
        super(application);
        timetableResponseModelMutableLiveData = new MutableLiveData<>();
        repository = Repository.getInstance();
        timetableRequestModel = new TimetableRequestModel();
    }

    public void sendRequest(String class_id, String day) {
        timetableRequestModel.setClassId(class_id);
        timetableRequestModel.setToday(day);
    }

    public LiveData<TimetableResponseModel> getTimetable(String filter) {
        timetableResponseModelMutableLiveData = repository.getTimetable(timetableRequestModel, filter);
        return timetableResponseModelMutableLiveData;
    }

}
