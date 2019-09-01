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

public class InboxFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<Events.InboxResponseEvent> inboxResponseModelMutableLiveData;
    private KeyStorePref keyStorePref;
    private Repository repository;

    public InboxFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();
        inboxResponseModelMutableLiveData = new MutableLiveData<>();
        keyStorePref = KeyStorePref.getInstance(application.getApplicationContext());
    }

    public void sendRequest(){
        repository.getInbox(keyStorePref.getString(KEY_USER_TYPE), inboxResponseModelMutableLiveData);
    }

    public LiveData<Events.InboxResponseEvent> getInbox() {
        return inboxResponseModelMutableLiveData;
    }

}
