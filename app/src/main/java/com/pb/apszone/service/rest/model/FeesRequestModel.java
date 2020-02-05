package com.pb.apszone.service.rest.model;

import com.google.gson.annotations.SerializedName;

public class FeesRequestModel {

    @SerializedName("year")
    private String year;

    @SerializedName("student_id")
    private String studentId;

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
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
                        ",student_id = '" + studentId + '\'' +
                        "}";
    }
}