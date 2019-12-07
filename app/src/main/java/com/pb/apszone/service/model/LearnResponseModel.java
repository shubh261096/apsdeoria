package com.pb.apszone.service.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class LearnResponseModel {

    /*{
    "error": false,
    "message": "Videos fetched successfully",
    "learn": [
        {
            "id": "S101",
            "name": "MY First ABC",
            "video": [
                {
                    "id": "1",
                    "title": "This is my first youTube Video of Subject 101",
                    "video_url": "http://ewe.com/",
                    "subject_id": "S101"
                },
                {
                    "id": "2",
                    "title": "This is my second youTube Video of Subject 101",
                    "video_url": "www.you.url",
                    "subject_id": "S101"
                }
            ]
        },
        {
            "id": "S102",
            "name": "Number Book 1-20",
            "video": null
        },
        {
            "id": "S103",
            "name": "Aksharmala",
            "video": null
        },
        {
            "id": "S104",
            "name": "Rhymes",
            "video": null
        },
        {
            "id": "S105",
            "name": "Akshar Lekhan",
            "video": null
        },
        {
            "id": "S106",
            "name": "My First Color Book",
            "video": null
        },
        {
            "id": "S107",
            "name": "My First Writing Book",
            "video": null
        },
        {
            "id": "S108",
            "name": "My First Color Book",
            "video": null
        },
        {
            "id": "S109",
            "name": "Games",
            "video": null
        },
        {
            "id": "S110",
            "name": "Recreational Activities",
            "video": null
        }
    ]
    }*/

    @SerializedName("learn")
    private List<LearnItem> learn;

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    public void setLearn(List<LearnItem> learn) {
        this.learn = learn;
    }

    public List<LearnItem> getLearn() {
        return learn;
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
                "LearnResponseModel{" +
                        "learn = '" + learn + '\'' +
                        ",error = '" + error + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}