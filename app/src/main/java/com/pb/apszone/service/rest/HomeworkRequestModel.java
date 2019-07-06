package com.pb.apszone.service.rest;


import com.google.gson.annotations.SerializedName;

public class HomeworkRequestModel {

    @SerializedName("date")
    private String date;

    @SerializedName("class_id")
    private String classId;

    @SerializedName("teacher_id")
    private String teacherId;

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

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
    @Override
    public String toString() {
        return
                "HomeworkRequestModel{" +
                        "date = '" + date + '\'' +
                        ",class_id = '" + classId + '\'' +
                        ",teacher_id = '" + teacherId + '\'' +
                        "}";
    }
}