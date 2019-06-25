package com.pb.apszone.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<Boolean> networkChange = new MutableLiveData<>();

    public void setStatus(Boolean status) {
        networkChange.postValue(status);
    }

    public LiveData<Boolean> getStatus() {
        return networkChange;
    }
}
