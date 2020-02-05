package com.pb.apszone.service.rest.model;

import com.google.gson.annotations.SerializedName;

public class TimetableRequestModel {

    @SerializedName("user_id")
    private String userId;

    @SerializedName("today")
    private String today;

    public void setToday(String today) {
        this.today = today;
    }

    public String getToday() {
        return today;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return
                "TimetableRequestModel{" +
                        ",today = '" + today + '\'' +
                        ",user_id = '" + userId + '\'' +
                        "}";
    }
}