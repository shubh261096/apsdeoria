package com.pb.apszone.service.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ClassDetailResponseModel {

    /*{
        "error": false,
            "message": "Details Fetched successfully",
            "class_detail": [
        {
            "class_id": {
            "id": "C101",
                    "name": "Play Group",
                    "students": [
            {
                "id": "APS102",
                    "fullname": "Shubham Agrawal"
            },
            {
                "id": "APS104",
                    "fullname": "Nitu Sharma"
            }
                ]
        }
        },
        {
            "class_id": {
            "id": "C107",
                    "name": "Class 3",
                    "students": null
        }
        }
    ]
    }*/

    @SerializedName("class_detail")
    private List<ClassDetailItem> classDetail;

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    public void setClassDetail(List<ClassDetailItem> classDetail) {
        this.classDetail = classDetail;
    }

    public List<ClassDetailItem> getClassDetail() {
        return classDetail;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isError() {
        return error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return
                "ClassDetailResponseModel{" +
                        "class_detail = '" + classDetail + '\'' +
                        ",error = '" + error + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}