package com.pb.apszone.service.repo;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.pb.apszone.service.model.AttendanceResponseModel;
import com.pb.apszone.service.model.ClassDetailResponseModel;
import com.pb.apszone.service.model.ClassSubjectResponseModel;
import com.pb.apszone.service.model.CommonResponseModel;
import com.pb.apszone.service.model.DashboardUIResponseModel;
import com.pb.apszone.service.model.FeesResponseModel;
import com.pb.apszone.service.model.HomeworkResponseModel;
import com.pb.apszone.service.model.InboxResponseModel;
import com.pb.apszone.service.model.LoginResponseModel;
import com.pb.apszone.service.model.ProfileResponseModel;
import com.pb.apszone.service.model.SubmitAttendanceResponseModel;
import com.pb.apszone.service.model.SyllabusResponseModel;
import com.pb.apszone.service.model.TimetableResponseModel;
import com.pb.apszone.service.rest.ApiClient;
import com.pb.apszone.service.rest.ApiInterface;
import com.pb.apszone.service.rest.AttendanceRequestModel;
import com.pb.apszone.service.rest.ClassDetailRequestModel;
import com.pb.apszone.service.rest.SubmitAttendanceRequestModel;
import com.pb.apszone.service.rest.FeesRequestModel;
import com.pb.apszone.service.rest.HomeworkRequestModel;
import com.pb.apszone.service.rest.LoginRequestModel;
import com.pb.apszone.service.rest.ProfileRequestModel;
import com.pb.apszone.service.rest.SyllabusRequestModel;
import com.pb.apszone.service.rest.TimetableRequestModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
    public void checkLogin(LoginRequestModel loginRequestModel, MutableLiveData<LoginResponseModel> loginResponseModelMutableLiveData) {
        Map<String, String> params = new HashMap<>();
        params.put("id", loginRequestModel.getId());
        params.put("password", loginRequestModel.getPassword());
        apiService.checkLogin(params)
                .enqueue(new Callback<LoginResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponseModel> call, @Nullable Response<LoginResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                loginResponseModelMutableLiveData.postValue(response.body());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponseModel> call, Throwable t) {
                        loginResponseModelMutableLiveData.postValue(null);
                    }
                });
    }

    /* Profile Request */
    public void getProfile(ProfileRequestModel profileRequestModel, MutableLiveData<ProfileResponseModel> profileResponseModelMutableLiveData) {
        Map<String, String> params = new HashMap<>();
        params.put("id", profileRequestModel.getId());
        params.put("type", profileRequestModel.getType());
        apiService.getProfile(params)
                .enqueue(new Callback<ProfileResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ProfileResponseModel> call, @Nullable Response<ProfileResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                profileResponseModelMutableLiveData.postValue(response.body());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ProfileResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        profileResponseModelMutableLiveData.postValue(null);
                    }
                });
    }

    /* Dashboard UI Element Request */
    public void getDashboardUIElements(MutableLiveData<DashboardUIResponseModel> dashboardUIResponseModelMutableLiveData) {
        apiService.getDashboardUIElements()
                .enqueue(new Callback<DashboardUIResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<DashboardUIResponseModel> call, @Nullable Response<DashboardUIResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                dashboardUIResponseModelMutableLiveData.postValue(response.body());
                                Log.i("Response ", response.message());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DashboardUIResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        dashboardUIResponseModelMutableLiveData.postValue(null);
                    }
                });
    }

    /* TimeTable Request */
    public void getTimetable(TimetableRequestModel timetableRequestModel, String filter, String user_type, MutableLiveData<TimetableResponseModel> timetableResponseModelMutableLiveData) {
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
                                timetableResponseModelMutableLiveData.postValue(response.body());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TimetableResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        timetableResponseModelMutableLiveData.postValue(null);
                    }
                });
    }

    /* Attendance Request */
    public void getAttendance(AttendanceRequestModel attendanceRequestModel, MutableLiveData<AttendanceResponseModel> attendanceResponseModelMutableLiveData) {
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
                                attendanceResponseModelMutableLiveData.postValue(response.body());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AttendanceResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        attendanceResponseModelMutableLiveData.postValue(null);
                    }
                });
    }

    /* Syllabus Request */
    public void getSyllabus(SyllabusRequestModel syllabusRequestModel, MutableLiveData<SyllabusResponseModel> syllabusResponseModelMutableLiveData) {
        Map<String, String> params = new HashMap<>();
        params.put("class_id", syllabusRequestModel.getClassId());

        apiService.getSyllabus(params)
                .enqueue(new Callback<SyllabusResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<SyllabusResponseModel> call, @Nullable Response<SyllabusResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                syllabusResponseModelMutableLiveData.postValue(response.body());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SyllabusResponseModel> call, Throwable t) {
                        syllabusResponseModelMutableLiveData.postValue(null);
                        handleFailureResponse(t);
                    }
                });
    }

    /* Homework Request */
    public void getHomework(HomeworkRequestModel homeworkRequestModel, MutableLiveData<HomeworkResponseModel> homeworkResponseModelMutableLiveData) {
        Map<String, String> params = new HashMap<>();
        params.put("class_id", homeworkRequestModel.getClassId());
        params.put("date", homeworkRequestModel.getDate());

        apiService.getHomework(params)
                .enqueue(new Callback<HomeworkResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<HomeworkResponseModel> call, @Nullable Response<HomeworkResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                homeworkResponseModelMutableLiveData.postValue(response.body());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<HomeworkResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        homeworkResponseModelMutableLiveData.postValue(null);
                    }
                });
    }

    /* Fees Request */
    public void getFees(FeesRequestModel feesRequestModel, MutableLiveData<FeesResponseModel> feesResponseModelMutableLiveData) {
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
                                feesResponseModelMutableLiveData.postValue(response.body());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<FeesResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        feesResponseModelMutableLiveData.postValue(null);
                    }
                });
    }

    /* Inbox Request */
    public void getInbox(MutableLiveData<InboxResponseModel> inboxResponseModelMutableLiveData) {
        apiService.getInbox()
                .enqueue(new Callback<InboxResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<InboxResponseModel> call, @Nullable Response<InboxResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                inboxResponseModelMutableLiveData.postValue(response.body());
                                Log.i("Response ", response.message());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<InboxResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        inboxResponseModelMutableLiveData.postValue(null);
                    }
                });
    }

    /* Teacher Attendance Class Details Request */
    public void getClassDetail(ClassDetailRequestModel classDetailRequestModel, MutableLiveData<ClassDetailResponseModel> classDetailResponseModelMutableLiveData) {
        Map<String, String> params = new HashMap<>();
        params.put("teacher_id", classDetailRequestModel.getTeacherId());
        params.put("date", classDetailRequestModel.getDate());

        apiService.getClassDetail(params)
                .enqueue(new Callback<ClassDetailResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ClassDetailResponseModel> call, @Nullable Response<ClassDetailResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                classDetailResponseModelMutableLiveData.postValue(response.body());
                                Log.i("Response ", response.message());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ClassDetailResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        classDetailResponseModelMutableLiveData.postValue(null);
                    }
                });
    }

    /* Edit Attendance Request */
    public void editAttendance(SubmitAttendanceRequestModel submitAttendanceRequestModel, MutableLiveData<SubmitAttendanceResponseModel> submitAttendanceResponseModelMutableLiveData) {
        apiService.editAttendance(submitAttendanceRequestModel)
                .enqueue(new Callback<SubmitAttendanceResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<SubmitAttendanceResponseModel> call, @Nullable Response<SubmitAttendanceResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                submitAttendanceResponseModelMutableLiveData.postValue(response.body());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubmitAttendanceResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        submitAttendanceResponseModelMutableLiveData.postValue(null);
                    }
                });
    }

    /* Add Attendance Request */
    public void addAttendance(SubmitAttendanceRequestModel submitAttendanceRequestModel, MutableLiveData<SubmitAttendanceResponseModel> submitAttendanceResponseModelMutableLiveData) {
        apiService.addAttendance(submitAttendanceRequestModel)
                .enqueue(new Callback<SubmitAttendanceResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<SubmitAttendanceResponseModel> call, @Nullable Response<SubmitAttendanceResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                submitAttendanceResponseModelMutableLiveData.postValue(response.body());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubmitAttendanceResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        submitAttendanceResponseModelMutableLiveData.postValue(null);
                    }
                });
    }

    /* Teacher Homework Class Subject Details Request */
    public void getClassSubjectDetail(HomeworkRequestModel homeworkRequestModel, MutableLiveData<ClassSubjectResponseModel> classSubjectResponseModelMutableLiveData) {
        Map<String, String> params = new HashMap<>();
        params.put("teacher_id", homeworkRequestModel.getTeacherId());

        apiService.getClassSubjectDetail(params)
                .enqueue(new Callback<ClassSubjectResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ClassSubjectResponseModel> call, @Nullable Response<ClassSubjectResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                classSubjectResponseModelMutableLiveData.postValue(response.body());
                                Log.i("Response ", response.message());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ClassSubjectResponseModel> call, @NonNull Throwable t) {
                        handleFailureResponse(t);
                        classSubjectResponseModelMutableLiveData.postValue(null);
                    }
                });
    }

    /* Add Homework Request */
    public void addHomework(HomeworkRequestModel homeworkRequestModel, MutableLiveData<CommonResponseModel> commonResponseModelMutableLiveData) {
        apiService.addHomework(homeworkRequestModel)
                .enqueue(new Callback<CommonResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<CommonResponseModel> call, @Nullable Response<CommonResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                commonResponseModelMutableLiveData.postValue(response.body());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CommonResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        commonResponseModelMutableLiveData.postValue(null);
                    }
                });
    }

    /* Check Syllabus Request */
    public void checkSyllabus(SyllabusRequestModel syllabusRequestModel, MutableLiveData<CommonResponseModel> commonResponseModelMutableLiveData) {
        Map<String, String> params = new HashMap<>();
        params.put("subject_id", syllabusRequestModel.getSubjectId());

        apiService.checkSyllabus(params)
                .enqueue(new Callback<CommonResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<CommonResponseModel> call, @Nullable Response<CommonResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                commonResponseModelMutableLiveData.postValue(response.body());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CommonResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        commonResponseModelMutableLiveData.postValue(null);
                    }
                });
    }

    /* Update Syllabus Request */
    public void updateSyllabus(MultipartBody.Part file, RequestBody subject_id, MutableLiveData<CommonResponseModel> commonResponseModelMutableLiveData) {
        apiService.updateSyllabus(file, subject_id)
                .enqueue(new Callback<CommonResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<CommonResponseModel> call, @Nullable Response<CommonResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                commonResponseModelMutableLiveData.postValue(response.body());
                            } else {
                                handleResponseCode(response.code());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CommonResponseModel> call, Throwable t) {
                        handleFailureResponse(t);
                        commonResponseModelMutableLiveData.postValue(null);
                    }
                });
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
