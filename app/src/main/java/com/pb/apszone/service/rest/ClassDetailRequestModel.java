package com.pb.apszone.service.rest;

import com.google.gson.annotations.SerializedName;

public class ClassDetailRequestModel {

    @SerializedName("teacher_id")
    private String teacherId;

    @SerializedName("date")
    private String date;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    @Override
    public String toString() {
        return
                "ClassDetailRequestModel{" +
                        "teacher_id = '" + teacherId + '\'' +
                        "date = '" + date + '\'' +
                        "}";
    }
}