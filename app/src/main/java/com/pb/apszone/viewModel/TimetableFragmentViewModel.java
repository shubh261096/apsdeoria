package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.pb.apszone.service.model.TimetableResponseModel;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.TimetableRequestModel;

public class TimetableFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<TimetableResponseModel> timetableResponseModelMutableLiveData;
    private Repository repository;
    private TimetableRequestModel timetableRequestModel;

    public TimetableFragmentViewModel(@NonNull Application application) {
        super(application);
        timetableResponseModelMutableLiveData = new MutableLiveData<>();
        repository = Repository.getInstance();
        timetableRequestModel = new TimetableRequestModel();
    }

    public void sendRequest(String class_id, String day, String filter, String user_type) {
        timetableRequestModel.setClassId(class_id);
        timetableRequestModel.setToday(day);
        repository.getTimetable(timetableRequestModel, filter, user_type, timetableResponseModelMutableLiveData);
    }

    public void sendTeacherRequest(String teacher_id, String day, String filter, String user_type) {
        timetableRequestModel.setTeacherId(teacher_id);
        timetableRequestModel.setToday(day);
        repository.getTimetable(timetableRequestModel, filter, user_type, timetableResponseModelMutableLiveData);
    }

    public LiveData<TimetableResponseModel> getTimetable() {
        return timetableResponseModelMutableLiveData;
    }

}
