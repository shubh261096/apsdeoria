package com.pb.apszone.service.rest;


import com.google.gson.annotations.SerializedName;

public class HomeworkRequestModel {

    @SerializedName("date")
    private String date;

    @SerializedName("subject_id")
    private String subjectId;

    @SerializedName("teacher_id")
    private String teacherId;

    @SerializedName("class_id")
    private String classId;

    @SerializedName("description")
    private String description;

    @SerializedName("title")
    private String title;

    @SerializedName("remarks")
    private String remarks;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassId() {
        return classId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    @Override
    public String toString() {
        return
                "HomeworkRequestModel{" +
                        "date = '" + date + '\'' +
                        ",subject_id = '" + subjectId + '\'' +
                        ",teacher_id = '" + teacherId + '\'' +
                        ",class_id = '" + classId + '\'' +
                        ",description = '" + description + '\'' +
                        ",title = '" + title + '\'' +
                        ",remarks = '" + remarks + '\'' +
                        "}";
    }
}