package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

public class ClassSubjectItem {

    @SerializedName("class_id")
    private ClassId classId;

    public void setClassId(ClassId classId) {
        this.classId = classId;
    }

    public ClassId getClassId() {
        return classId;
    }

    @Override
    public String toString() {
        return
                "ClassSubjectItem{" +
                        "class_id = '" + classId + '\'' +
                        "}";
    }
}