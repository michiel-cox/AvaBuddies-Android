package com.projectsoa.avabuddies.data.models.responses.chat;

import com.google.gson.annotations.SerializedName;
import com.projectsoa.avabuddies.data.models.User;

public class DialogResponse {
    @SerializedName("_id")
    public String _id;

    @SerializedName("user1")
    public User user1;

    @SerializedName("user2")
    public User user2;
}
