package com.projectsoa.avabuddies.data.models.responses.chat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageListResponse {
    @SerializedName("messages")
    public List<MessageResponse> messages;
}
