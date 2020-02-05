package com.pb.apszone.service.rest.model;

import com.google.gson.annotations.SerializedName;

public class ProfileRequestModel {

    @SerializedName("id")
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return
                "ProfileRequestModel{" +
                        "id = '" + id + '\'' +
                        "}";
    }
}