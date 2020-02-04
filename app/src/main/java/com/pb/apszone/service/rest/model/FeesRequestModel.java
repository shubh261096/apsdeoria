package com.pb.apszone.service.rest.model;

import com.google.gson.annotations.SerializedName;

public class FeesRequestModel {

    @SerializedName("year")
    private String year;

    @SerializedName("class_id")
    private String classId;

    @SerializedName("student_id")
    private String studentId;

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassId() {
        return classId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    @Override
    public String toString() {
        return
                "FeesRequestModel{" +
                        "year = '" + year + '\'' +
                        ",class_id = '" + classId + '\'' +
                        ",student_id = '" + studentId + '\'' +
                        "}";
    }
}