package com.pb.apszone.service.rest;

import com.google.gson.annotations.SerializedName;

public class FeedbackItem {

    @SerializedName("feedback")
    private String feedback;

    @SerializedName("teacher_id")
    private String teacherId;

    @SerializedName("rating")
    private int rating;

    @SerializedName("student_id")
    private String studentId;

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    @Override
    public String toString() {
        return
                "FeedbackItem{" +
                        "feedback = '" + feedback + '\'' +
                        ",teacher_id = '" + teacherId + '\'' +
                        ",rating = '" + rating + '\'' +
                        ",student_id = '" + studentId + '\'' +
                        "}";
    }
}