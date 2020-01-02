package com.pb.apszone.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pb.apszone.service.repo.Events;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.TimetableRequestModel;

public class TimetableFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<Events.TimetableResponseEvent> timetableResponseModelMutableLiveData;
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

    public LiveData<Events.TimetableResponseEvent> getTimetable() {
        return timetableResponseModelMutableLiveData;
    }

}
