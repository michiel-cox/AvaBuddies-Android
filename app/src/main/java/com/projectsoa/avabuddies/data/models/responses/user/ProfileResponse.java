package com.projectsoa.avabuddies.data.models.responses.user;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {

    @SerializedName("user")
    public UserResponse user;
}
