package com.pb.apszone.service.rest.model;

import com.google.gson.annotations.SerializedName;

public class SyllabusRequestModel {

    @SerializedName("class_id")
    private String classId;

    @SerializedName("subject_id")
    private String subjectId;

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassId() {
        return classId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public String toString() {
        return
                "SyllabusRequestModel{" +
                        "class_id = '" + classId + '\'' +
                        ",subject_id = '" + subjectId + '\'' +
                        "}";
    }
}