package com.pb.apszone.service.rest.model;

import com.google.gson.annotations.SerializedName;

public class SyllabusRequestModel {

    @SerializedName("student_id")
    private String studentID;

    @SerializedName("subject_id")
    private String subjectId;

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentID() {
        return studentID;
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
                        "student_id = '" + studentID + '\'' +
                        ",subject_id = '" + subjectId + '\'' +
                        "}";
    }
}