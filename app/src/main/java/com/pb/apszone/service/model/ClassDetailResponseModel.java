package com.pb.apszone.service.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ClassDetailResponseModel {

    /*{
        "error": false,
            "message": "Details Fetched successfully",
            "class_detail": [
        {
            "timetable_id": "1",
                "class_id": {
            "id": "C101",
                    "name": "Play Group",
                    "students": [
            {
                "id": "APS101",
                    "fullname": "Apoorva Pandey",
                    "attendance": null
            },
            {
                "id": "APS102",
                    "fullname": "Shubham Agrawal",
                    "attendance": {
                "date": "2019-05-08",
                        "id": "6",
                        "student_id": "APS102",
                        "status": "1",
                        "remarks": "Present"
            }
            },
            {
                "id": "APS104",
                    "fullname": "Nitu Sharma",
                    "attendance": {
                "date": "2019-05-08",
                        "id": "7",
                        "student_id": "APS104",
                        "status": "1",
                        "remarks": "Present"
            }
            }
                ]
        }
        },
        {
            "timetable_id": "13",
                "class_id": {
            "id": "C107",
                    "name": "Class 3",
                    "students": [
            {
                "id": "APS103",
                    "fullname": "Vishal Agrawal",
                    "attendance": null
            }
                ]
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