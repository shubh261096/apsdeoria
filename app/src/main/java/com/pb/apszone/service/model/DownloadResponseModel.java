package com.pb.apszone.service.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class DownloadResponseModel {

    @SerializedName("download")
    private List<DownloadItem> download;

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    public void setDownload(List<DownloadItem> download) {
        this.download = download;
    }

    public List<DownloadItem> getDownload() {
        return download;
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
                "DownloadResponseModel{" +
                        "download = '" + download + '\'' +
                        ",error = '" + error + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}