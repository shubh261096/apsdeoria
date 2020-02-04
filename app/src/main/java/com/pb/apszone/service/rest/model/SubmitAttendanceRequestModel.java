package com.pb.apszone.service.rest.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.pb.apszone.service.model.AttendanceItem;

public class SubmitAttendanceRequestModel {

    @SerializedName("attendance")
    private List<AttendanceItem> attendance;

    public void setAttendance(List<AttendanceItem> attendance) {
        this.attendance = attendance;
    }

    public List<AttendanceItem> getAttendance() {
        return attendance;
    }

    @Override
    public String toString() {
        return
                "SubmitAttendanceRequestModel{" +
                        "attendance = '" + attendance + '\'' +
                        "}";
    }
}