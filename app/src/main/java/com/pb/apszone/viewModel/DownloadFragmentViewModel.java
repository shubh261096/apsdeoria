package com.pb.apszone.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pb.apszone.service.repo.Events;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.utils.KeyStorePref;

import static com.pb.apszone.utils.AppConstants.KEY_USER_ID;
import static com.pb.apszone.utils.AppConstants.KEY_USER_TYPE;

public class DownloadFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<Events.DownloadResponseEvent> downloadResponseEventMutableLiveData;
    private KeyStorePref keyStorePref;
    private Repository repository;

    public DownloadFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();
        downloadResponseEventMutableLiveData = new MutableLiveData<>();
        keyStorePref = KeyStorePref.getInstance(application.getApplicationContext());
    }

    public void sendRequest(){
        repository.getDownloads(keyStorePref.getString(KEY_USER_ID), downloadResponseEventMutableLiveData);
    }

    public LiveData<Events.DownloadResponseEvent> getDownloads() {
        return downloadResponseEventMutableLiveData;
    }

}
