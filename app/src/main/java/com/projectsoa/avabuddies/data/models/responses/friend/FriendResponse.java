package com.projectsoa.avabuddies.data.models.responses.friend;

import com.google.gson.annotations.SerializedName;

public class FriendResponse {
    @SerializedName("user")
    public String friend1;

    @SerializedName("friend")
    public String friend2;

    @SerializedName("confirmed")
    public boolean confirmed;

    @SerializedName("validated")
    public boolean validated;
}
