package com.pb.apszone.service.rest;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class FeedbackRequestModel {

    /* Feedback Request Body
        {
            "feedback": [
                {
                    "student_id": "APS110",
                    "teacher_id": "APST165",
                    "rating": 5,
                    "feedback": "Awesome"
                },
                {
                    "student_id": "APS110",
                    "teacher_id": "APST165",
                    "rating": 5,
                    "feedback": "Awesome"
                }
            ]
        }
    */
    @SerializedName("feedback")
    private List<FeedbackItem> feedback;

    public void setFeedback(List<FeedbackItem> feedback) {
        this.feedback = feedback;
    }

    public List<FeedbackItem> getFeedback() {
        return feedback;
    }

    @Override
    public String toString() {
        return
                "FeedbackRequestModel{" +
                        "feedback = '" + feedback + '\'' +
                        "}";
    }
}