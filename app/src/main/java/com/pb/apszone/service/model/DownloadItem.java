package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

public class DownloadItem {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private String type;

    @SerializedName("url")
    private String url;

    @SerializedName("date_added")
    private String dateAdded;

    @SerializedName("status")
    private String status;

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return
                "DownloadItem{" +
                        "name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",type = '" + type + '\'' +
                        ",url = '" + url + '\'' +
                        ",date_added = '" + dateAdded + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}