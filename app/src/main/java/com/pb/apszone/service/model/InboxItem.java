package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

public class InboxItem {

    @SerializedName("date")
    private String date;

    @SerializedName("send_to")
    private String sendTo;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("action")
    private String action;

    @SerializedName("topic")
    private String topic;

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("message")
    private String message;

    @SerializedName("firebase_token")
    private String firebaseToken;

    @SerializedName("action_destination")
    private String actionDestination;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
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

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setActionDestination(String actionDestination) {
        this.actionDestination = actionDestination;
    }

    public String getActionDestination() {
        return actionDestination;
    }

    @Override
    public String toString() {
        return
                "InboxItem{" +
                        "date = '" + date + '\'' +
                        ",send_to = '" + sendTo + '\'' +
                        ",image_url = '" + imageUrl + '\'' +
                        ",action = '" + action + '\'' +
                        ",topic = '" + topic + '\'' +
                        ",id = '" + id + '\'' +
                        ",title = '" + title + '\'' +
                        ",message = '" + message + '\'' +
                        ",firebase_token = '" + firebaseToken + '\'' +
                        ",action_destination = '" + actionDestination + '\'' +
                        "}";
    }
}