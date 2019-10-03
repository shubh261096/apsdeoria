package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.pb.apszone.service.repo.Events;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.FeedbackRequestModel;
import com.pb.apszone.service.rest.TimetableRequestModel;
import com.pb.apszone.utils.KeyStorePref;

import static com.pb.apszone.utils.AppConstants.KEY_STUDENT_ID;

public class FeedbackFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<Events.CommonResponseEvent> commonResponseEventMutableLiveData;
    private MutableLiveData<Events.TimetableResponseEvent> timetableResponseModelMutableLiveData;
    private KeyStorePref keyStorePref;
    private Repository repository;
    private TimetableRequestModel timetableRequestModel;

    public FeedbackFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();
        keyStorePref = KeyStorePref.getInstance(application.getApplicationContext());
        commonResponseEventMutableLiveData = new MutableLiveData<>();
        timetableResponseModelMutableLiveData = new MutableLiveData<>();
        timetableRequestModel = new TimetableRequestModel();
    }

    public void sendRequest(){
        repository.checkFeedback(keyStorePref.getString(KEY_STUDENT_ID), commonResponseEventMutableLiveData);
    }

    public LiveData<Events.CommonResponseEvent> checkFeedback() {
        return commonResponseEventMutableLiveData;
    }

    public void sendTimetableRequest(String class_id, String filter, String user_type) {
        timetableRequestModel.setClassId(class_id);
        repository.getTimetable(timetableRequestModel, filter, user_type, timetableResponseModelMutableLiveData);
    }

    public LiveData<Events.TimetableResponseEvent> getTimetable() {
        return timetableResponseModelMutableLiveData;
    }

    public void addFeedbackRequest(FeedbackRequestModel feedbackRequestModel) {
        repository.addFeedback(feedbackRequestModel, commonResponseEventMutableLiveData);
    }

}
