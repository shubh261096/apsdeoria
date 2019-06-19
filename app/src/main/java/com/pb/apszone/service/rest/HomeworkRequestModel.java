package com.pb.apszone.service.rest;


import com.google.gson.annotations.SerializedName;

public class HomeworkRequestModel {

    @SerializedName("date")
    private String date;

    @SerializedName("class_id")
    private String classId;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassId() {
        return classId;
    }

    @Override
    public String toString() {
        return
                "HomeworkRequestModel{" +
                        "date = '" + date + '\'' +
                        ",class_id = '" + classId + '\'' +
                        "}";
    }
}