package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

public class SyllabusItem {

    @SerializedName("syllabus")
    private String syllabus;

    @SerializedName("class_id")
    private String classId;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("id")
    private String id;

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassId() {
        return classId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return
                "SyllabusItem{" +
                        "syllabus = '" + syllabus + '\'' +
                        ",class_id = '" + classId + '\'' +
                        ",name = '" + name + '\'' +
                        ",description = '" + description + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}