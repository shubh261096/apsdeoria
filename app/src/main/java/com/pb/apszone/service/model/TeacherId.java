package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

public class TeacherId {

    @SerializedName("id")
    private String id;

    @SerializedName("fullname")
    private String fullname;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }

    @Override
    public String toString() {
        return
                "TeacherId{" +
                        "id = '" + id + '\'' +
                        ",fullname = '" + fullname + '\'' +
                        "}";
    }
}