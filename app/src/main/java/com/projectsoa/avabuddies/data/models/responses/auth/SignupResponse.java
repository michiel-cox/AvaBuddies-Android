package com.projectsoa.avabuddies.data.models.responses.auth;

import com.google.gson.annotations.SerializedName;
import com.projectsoa.avabuddies.data.models.responses.MessageResponse;
import com.projectsoa.avabuddies.data.models.responses.user.UserResponse;

public class SignupResponse extends MessageResponse {

    @SerializedName("user")
    public UserResponse user;
}
