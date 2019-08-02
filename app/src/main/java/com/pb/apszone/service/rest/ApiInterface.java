package com.pb.apszone.service.rest;

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

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @POST("Login")
    @FormUrlEncoded
    Call<LoginResponseModel> checkLogin(@FieldMap Map<String, String> params);

    @POST("Profile")
    @FormUrlEncoded
    Call<ProfileResponseModel> getProfile(@FieldMap Map<String, String> params);

    @GET("Dashboard")
    Call<DashboardUIResponseModel> getDashboardUIElements();

    @POST("TimeTable")
    @FormUrlEncoded
    Call<TimetableResponseModel> getTimeTable(@FieldMap Map<String, String> params);

    @POST("Attendance")
    @FormUrlEncoded
    Call<AttendanceResponseModel> getAttendance(@FieldMap Map<String, String> params);

    @POST("Syllabus")
    @FormUrlEncoded
    Call<SyllabusResponseModel> getSyllabus(@FieldMap Map<String, String> params);

    @POST("Homework")
    @FormUrlEncoded
    Call<HomeworkResponseModel> getHomework(@FieldMap Map<String, String> params);

    @POST("Fees")
    @FormUrlEncoded
    Call<FeesResponseModel> getFees(@FieldMap Map<String, String> params);

    @GET("Inbox")
    Call<InboxResponseModel> getInbox();

    /* Teacher's api end point */
    @POST("teacher/Attendance")
    @FormUrlEncoded
    Call<ClassDetailResponseModel> getClassDetail(@FieldMap Map<String, String> params);

    @POST("teacher/Attendance/edit")
    Call<SubmitAttendanceResponseModel> editAttendance(@Body SubmitAttendanceRequestModel submitAttendanceRequestModel);

    @POST("teacher/Attendance/add")
    Call<SubmitAttendanceResponseModel> addAttendance(@Body SubmitAttendanceRequestModel submitAttendanceRequestModel);

    @POST("teacher/Homework")
    @FormUrlEncoded
    Call<ClassSubjectResponseModel> getClassSubjectDetail(@FieldMap Map<String, String> params);

    @POST("teacher/Homework/add")
    Call<CommonResponseModel> addHomework(@Body HomeworkRequestModel homeworkRequestModel);

    @POST("teacher/Syllabus")
    @FormUrlEncoded
    Call<CommonResponseModel> checkSyllabus(@FieldMap Map<String, String> params);

    @Multipart
    @POST("teacher/Syllabus/update")
    Call<CommonResponseModel> updateSyllabus(@Part MultipartBody.Part file, @Part("subject_id") RequestBody subject_id);

    @POST("Login/validate")
    @FormUrlEncoded
    Call<CommonResponseModel> validateResetPassword(@FieldMap Map<String, String> params);

    @POST("Login/reset")
    @FormUrlEncoded
    Call<CommonResponseModel> resetPassword(@FieldMap Map<String, String> params);
}