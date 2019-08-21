package com.pb.apszone.service.repo;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pb.apszone.service.model.AttendanceResponseModel;
import com.pb.apszone.service.model.ClassDetailResponseModel;
import com.pb.apszone.service.model.ClassSubjectResponseModel;
import com.pb.apszone.service.model.CommonResponseModel;
import com.pb.apszone.service.model.DashboardUIResponseModel;
import com.pb.apszone.service.model.ErrorModel;
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
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pb.apszone.utils.AppConstants.ErrorConstants.BAD_REQUEST_ERROR;
import static com.pb.apszone.utils.AppConstants.ErrorConstants.SERVER_ERROR;
import static com.pb.apszone.utils.AppConstants.ErrorConstants.SOCKET_ERROR;
import static com.pb.apszone.utils.AppConstants.ErrorConstants.UNKNOWN_ERROR;
import static com.pb.apszone.utils.AppConstants.KEY_FILTER_BY_DAY;
import static com.pb.apszone.utils.AppConstants.KEY_FILTER_BY_WEEK;
import static com.pb.apszone.utils.AppConstants.USER_TYPE_STUDENT;
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
    public void checkLogin(LoginRequestModel loginRequestModel, MutableLiveData<Events.LoginResponseEvent> loginResponseModelMutableLiveData) {
        Map<String, String> params = new HashMap<>();
        params.put("id", loginRequestModel.getId());
        params.put("password", loginRequestModel.getPassword());
        apiService.checkLogin(params)
                .enqueue(new Callback<LoginResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponseModel> call, @Nullable Response<LoginResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                loginResponseModelMutableLiveData.
                                        postValue(new Events.LoginResponseEvent(null, true, response.body()));
                            } else {
                                if (response.errorBody() != null) {
                                    loginResponseModelMutableLiveData.
                                            postValue(new Events.LoginResponseEvent(buildErrorModel(response.code(), response.errorBody()), false, null));
                                } else {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setMessage(UNKNOWN_ERROR);
                                    loginResponseModelMutableLiveData.
                                            postValue(new Events.LoginResponseEvent(errorModel, false, null));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponseModel> call, @NonNull Throwable t) {
                        String errorMsg = handleFailureResponse(t);
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setMessage(errorMsg);
                        loginResponseModelMutableLiveData.
                                postValue(new Events.LoginResponseEvent(errorModel, false, null));
                    }
                });
    }

    /* Profile Request */
    public void getProfile(ProfileRequestModel profileRequestModel, MutableLiveData<Events.ProfileResponseEvent> profileResponseModelMutableLiveData) {
        Map<String, String> params = new HashMap<>();
        params.put("id", profileRequestModel.getId());
        params.put("type", profileRequestModel.getType());
        apiService.getProfile(params)
                .enqueue(new Callback<ProfileResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ProfileResponseModel> call, @Nullable Response<ProfileResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                profileResponseModelMutableLiveData.
                                        postValue(new Events.ProfileResponseEvent(null, true, response.body()));
                            } else {
                                if (response.errorBody() != null) {
                                    profileResponseModelMutableLiveData.
                                            postValue(new Events.ProfileResponseEvent(buildErrorModel(response.code(), response.errorBody()), false, null));
                                } else {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setMessage(UNKNOWN_ERROR);
                                    profileResponseModelMutableLiveData.
                                            postValue(new Events.ProfileResponseEvent(errorModel, false, null));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ProfileResponseModel> call, @NonNull Throwable t) {
                        String errorMsg = handleFailureResponse(t);
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setMessage(errorMsg);
                        profileResponseModelMutableLiveData.
                                postValue(new Events.ProfileResponseEvent(errorModel, false, null));
                    }
                });
    }

    /* Dashboard UI Element Request */
    public void getDashboardUIElements(MutableLiveData<Events.DashboardUIResponseEvent> dashboardUIResponseModelMutableLiveData) {
        apiService.getDashboardUIElements()
                .enqueue(new Callback<DashboardUIResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<DashboardUIResponseModel> call, @Nullable Response<DashboardUIResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                dashboardUIResponseModelMutableLiveData.
                                        postValue(new Events.DashboardUIResponseEvent(null, true, response.body()));
                            } else {
                                if (response.errorBody() != null) {
                                    dashboardUIResponseModelMutableLiveData.
                                            postValue(new Events.DashboardUIResponseEvent(buildErrorModel(response.code(), response.errorBody()), false, null));
                                } else {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setMessage(UNKNOWN_ERROR);
                                    dashboardUIResponseModelMutableLiveData.
                                            postValue(new Events.DashboardUIResponseEvent(errorModel, false, null));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DashboardUIResponseModel> call, @NonNull Throwable t) {
                        String errorMsg = handleFailureResponse(t);
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setMessage(errorMsg);
                        dashboardUIResponseModelMutableLiveData.
                                postValue(new Events.DashboardUIResponseEvent(errorModel, false, null));
                    }
                });
    }

    /* TimeTable Request */
    public void getTimetable(TimetableRequestModel timetableRequestModel, String filter, String user_type, MutableLiveData<Events.TimetableResponseEvent> timetableResponseModelMutableLiveData) {
        Map<String, String> params = new HashMap<>();
        if (TextUtils.equals(user_type, USER_TYPE_STUDENT)) {
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
                                timetableResponseModelMutableLiveData.
                                        postValue(new Events.TimetableResponseEvent(null, true, response.body()));
                            } else {
                                if (response.errorBody() != null) {
                                    timetableResponseModelMutableLiveData.
                                            postValue(new Events.TimetableResponseEvent(buildErrorModel(response.code(), response.errorBody()), false, null));
                                } else {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setMessage(UNKNOWN_ERROR);
                                    timetableResponseModelMutableLiveData.
                                            postValue(new Events.TimetableResponseEvent(errorModel, false, null));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TimetableResponseModel> call, @NonNull Throwable t) {
                        String errorMsg = handleFailureResponse(t);
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setMessage(errorMsg);
                        timetableResponseModelMutableLiveData.
                                postValue(new Events.TimetableResponseEvent(errorModel, false, null));
                    }
                });
    }

    /* Attendance Request */
    public void getAttendance(AttendanceRequestModel attendanceRequestModel, MutableLiveData<Events.AttendanceResponseEvent> attendanceResponseModelMutableLiveData) {
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
                                attendanceResponseModelMutableLiveData.
                                        postValue(new Events.AttendanceResponseEvent(null, true, response.body()));
                            } else {
                                if (response.errorBody() != null) {
                                    attendanceResponseModelMutableLiveData.
                                            postValue(new Events.AttendanceResponseEvent(buildErrorModel(response.code(), response.errorBody()), false, null));
                                } else {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setMessage(UNKNOWN_ERROR);
                                    attendanceResponseModelMutableLiveData.
                                            postValue(new Events.AttendanceResponseEvent(errorModel, false, null));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AttendanceResponseModel> call, @NonNull Throwable t) {
                        String errorMsg = handleFailureResponse(t);
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setMessage(errorMsg);
                        attendanceResponseModelMutableLiveData.
                                postValue(new Events.AttendanceResponseEvent(errorModel, false, null));
                    }
                });
    }

    /* Syllabus Request */
    public void getSyllabus(SyllabusRequestModel syllabusRequestModel, MutableLiveData<Events.SyllabusResponseEvent> syllabusResponseModelMutableLiveData) {
        Map<String, String> params = new HashMap<>();
        params.put("class_id", syllabusRequestModel.getClassId());

        apiService.getSyllabus(params)
                .enqueue(new Callback<SyllabusResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<SyllabusResponseModel> call, @Nullable Response<SyllabusResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                syllabusResponseModelMutableLiveData.
                                        postValue(new Events.SyllabusResponseEvent(null, true, response.body()));
                            } else {
                                if (response.errorBody() != null) {
                                    syllabusResponseModelMutableLiveData.
                                            postValue(new Events.SyllabusResponseEvent(buildErrorModel(response.code(), response.errorBody()), false, null));
                                } else {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setMessage(UNKNOWN_ERROR);
                                    syllabusResponseModelMutableLiveData.
                                            postValue(new Events.SyllabusResponseEvent(errorModel, false, null));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SyllabusResponseModel> call, @NonNull Throwable t) {
                        String errorMsg = handleFailureResponse(t);
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setMessage(errorMsg);
                        syllabusResponseModelMutableLiveData.
                                postValue(new Events.SyllabusResponseEvent(errorModel, false, null));
                    }
                });
    }

    /* Homework Request */
    public void getHomework(HomeworkRequestModel homeworkRequestModel, MutableLiveData<Events.HomeworkResponseEvent> homeworkResponseModelMutableLiveData) {
        Map<String, String> params = new HashMap<>();
        params.put("class_id", homeworkRequestModel.getClassId());
        params.put("date", homeworkRequestModel.getDate());

        apiService.getHomework(params)
                .enqueue(new Callback<HomeworkResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<HomeworkResponseModel> call, @Nullable Response<HomeworkResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                homeworkResponseModelMutableLiveData.
                                        postValue(new Events.HomeworkResponseEvent(null, true, response.body()));
                            } else {
                                if (response.errorBody() != null) {
                                    homeworkResponseModelMutableLiveData.
                                            postValue(new Events.HomeworkResponseEvent(buildErrorModel(response.code(), response.errorBody()), false, null));
                                } else {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setMessage(UNKNOWN_ERROR);
                                    homeworkResponseModelMutableLiveData.
                                            postValue(new Events.HomeworkResponseEvent(errorModel, false, null));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<HomeworkResponseModel> call, @NonNull Throwable t) {
                        String errorMsg = handleFailureResponse(t);
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setMessage(errorMsg);
                        homeworkResponseModelMutableLiveData.
                                postValue(new Events.HomeworkResponseEvent(errorModel, false, null));
                    }
                });
    }

    /* Fees Request */
    public void getFees(FeesRequestModel feesRequestModel, MutableLiveData<Events.FeesResponseEvent> feesResponseModelMutableLiveData) {
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
                                feesResponseModelMutableLiveData.
                                        postValue(new Events.FeesResponseEvent(null, true, response.body()));
                            } else {
                                if (response.errorBody() != null) {
                                    feesResponseModelMutableLiveData.
                                            postValue(new Events.FeesResponseEvent(buildErrorModel(response.code(), response.errorBody()), false, null));
                                } else {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setMessage(UNKNOWN_ERROR);
                                    feesResponseModelMutableLiveData.
                                            postValue(new Events.FeesResponseEvent(errorModel, false, null));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<FeesResponseModel> call, @NonNull Throwable t) {
                        String errorMsg = handleFailureResponse(t);
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setMessage(errorMsg);
                        feesResponseModelMutableLiveData.
                                postValue(new Events.FeesResponseEvent(errorModel, false, null));
                    }
                });
    }

    /* Inbox Request */
    public void getInbox(MutableLiveData<Events.InboxResponseEvent> inboxResponseModelMutableLiveData) {
        apiService.getInbox()
                .enqueue(new Callback<InboxResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<InboxResponseModel> call, @Nullable Response<InboxResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                inboxResponseModelMutableLiveData.
                                        postValue(new Events.InboxResponseEvent(null, true, response.body()));
                            } else {
                                if (response.errorBody() != null) {
                                    inboxResponseModelMutableLiveData.
                                            postValue(new Events.InboxResponseEvent(buildErrorModel(response.code(), response.errorBody()), false, null));
                                } else {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setMessage(UNKNOWN_ERROR);
                                    inboxResponseModelMutableLiveData.
                                            postValue(new Events.InboxResponseEvent(errorModel, false, null));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<InboxResponseModel> call, @NonNull Throwable t) {
                        String errorMsg = handleFailureResponse(t);
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setMessage(errorMsg);
                        inboxResponseModelMutableLiveData.
                                postValue(new Events.InboxResponseEvent(errorModel, false, null));
                    }
                });
    }

    /* Teacher Attendance Class Details Request */
    public void getClassDetail(ClassDetailRequestModel classDetailRequestModel, MutableLiveData<Events.ClassDetailResponseEvent> classDetailResponseModelMutableLiveData) {
        Map<String, String> params = new HashMap<>();
        params.put("teacher_id", classDetailRequestModel.getTeacherId());
        params.put("date", classDetailRequestModel.getDate());

        apiService.getClassDetail(params)
                .enqueue(new Callback<ClassDetailResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ClassDetailResponseModel> call, @Nullable Response<ClassDetailResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                classDetailResponseModelMutableLiveData.
                                        postValue(new Events.ClassDetailResponseEvent(null, true, response.body()));
                            } else {
                                if (response.errorBody() != null) {
                                    classDetailResponseModelMutableLiveData.
                                            postValue(new Events.ClassDetailResponseEvent(buildErrorModel(response.code(), response.errorBody()), false, null));
                                } else {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setMessage(UNKNOWN_ERROR);
                                    classDetailResponseModelMutableLiveData.
                                            postValue(new Events.ClassDetailResponseEvent(errorModel, false, null));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ClassDetailResponseModel> call, @NonNull Throwable t) {
                        String errorMsg = handleFailureResponse(t);
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setMessage(errorMsg);
                        classDetailResponseModelMutableLiveData.
                                postValue(new Events.ClassDetailResponseEvent(errorModel, false, null));
                    }
                });
    }

    /* Edit Attendance Request */
    public void editAttendance(SubmitAttendanceRequestModel submitAttendanceRequestModel, MutableLiveData<Events.SubmitAttendanceResponseEvent> submitAttendanceResponseModelMutableLiveData) {
        apiService.editAttendance(submitAttendanceRequestModel)
                .enqueue(new Callback<SubmitAttendanceResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<SubmitAttendanceResponseModel> call, @Nullable Response<SubmitAttendanceResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                submitAttendanceResponseModelMutableLiveData.
                                        postValue(new Events.SubmitAttendanceResponseEvent(null, true, response.body()));
                            } else {
                                if (response.errorBody() != null) {
                                    submitAttendanceResponseModelMutableLiveData.
                                            postValue(new Events.SubmitAttendanceResponseEvent(buildErrorModel(response.code(), response.errorBody()), false, null));
                                } else {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setMessage(UNKNOWN_ERROR);
                                    submitAttendanceResponseModelMutableLiveData.
                                            postValue(new Events.SubmitAttendanceResponseEvent(errorModel, false, null));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubmitAttendanceResponseModel> call, @NonNull Throwable t) {
                        String errorMsg = handleFailureResponse(t);
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setMessage(errorMsg);
                        submitAttendanceResponseModelMutableLiveData.
                                postValue(new Events.SubmitAttendanceResponseEvent(errorModel, false, null));
                    }
                });
    }

    /* Add Attendance Request */
    public void addAttendance(SubmitAttendanceRequestModel submitAttendanceRequestModel, MutableLiveData<Events.SubmitAttendanceResponseEvent> submitAttendanceResponseModelMutableLiveData) {
        apiService.addAttendance(submitAttendanceRequestModel)
                .enqueue(new Callback<SubmitAttendanceResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<SubmitAttendanceResponseModel> call, @Nullable Response<SubmitAttendanceResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                submitAttendanceResponseModelMutableLiveData.
                                        postValue(new Events.SubmitAttendanceResponseEvent(null, true, response.body()));
                            } else {
                                if (response.errorBody() != null) {
                                    submitAttendanceResponseModelMutableLiveData.
                                            postValue(new Events.SubmitAttendanceResponseEvent(buildErrorModel(response.code(), response.errorBody()), false, null));
                                } else {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setMessage(UNKNOWN_ERROR);
                                    submitAttendanceResponseModelMutableLiveData.
                                            postValue(new Events.SubmitAttendanceResponseEvent(errorModel, false, null));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubmitAttendanceResponseModel> call, @NonNull Throwable t) {
                        String errorMsg = handleFailureResponse(t);
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setMessage(errorMsg);
                        submitAttendanceResponseModelMutableLiveData.
                                postValue(new Events.SubmitAttendanceResponseEvent(errorModel, false, null));
                    }
                });
    }

    /* Teacher Homework Class Subject Details Request */
    public void getClassSubjectDetail(HomeworkRequestModel homeworkRequestModel, MutableLiveData<Events.ClassSubjectResponseEvent> classSubjectResponseModelMutableLiveData) {
        Map<String, String> params = new HashMap<>();
        params.put("teacher_id", homeworkRequestModel.getTeacherId());

        apiService.getClassSubjectDetail(params)
                .enqueue(new Callback<ClassSubjectResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ClassSubjectResponseModel> call, @Nullable Response<ClassSubjectResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                classSubjectResponseModelMutableLiveData.
                                        postValue(new Events.ClassSubjectResponseEvent(null, true, response.body()));
                            } else {
                                if (response.errorBody() != null) {
                                    classSubjectResponseModelMutableLiveData.
                                            postValue(new Events.ClassSubjectResponseEvent(buildErrorModel(response.code(), response.errorBody()), false, null));
                                } else {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setMessage(UNKNOWN_ERROR);
                                    classSubjectResponseModelMutableLiveData.
                                            postValue(new Events.ClassSubjectResponseEvent(errorModel, false, null));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ClassSubjectResponseModel> call, @NonNull Throwable t) {
                        String errorMsg = handleFailureResponse(t);
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setMessage(errorMsg);
                        classSubjectResponseModelMutableLiveData.
                                postValue(new Events.ClassSubjectResponseEvent(errorModel, false, null));
                    }
                });
    }

    /* Add Homework Request */
    public void addHomework(HomeworkRequestModel homeworkRequestModel, MutableLiveData<Events.CommonResponseEvent> commonResponseModelMutableLiveData) {
        apiService.addHomework(homeworkRequestModel)
                .enqueue(new Callback<CommonResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<CommonResponseModel> call, @Nullable Response<CommonResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                commonResponseModelMutableLiveData.
                                        postValue(new Events.CommonResponseEvent(null, true, response.body()));
                            } else {
                                if (response.errorBody() != null) {
                                    commonResponseModelMutableLiveData.
                                            postValue(new Events.CommonResponseEvent(buildErrorModel(response.code(), response.errorBody()), false, null));
                                } else {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setMessage(UNKNOWN_ERROR);
                                    commonResponseModelMutableLiveData.
                                            postValue(new Events.CommonResponseEvent(errorModel, false, null));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CommonResponseModel> call, @NonNull Throwable t) {
                        String errorMsg = handleFailureResponse(t);
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setMessage(errorMsg);
                        commonResponseModelMutableLiveData.
                                postValue(new Events.CommonResponseEvent(errorModel, false, null));
                    }
                });
    }

    /* Check Syllabus Request */
    public void checkSyllabus(SyllabusRequestModel syllabusRequestModel, MutableLiveData<Events.CommonResponseEvent> commonResponseModelMutableLiveData) {
        Map<String, String> params = new HashMap<>();
        params.put("subject_id", syllabusRequestModel.getSubjectId());

        apiService.checkSyllabus(params)
                .enqueue(new Callback<CommonResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<CommonResponseModel> call, @Nullable Response<CommonResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                commonResponseModelMutableLiveData.
                                        postValue(new Events.CommonResponseEvent(null, true, response.body()));
                            } else {
                                if (response.errorBody() != null) {
                                    commonResponseModelMutableLiveData.
                                            postValue(new Events.CommonResponseEvent(buildErrorModel(response.code(), response.errorBody()), false, null));
                                } else {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setMessage(UNKNOWN_ERROR);
                                    commonResponseModelMutableLiveData.
                                            postValue(new Events.CommonResponseEvent(errorModel, false, null));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CommonResponseModel> call, @NonNull Throwable t) {
                        String errorMsg = handleFailureResponse(t);
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setMessage(errorMsg);
                        commonResponseModelMutableLiveData.
                                postValue(new Events.CommonResponseEvent(errorModel, false, null));
                    }
                });
    }

    /* Update Syllabus Request */
    public void updateSyllabus(MultipartBody.Part file, RequestBody subject_id, RequestBody subject_description, MutableLiveData<Events.CommonResponseEvent> commonResponseModelMutableLiveData) {
        apiService.updateSyllabus(file, subject_id, subject_description)
                .enqueue(new Callback<CommonResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<CommonResponseModel> call, @Nullable Response<CommonResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                commonResponseModelMutableLiveData.
                                        postValue(new Events.CommonResponseEvent(null, true, response.body()));
                            } else {
                                if (response.errorBody() != null) {
                                    commonResponseModelMutableLiveData.
                                            postValue(new Events.CommonResponseEvent(buildErrorModel(response.code(), response.errorBody()), false, null));
                                } else {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setMessage(UNKNOWN_ERROR);
                                    commonResponseModelMutableLiveData.
                                            postValue(new Events.CommonResponseEvent(errorModel, false, null));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CommonResponseModel> call, @NonNull Throwable t) {
                        String errorMsg = handleFailureResponse(t);
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setMessage(errorMsg);
                        commonResponseModelMutableLiveData.
                                postValue(new Events.CommonResponseEvent(errorModel, false, null));
                    }
                });
    }

    /* Validate Reset Password Request */
    public void validateResetPassword(LoginRequestModel loginRequestModel, MutableLiveData<Events.CommonResponseEvent> commonResponseModelMutableLiveData) {
        Map<String, String> params = new HashMap<>();
        params.put("id", loginRequestModel.getId());
        params.put("dob", loginRequestModel.getDob());
        apiService.validateResetPassword(params)
                .enqueue(new Callback<CommonResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<CommonResponseModel> call, @Nullable Response<CommonResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                commonResponseModelMutableLiveData.
                                        postValue(new Events.CommonResponseEvent(null, true, response.body()));
                            } else {
                                if (response.errorBody() != null) {
                                    commonResponseModelMutableLiveData.
                                            postValue(new Events.CommonResponseEvent(buildErrorModel(response.code(), response.errorBody()), false, null));
                                } else {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setMessage(UNKNOWN_ERROR);
                                    commonResponseModelMutableLiveData.
                                            postValue(new Events.CommonResponseEvent(errorModel, false, null));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CommonResponseModel> call, @NonNull Throwable t) {
                        String errorMsg = handleFailureResponse(t);
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setMessage(errorMsg);
                        commonResponseModelMutableLiveData.
                                postValue(new Events.CommonResponseEvent(errorModel, false, null));
                    }
                });
    }

    /* Reset Password Request */
    public void resetPassword(LoginRequestModel loginRequestModel, MutableLiveData<Events.CommonResponseEvent> commonResponseModelMutableLiveData) {
        Map<String, String> params = new HashMap<>();
        params.put("id", loginRequestModel.getId());
        params.put("password", loginRequestModel.getPassword());
        apiService.resetPassword(params)
                .enqueue(new Callback<CommonResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<CommonResponseModel> call, @Nullable Response<CommonResponseModel> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                commonResponseModelMutableLiveData.
                                        postValue(new Events.CommonResponseEvent(null, true, response.body()));
                            } else {
                                if (response.errorBody() != null) {
                                    commonResponseModelMutableLiveData.
                                            postValue(new Events.CommonResponseEvent(buildErrorModel(response.code(), response.errorBody()), false, null));
                                } else {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setMessage(UNKNOWN_ERROR);
                                    commonResponseModelMutableLiveData.
                                            postValue(new Events.CommonResponseEvent(errorModel, false, null));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CommonResponseModel> call, @NonNull Throwable t) {
                        String errorMsg = handleFailureResponse(t);
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setMessage(errorMsg);
                        commonResponseModelMutableLiveData.
                                postValue(new Events.CommonResponseEvent(errorModel, false, null));
                    }
                });
    }

    /* Handle Retrofit Response Failure */
    private String handleFailureResponse(Throwable throwable) {
        if (throwable instanceof IOException) {
            return SOCKET_ERROR;
        } else {
            return UNKNOWN_ERROR;
        }
    }

    private ErrorModel buildErrorModel(int responseCode, ResponseBody body) {
        Gson gson = new GsonBuilder().create();
        ErrorModel mError = new ErrorModel();
        try {
            mError = gson.fromJson(body.string(), ErrorModel.class);
        } catch (Exception e) {
            if (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST && responseCode < HttpURLConnection.HTTP_INTERNAL_ERROR) {
                mError.setMessage(BAD_REQUEST_ERROR);
            } else if (responseCode >= HttpURLConnection.HTTP_INTERNAL_ERROR && responseCode < (HttpURLConnection.HTTP_INTERNAL_ERROR + 100)) {
                mError.setMessage(SERVER_ERROR);
            } else {
                mError.setMessage(UNKNOWN_ERROR);
            }
        }
        return mError;
    }
}
