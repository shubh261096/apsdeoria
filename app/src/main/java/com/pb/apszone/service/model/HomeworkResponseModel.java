package com.pb.apszone.service.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class HomeworkResponseModel {

    @SerializedName("homework")
    private List<HomeworkItem> homework;

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    public void setHomework(List<HomeworkItem> homework) {
        this.homework = homework;
    }

    public List<HomeworkItem> getHomework() {
        return homework;
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
                "HomeworkResponseModel{" +
                        "homework = '" + homework + '\'' +
                        ",error = '" + error + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}