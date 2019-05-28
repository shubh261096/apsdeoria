package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.pb.apszone.service.model.DashboardUIResponseModel;
import com.pb.apszone.service.model.ProfileResponseModel;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.utils.KeyStorePref;

import static com.pb.apszone.utils.AppConstants.KEY_STUDENT_CLASS_ID;
import static com.pb.apszone.utils.AppConstants.KEY_STUDENT_ID;
import static com.pb.apszone.utils.AppConstants.KEY_USER_TYPE;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_PARENT;

public class DashboardViewModel extends AndroidViewModel {

    private MutableLiveData<DashboardUIResponseModel> dashboardUIResponseModelMutableLiveData;
    private KeyStorePref keyStorePref;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        Repository repository = Repository.getInstance();
        keyStorePref = KeyStorePref.getInstance(application.getApplicationContext());
        dashboardUIResponseModelMutableLiveData = repository.getDashboardUIElements();
    }

    public LiveData<DashboardUIResponseModel> getDashboardUIElements() {
        return dashboardUIResponseModelMutableLiveData;
    }

    public void putSharedPrefData(ProfileResponseModel profileResponseModel) {
        if (TextUtils.equals(keyStorePref.getString(KEY_USER_TYPE), USER_TYPE_PARENT)) {
            if (!TextUtils.isEmpty(profileResponseModel.getProfile().getClassId().getId())) {
                keyStorePref.putString(KEY_STUDENT_CLASS_ID, profileResponseModel.getProfile().getClassId().getId());
            }
            if (!TextUtils.isEmpty(profileResponseModel.getProfile().getId())) {
                keyStorePref.putString(KEY_STUDENT_ID, profileResponseModel.getProfile().getId());
            }
        }
    }

}
