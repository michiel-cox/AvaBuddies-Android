package com.projectsoa.avabuddies.data.models.responses.chat;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MessageResponse {
    @SerializedName("id")
    public String id;

    @SerializedName("chatId")
    public String chatId;

    @SerializedName("senderId")
    public String senderId;

    @SerializedName("message")
    public String message;

    @SerializedName("dateTime")
    public Date dateTime;
}
