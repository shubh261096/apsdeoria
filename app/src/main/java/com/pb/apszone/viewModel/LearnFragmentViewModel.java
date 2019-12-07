package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.pb.apszone.service.repo.Events;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.utils.KeyStorePref;

import static com.pb.apszone.utils.AppConstants.KEY_STUDENT_ID;

public class LearnFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<Events.LearnResponseEvent> learnResponseEventMutableLiveData;
    private KeyStorePref keyStorePref;
    private Repository repository;

    public LearnFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();
        keyStorePref = KeyStorePref.getInstance(application.getApplicationContext());
        learnResponseEventMutableLiveData = new MutableLiveData<>();
    }

    public void sendRequest() {
        repository.getLearnVideo(keyStorePref.getString(KEY_STUDENT_ID), learnResponseEventMutableLiveData);
    }

    public LiveData<Events.LearnResponseEvent> getLearnVideo() {
        return learnResponseEventMutableLiveData;
    }
}
