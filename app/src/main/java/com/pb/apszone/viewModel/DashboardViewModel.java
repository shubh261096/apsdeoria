package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.pb.apszone.service.model.DashboardUIResponseModel;
import com.pb.apszone.service.repo.Repository;

public class DashboardViewModel extends AndroidViewModel {

    private MutableLiveData<DashboardUIResponseModel> dashboardUIResponseModelMutableLiveData;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        Repository repository = Repository.getInstance();
        dashboardUIResponseModelMutableLiveData = repository.getDashboardUIElements();
    }

    public LiveData<DashboardUIResponseModel> getDashboardUIElements() {
        return dashboardUIResponseModelMutableLiveData;
    }

}
