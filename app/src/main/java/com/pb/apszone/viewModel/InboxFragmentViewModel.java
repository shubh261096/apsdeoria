package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.pb.apszone.service.model.InboxResponseModel;
import com.pb.apszone.service.repo.Repository;

public class InboxFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<InboxResponseModel> inboxResponseModelMutableLiveData;
    private Repository repository;

    public InboxFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();
        inboxResponseModelMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<InboxResponseModel> getInbox() {
        inboxResponseModelMutableLiveData = repository.getInbox();
        return inboxResponseModelMutableLiveData;
    }

}
