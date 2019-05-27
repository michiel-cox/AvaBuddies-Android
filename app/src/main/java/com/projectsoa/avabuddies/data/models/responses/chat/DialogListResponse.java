package com.projectsoa.avabuddies.data.models.responses.chat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DialogListResponse {
    @SerializedName("chats")
    public List<DialogResponse> dialogs;
}
