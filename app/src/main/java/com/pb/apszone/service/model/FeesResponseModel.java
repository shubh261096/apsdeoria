package com.pb.apszone.service.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class FeesResponseModel {

    /* Fee response
    {
        "error": false,
            "message": "Fees fetched successfully",
            "fees": [
        {
            "fees_id": {
            "id": "F1",
                    "year": "2019",
                    "class_id": "C102",
                    "total_amount": "700"
        },
            "student_id": "APS101",
                "due_amount": "200",
                "fees_paid": "900",
                "period": "January",
                "date_paid": "2019-01-01",
                "status": "paid"
        },
        {
            "fees_id": {
            "id": "F1",
                    "year": "2019",
                    "class_id": "C102",
                    "total_amount": "700"
        },
            "student_id": "APS101",
                "due_amount": "0",
                "fees_paid": "700",
                "period": "June",
                "date_paid": "2019-06-21",
                "status": "paid"
        }
    ]
    }
    */

    @SerializedName("fees")
    private List<FeesItem> fees;

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    public void setFees(List<FeesItem> fees) {
        this.fees = fees;
    }

    public List<FeesItem> getFees() {
        return fees;
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
                "FeesResponseModel{" +
                        "fees = '" + fees + '\'' +
                        ",error = '" + error + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}