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

    @SerializedName("admission_fee")
    private String admissionFee;

    @SerializedName("annual_fee")
    private String annualFee;

    @SerializedName("activity_fee")
    private String activityFee;

    @SerializedName("computer_fee")
    private String computerFee;

    public String getAdmissionFee() {
        return admissionFee;
    }

    public void setAdmissionFee(String admissionFee) {
        this.admissionFee = admissionFee;
    }

    public String getAnnualFee() {
        return annualFee;
    }

    public void setAnnualFee(String annualFee) {
        this.annualFee = annualFee;
    }

    public String getActivityFee() {
        return activityFee;
    }

    public void setActivityFee(String activityFee) {
        this.activityFee = activityFee;
    }

    public String getComputerFee() {
        return computerFee;
    }

    public void setComputer_fee(String computer_fee) {
        this.computerFee = computer_fee;
    }

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
                        ",admission_fee = '" + admissionFee + '\'' +
                        ",annual_fee = '" + annualFee + '\'' +
                        ",activity_fee = '" + activityFee + '\'' +
                        ",computer_fee = '" + computerFee + '\'' +
                        "}";
    }
}