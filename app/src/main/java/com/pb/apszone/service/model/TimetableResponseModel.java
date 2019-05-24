package com.pb.apszone.service.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class TimetableResponseModel {

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("timetable")
    private List<TimetableItem> timetable;

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

    public void setTimetable(List<TimetableItem> timetable) {
        this.timetable = timetable;
    }

    public List<TimetableItem> getTimetable() {
        return timetable;
    }

    @Override
    public String toString() {
        return
                "TimetableResponseModel{" +
                        "error = '" + error + '\'' +
                        ",message = '" + message + '\'' +
                        ",timetable = '" + timetable + '\'' +
                        "}";
    }
}