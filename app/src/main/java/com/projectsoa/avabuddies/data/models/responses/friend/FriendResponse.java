package com.projectsoa.avabuddies.data.models.responses.friend;

import com.google.gson.annotations.SerializedName;

public class FriendResponse {
    @SerializedName("friend1")
    public String friend1;

    @SerializedName("friend2")
    public String friend2;

    @SerializedName("confirmed")
    public boolean confirmed;
}
