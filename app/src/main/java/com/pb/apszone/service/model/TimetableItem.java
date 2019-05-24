package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

public class TimetableItem {

    @SerializedName("subject_id")
    private SubjectId subjectId;

    @SerializedName("start_time")
    private String startTime;

    @SerializedName("teacher_id")
    private TeacherId teacherId;

    @SerializedName("class_id")
    private ClassId classId;

    @SerializedName("end_time")
    private String endTime;

    @SerializedName("id")
    private String id;

    @SerializedName("day")
    private String day;

    @SerializedName("status")
    private String status;

    public void setSubjectId(SubjectId subjectId) {
        this.subjectId = subjectId;
    }

    public SubjectId getSubjectId() {
        return subjectId;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setTeacherId(TeacherId teacherId) {
        this.teacherId = teacherId;
    }

    public TeacherId getTeacherId() {
        return teacherId;
    }

    public void setClassId(ClassId classId) {
        this.classId = classId;
    }

    public ClassId getClassId() {
        return classId;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return
                "TimetableItem{" +
                        "subject_id = '" + subjectId + '\'' +
                        ",start_time = '" + startTime + '\'' +
                        ",teacher_id = '" + teacherId + '\'' +
                        ",class_id = '" + classId + '\'' +
                        ",end_time = '" + endTime + '\'' +
                        ",id = '" + id + '\'' +
                        ",day = '" + day + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}