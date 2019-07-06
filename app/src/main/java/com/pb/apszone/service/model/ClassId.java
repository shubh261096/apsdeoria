package com.pb.apszone.service.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ClassId {

    @SerializedName("subject_id")
    private List<SubjectId> subjectId;

    @SerializedName("name")
    private String name;

    @SerializedName("students")
    private List<StudentsItem> students;

    @SerializedName("id")
    private String id;

    public void setSubjectId(List<SubjectId> subjectId) {
        this.subjectId = subjectId;
    }

    public List<SubjectId> getSubjectId() {
        return subjectId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStudents(List<StudentsItem> students) {
        this.students = students;
    }

    public List<StudentsItem> getStudents() {
        return students;
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
                "ClassId{" +
                        "subject_id = '" + subjectId + '\'' +
                        ",name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}