package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LearnItem {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    @SerializedName("video")
    private List<VideoItem> video;

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

    public void setVideo(List<VideoItem> video) {
        this.video = video;
    }

    public List<VideoItem> getVideo() {
        return video;
    }

    @Override
    public String toString() {
        return
                "LearnItem{" +
                        "name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",video = '" + video + '\'' +
                        "}";
    }
}