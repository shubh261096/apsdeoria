package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

public class TeacherId {

    @SerializedName("id")
    private String id;

    @SerializedName("fullname")
    private String fullname;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }

    /* These keys are used in FeedbackFragment*/
    @SerializedName("feedback")
    private String feedback;

    @SerializedName("rating")
    private int rating;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return
                "TeacherId{" +
                        "id = '" + id + '\'' +
                        ",fullname = '" + fullname + '\'' +
                        ",rating = '" + rating + '\'' +
                        ",feedback = '" + feedback + '\'' +
                        "}";
    }
}