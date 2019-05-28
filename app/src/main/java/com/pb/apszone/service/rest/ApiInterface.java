package com.pb.apszone.service.rest;

import com.pb.apszone.service.model.AttendanceResponseModel;
import com.pb.apszone.service.model.DashboardUIResponseModel;
import com.pb.apszone.service.model.LoginResponseModel;
import com.pb.apszone.service.model.ProfileResponseModel;
import com.pb.apszone.service.model.TimetableResponseModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

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
}