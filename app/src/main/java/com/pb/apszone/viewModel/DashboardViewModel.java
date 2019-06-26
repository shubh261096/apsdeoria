package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.pb.apszone.service.model.DashboardItem;
import com.pb.apszone.service.model.DashboardUIResponseModel;
import com.pb.apszone.service.model.ProfileResponseModel;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.utils.KeyStorePref;

import java.util.ArrayList;
import java.util.List;

import static com.pb.apszone.utils.AppConstants.KEY_ENABLED;
import static com.pb.apszone.utils.AppConstants.KEY_STUDENT_CLASS_ID;
import static com.pb.apszone.utils.AppConstants.KEY_STUDENT_ID;
import static com.pb.apszone.utils.AppConstants.KEY_TEACHER_ID;
import static com.pb.apszone.utils.AppConstants.KEY_USER_TYPE;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_PARENT;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_TEACHER;

public class DashboardViewModel extends AndroidViewModel {

    private MutableLiveData<DashboardUIResponseModel> dashboardUIResponseModelMutableLiveData;
    private KeyStorePref keyStorePref;
    private Repository repository;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        dashboardUIResponseModelMutableLiveData = new MutableLiveData<>();
        repository = Repository.getInstance();
        keyStorePref = KeyStorePref.getInstance(application.getApplicationContext());
    }

    public LiveData<DashboardUIResponseModel> getDashboardUIElements() {
        dashboardUIResponseModelMutableLiveData = repository.getDashboardUIElements();
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
        } else if (TextUtils.equals(keyStorePref.getString(KEY_USER_TYPE), USER_TYPE_TEACHER)) {
            if (!TextUtils.isEmpty(profileResponseModel.getProfile().getId())) {
                keyStorePref.putString(KEY_TEACHER_ID, profileResponseModel.getProfile().getId());
            }
        }
    }

    public List<DashboardItem> addListData(List<DashboardItem> dashboardItems, String user_type) {
        List<DashboardItem> dashboardItemList = new ArrayList<>();
        for (int i = 0; i < dashboardItems.size(); i++) {
            if (TextUtils.equals(user_type, USER_TYPE_PARENT)) {
                if (TextUtils.equals(dashboardItems.get(i).getParent(), KEY_ENABLED)) {
                    dashboardItemList.add(dashboardItems.get(i));
                }
            } else if (TextUtils.equals(user_type, USER_TYPE_TEACHER)) {
                if (TextUtils.equals(dashboardItems.get(i).getTeacher(), KEY_ENABLED)) {
                    dashboardItemList.add(dashboardItems.get(i));
                }
            }
        }
        return dashboardItemList;
    }

}
