package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

public class FeesId {

    @SerializedName("year")
    private String year;

    @SerializedName("total_amount")
    private String totalAmount;

    @SerializedName("class_id")
    private String classId;

    @SerializedName("id")
    private String id;

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassId() {
        return classId;
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
                "FeesId{" +
                        "year = '" + year + '\'' +
                        ",total_amount = '" + totalAmount + '\'' +
                        ",class_id = '" + classId + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}