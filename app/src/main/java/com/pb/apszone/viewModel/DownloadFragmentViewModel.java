package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.pb.apszone.service.repo.Events;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.utils.KeyStorePref;

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
        repository.getDownloads(keyStorePref.getString(KEY_USER_TYPE), downloadResponseEventMutableLiveData);
    }

    public LiveData<Events.DownloadResponseEvent> getDownloads() {
        return downloadResponseEventMutableLiveData;
    }

}
