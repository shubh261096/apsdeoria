package com.pb.apszone.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessaging;
import com.pb.apszone.service.model.DashboardItem;
import com.pb.apszone.service.model.ProfileResponseModel;
import com.pb.apszone.service.repo.Events;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.utils.KeyStorePref;

import java.util.ArrayList;
import java.util.List;

import static com.pb.apszone.utils.AppConstants.KEY_ENABLED;
import static com.pb.apszone.utils.AppConstants.KEY_SUBSCRIBE_CLASS_ID;
import static com.pb.apszone.utils.AppConstants.KEY_SUBSCRIBE_STUDENT;
import static com.pb.apszone.utils.AppConstants.KEY_SUBSCRIBE_TEACHER;
import static com.pb.apszone.utils.AppConstants.KEY_USER_TYPE;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_STUDENT;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_TEACHER;

public class DashboardViewModel extends AndroidViewModel {

    private MutableLiveData<Events.DashboardUIResponseEvent> dashboardUIResponseModelMutableLiveData;
    private KeyStorePref keyStorePref;
    private Repository repository;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        dashboardUIResponseModelMutableLiveData = new MutableLiveData<>();
        repository = Repository.getInstance();
        keyStorePref = KeyStorePref.getInstance(application.getApplicationContext());
    }

    public void sendRequest() {
        repository.getDashboardUIElements(dashboardUIResponseModelMutableLiveData);
    }

    public LiveData<Events.DashboardUIResponseEvent> getDashboardUIElements() {
        return dashboardUIResponseModelMutableLiveData;
    }


    public List<DashboardItem> addListData(List<DashboardItem> dashboardItems, String user_type) {
        List<DashboardItem> dashboardItemList = new ArrayList<>();
        for (int i = 0; i < dashboardItems.size(); i++) {
            if (TextUtils.equals(user_type, USER_TYPE_STUDENT)) {
                if (TextUtils.equals(dashboardItems.get(i).getStudent(), KEY_ENABLED)) {
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

    public void subscribeToTopic(ProfileResponseModel profileResponseModel) {
        if (TextUtils.equals(keyStorePref.getString(KEY_USER_TYPE), USER_TYPE_STUDENT)) {
            /* This is done to subscribe all students */
            if (!keyStorePref.getBoolean(KEY_SUBSCRIBE_STUDENT)) {
                FirebaseMessaging.getInstance().subscribeToTopic("student");
                keyStorePref.putBoolean(KEY_SUBSCRIBE_STUDENT, true);
            }

            /* This is done to subscribe students according to classID */
            if (!TextUtils.isEmpty(profileResponseModel.getProfile().getClassId().getId())) {
                if (!keyStorePref.getBoolean(KEY_SUBSCRIBE_CLASS_ID)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(profileResponseModel.getProfile().getClassId().getId());
                    keyStorePref.putBoolean(KEY_SUBSCRIBE_CLASS_ID, true);
                }
            }
        } else if (TextUtils.equals(keyStorePref.getString(KEY_USER_TYPE), USER_TYPE_TEACHER)) {
            /* This is done to subscribe all teachers */
            if (!keyStorePref.getBoolean(KEY_SUBSCRIBE_TEACHER)) {
                FirebaseMessaging.getInstance().subscribeToTopic("teacher");
                keyStorePref.putBoolean(KEY_SUBSCRIBE_TEACHER, true);
            }
        }
    }

    public static void unsubscribeFromAllTopic() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("student");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("teacher");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("C101");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("C102");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("C103");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("C104");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("C105");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("C106");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("C107");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("C108");
    }

}
