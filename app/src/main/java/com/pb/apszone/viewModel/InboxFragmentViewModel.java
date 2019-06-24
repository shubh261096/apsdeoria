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

    public InboxFragmentViewModel(@NonNull Application application) {
        super(application);
        Repository repository = Repository.getInstance();
        inboxResponseModelMutableLiveData = repository.getInbox();
    }

    public LiveData<InboxResponseModel> getInbox() {
        return inboxResponseModelMutableLiveData;
    }

}
