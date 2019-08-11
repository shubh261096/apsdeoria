package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

public class SubjectId {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    @SerializedName("syllabus")
    private String syllabus;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    @Override
    public String toString() {
        return
                "SubjectId{" +
                        "name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",syllabus = '" + syllabus + '\'' +
                        "}";
    }
}