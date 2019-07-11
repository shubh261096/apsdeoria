package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

public class CommonResponseModel {

    /*{
        "error": false,
            "message": "Added successfully"
    }*/

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

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
                "CommonResponseModel{" +
                        "error = '" + error + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}