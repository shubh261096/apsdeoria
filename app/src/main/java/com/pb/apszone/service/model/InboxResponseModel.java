package com.pb.apszone.service.model;

import java.util.List;


import com.google.gson.annotations.SerializedName;

public class InboxResponseModel {

    /* Inbox Response
    {
    "error": false,
    "message": "Inbox Fetched Successfully",
    "inbox": [
        {
            "id": "5",
            "title": "This is a test from real server",
            "message": "Hello Test Server",
            "image_url": "",
            "action": "",
            "action_destination": "",
            "send_to": "topic",
            "firebase_token": "",
            "topic": "global",
            "date": "2019-06-23 23:56:39"
        },
        {
            "id": "4",
            "title": "Hello",
            "message": "Hello Shubham",
            "image_url": "",
            "action": "",
            "action_destination": "",
            "send_to": "topic",
            "firebase_token": "",
            "topic": "global",
            "date": "2019-06-23 23:53:48"
        },
        {
            "id": "3",
            "title": "h",
            "message": "h",
            "image_url": "",
            "action": "",
            "action_destination": "",
            "send_to": "topic",
            "firebase_token": "",
            "topic": "global",
            "date": "2019-06-23 11:42:25"
        },
        {
            "id": "2",
            "title": "Hello",
            "message": "Hello",
            "image_url": "",
            "action": "",
            "action_destination": "",
            "send_to": "topic",
            "firebase_token": "",
            "topic": "global",
            "date": "2019-06-23 08:11:55"
        },
        {
            "id": "1",
            "title": "Hello",
            "message": "Hello",
            "image_url": "",
            "action": "",
            "action_destination": "",
            "send_to": "topic",
            "firebase_token": "",
            "topic": "global",
            "date": "2019-06-23 08:09:33"
        }
       ]
     }
    */

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("inbox")
    private List<InboxItem> inbox;

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

    public void setInbox(List<InboxItem> inbox) {
        this.inbox = inbox;
    }

    public List<InboxItem> getInbox() {
        return inbox;
    }

    @Override
    public String toString() {
        return
                "InboxResponseModel{" +
                        "error = '" + error + '\'' +
                        ",message = '" + message + '\'' +
                        ",inbox = '" + inbox + '\'' +
                        "}";
    }
}