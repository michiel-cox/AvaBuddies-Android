package com.projectsoa.avabuddies.data.models.responses;

import com.google.gson.annotations.SerializedName;
import com.projectsoa.avabuddies.data.models.responses.UserResponse;

public class SignupResponse {
    @SerializedName("message")
    public String message;

    @SerializedName("user")
    public UserResponse user;
}
