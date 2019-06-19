package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

public class HomeworkItem {

    @SerializedName("date")
    private String date;

    @SerializedName("subject_id")
    private SubjectId subjectId;

    @SerializedName("data")
    private String data;

    @SerializedName("teacher_id")
    private TeacherId teacherId;

    @SerializedName("class_id")
    private ClassId classId;

    @SerializedName("id")
    private String id;

    @SerializedName("remarks")
    private String remarks;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setSubjectId(SubjectId subjectId) {
        this.subjectId = subjectId;
    }

    public SubjectId getSubjectId() {
        return subjectId;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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
                "HomeworkItem{" +
                        "date = '" + date + '\'' +
                        ",subject_id = '" + subjectId + '\'' +
                        ",data = '" + data + '\'' +
                        ",teacher_id = '" + teacherId + '\'' +
                        ",class_id = '" + classId + '\'' +
                        ",id = '" + id + '\'' +
                        ",remarks = '" + remarks + '\'' +
                        "}";
    }
}