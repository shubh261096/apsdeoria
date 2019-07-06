package com.pb.apszone.service.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ClassSubjectResponseModel {

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("class_subject")
    private List<ClassSubjectItem> classSubject;

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

    public void setClassSubject(List<ClassSubjectItem> classSubject) {
        this.classSubject = classSubject;
    }

    public List<ClassSubjectItem> getClassSubject() {
        return classSubject;
    }

    @Override
    public String toString() {
        return
                "ClassSubjectResponseModel{" +
                        "error = '" + error + '\'' +
                        ",message = '" + message + '\'' +
                        ",class_subject = '" + classSubject + '\'' +
                        "}";
    }
}