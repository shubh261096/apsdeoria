package com.pb.apszone.service.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class VideoItem implements Parcelable {

    @SerializedName("subject_id")
    private String subjectId;

    @SerializedName("video_url")
    private String videoUrl;

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    private VideoItem(Parcel in) {
        subjectId = in.readString();
        videoUrl = in.readString();
        id = in.readString();
        title = in.readString();
    }

    public static final Creator<VideoItem> CREATOR = new Creator<VideoItem>() {
        @Override
        public VideoItem createFromParcel(Parcel in) {
            return new VideoItem(in);
        }

        @Override
        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return
                "VideoItem{" +
                        "subject_id = '" + subjectId + '\'' +
                        ",video_url = '" + videoUrl + '\'' +
                        ",id = '" + id + '\'' +
                        ",title = '" + title + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subjectId);
        dest.writeString(videoUrl);
        dest.writeString(id);
        dest.writeString(title);
    }
}