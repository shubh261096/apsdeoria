package com.pb.apszone.service.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SyllabusResponseModel {

    @SerializedName("syllabus")
    private List<SyllabusItem> syllabus;

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    public void setSyllabus(List<SyllabusItem> syllabus) {
        this.syllabus = syllabus;
    }

    public List<SyllabusItem> getSyllabus() {
        return syllabus;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isError() {
        return error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return
                "SyllabusResponseModel{" +
                        "syllabus = '" + syllabus + '\'' +
                        ",error = '" + error + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}