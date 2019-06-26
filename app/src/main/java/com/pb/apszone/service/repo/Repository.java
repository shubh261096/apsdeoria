package com.pb.apszone.service.repo;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.pb.apszone.service.model.AttendanceResponseModel;
import com.pb.apszone.service.model.DashboardUIResponseModel;
import com.pb.apszone.service.model.FeesResponseModel;
import com.pb.apszone.service.model.HomeworkResponseModel;
import com.pb.apszone.service.model.InboxResponseModel;
import com.pb.apszone.service.model.LoginResponseModel;
import com.pb.apszone.service.model.ProfileResponseModel;
import com.pb.apszone.service.model.SyllabusResponseModel;
import com.pb.apszone.service.model.TimetableResponseModel;
import com.pb.apszone.service.rest.ApiClient;
import com.pb.apszone.service.rest.ApiInterface;
import com.pb.apszone.service.rest.AttendanceRequestModel;
import com.pb.apszone.service.rest.FeesRequestModel;
import com.pb.apszone.service.rest.HomeworkRequestModel;
import com.pb.apszone.service.rest.LoginRequestModel;
import com.pb.apszone.service.rest.ProfileRequestModel;
import com.pb.apszone.service.rest.SyllabusRequestModel;
import com.pb.apszone.service.rest.TimetableRequestModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pb.apszone.utils.AppConstants.KEY_FILTER_BY_DAY;
import static com.pb.apszone.utils.AppConstants.KEY_FILTER_BY_WEEK;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_PARENT;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_TEACHER;

public class Repository {
    private static ApiInterface apiService;

    private static class SingletonHelper {
        private static final Repository INSTANCE = new Repository();
    }

    public static Repository getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private Repository() {
        apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    /* Login Request */
    public MutableLiveData<LoginResponseModel> checkLogin(LoginRequestModel loginRequestModel) {
        final MutableLiveData<LoginResponseModel> data = new MutableLiveData<>();
        Map<String, String> params = new HashMap<>();
        params.put("email", loginRequestModel.getEmail());
        params.put("password", loginRequestModel.getPassword());
        apiService.checkLogin(params)
                .enqueue(new Callback<LoginResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponseModel> call, @Nullable Response<LoginResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                data.postValue(response.body());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponseModel> call, Throwable t) {
                        data.postValue(null);
                    }
                });
        return data;
    }

    /* Profile Request */
    public MutableLiveData<ProfileResponseModel> getProfile(ProfileRequestModel profileRequestModel) {
        final MutableLiveData<ProfileResponseModel> data = new MutableLiveData<>();
        Map<String, String> params = new HashMap<>();
        params.put("id", profileRequestModel.getId());
        params.put("type", profileRequestModel.getType());
        apiService.getProfile(params)
                .enqueue(new Callback<ProfileResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ProfileResponseModel> call, @Nullable Response<ProfileResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                data.postValue(response.body());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ProfileResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        data.postValue(null);
                    }
                });
        return data;
    }

    /* Dashboard UI Element Request */
    public MutableLiveData<DashboardUIResponseModel> getDashboardUIElements() {
        final MutableLiveData<DashboardUIResponseModel> data = new MutableLiveData<>();
        apiService.getDashboardUIElements()
                .enqueue(new Callback<DashboardUIResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<DashboardUIResponseModel> call, @Nullable Response<DashboardUIResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                data.postValue(response.body());
                                Log.i("Response ", response.message());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DashboardUIResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        data.postValue(null);
                    }
                });
        return data;
    }

    /* TimeTable Request */
    public MutableLiveData<TimetableResponseModel> getTimetable(TimetableRequestModel timetableRequestModel, String filter, String user_type) {
        final MutableLiveData<TimetableResponseModel> data = new MutableLiveData<>();
        Map<String, String> params = new HashMap<>();
        if (TextUtils.equals(user_type, USER_TYPE_PARENT)) {
            if (TextUtils.equals(filter, KEY_FILTER_BY_DAY)) {
                params.put("class_id", timetableRequestModel.getClassId());
                params.put("today", timetableRequestModel.getToday());
            } else if (TextUtils.equals(filter, KEY_FILTER_BY_WEEK)) {
                params.put("class_id", timetableRequestModel.getClassId());
            }
        } else if (TextUtils.equals(user_type, USER_TYPE_TEACHER)) {
            if (TextUtils.equals(filter, KEY_FILTER_BY_DAY)) {
                params.put("teacher_id", timetableRequestModel.getTeacherId());
                params.put("today", timetableRequestModel.getToday());
            }
        }

        apiService.getTimeTable(params)
                .enqueue(new Callback<TimetableResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<TimetableResponseModel> call, @Nullable Response<TimetableResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                data.postValue(response.body());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TimetableResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        data.postValue(null);
                    }
                });
        return data;
    }

    /* Attendance Request */
    public MutableLiveData<AttendanceResponseModel> getAttendance(AttendanceRequestModel attendanceRequestModel) {
        final MutableLiveData<AttendanceResponseModel> data = new MutableLiveData<>();
        Map<String, String> params = new HashMap<>();
        params.put("student_id", attendanceRequestModel.getStudentId());
        params.put("month", attendanceRequestModel.getMonth());
        params.put("year", attendanceRequestModel.getYear());

        apiService.getAttendance(params)
                .enqueue(new Callback<AttendanceResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<AttendanceResponseModel> call, @Nullable Response<AttendanceResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                data.postValue(response.body());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AttendanceResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        data.postValue(null);
                    }
                });
        return data;
    }

    /* Syllabus Request */
    public MutableLiveData<SyllabusResponseModel> getSyllabus(SyllabusRequestModel syllabusRequestModel) {
        final MutableLiveData<SyllabusResponseModel> data = new MutableLiveData<>();
        Map<String, String> params = new HashMap<>();
        params.put("class_id", syllabusRequestModel.getClassId());

        apiService.getSyllabus(params)
                .enqueue(new Callback<SyllabusResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<SyllabusResponseModel> call, @Nullable Response<SyllabusResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                data.postValue(response.body());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SyllabusResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        data.postValue(null);
                    }
                });
        return data;
    }

    /* Homework Request */
    public MutableLiveData<HomeworkResponseModel> getHomework(HomeworkRequestModel homeworkRequestModel) {
        final MutableLiveData<HomeworkResponseModel> data = new MutableLiveData<>();
        Map<String, String> params = new HashMap<>();
        params.put("class_id", homeworkRequestModel.getClassId());
        params.put("date", homeworkRequestModel.getDate());

        apiService.getHomework(params)
                .enqueue(new Callback<HomeworkResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<HomeworkResponseModel> call, @Nullable Response<HomeworkResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                data.postValue(response.body());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<HomeworkResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        data.postValue(null);
                    }
                });
        return data;
    }

    /* Fees Request */
    public MutableLiveData<FeesResponseModel> getFees(FeesRequestModel feesRequestModel) {
        final MutableLiveData<FeesResponseModel> data = new MutableLiveData<>();
        Map<String, String> params = new HashMap<>();
        params.put("class_id", feesRequestModel.getClassId());
        params.put("year", feesRequestModel.getYear());
        params.put("student_id", feesRequestModel.getStudentId());

        apiService.getFees(params)
                .enqueue(new Callback<FeesResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<FeesResponseModel> call, @Nullable Response<FeesResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                data.postValue(response.body());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<FeesResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        data.postValue(null);
                    }
                });
        return data;
    }

    /* Inbox Request */
    public MutableLiveData<InboxResponseModel> getInbox() {
        final MutableLiveData<InboxResponseModel> data = new MutableLiveData<>();
        apiService.getInbox()
                .enqueue(new Callback<InboxResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<InboxResponseModel> call, @Nullable Response<InboxResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                data.postValue(response.body());
                                Log.i("Response ", response.message());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<InboxResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        data.postValue(null);
                    }
                });
        return data;
    }

    private void handleResponseCode(int code) {
        if (code > 200)
            Log.i("Error Response Code ", code + ": Unauthorised");
    }

    /* Handle Retrofit Response Failure */
    private void handleFailureResponse(Throwable throwable) {
        if (throwable instanceof IOException) {
            Log.i("Network Error ", throwable.getMessage());
        } else {
            Log.i("Unknown Error ", throwable.getMessage());
        }
    }
}
