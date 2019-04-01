package com.projectsoa.avabuddies.data.models.responses;

import com.google.gson.annotations.SerializedName;
import com.projectsoa.avabuddies.data.repositories.UserRepository;

public class ProfileResponse {

    @SerializedName("user")
    public UserResponse user;
}
