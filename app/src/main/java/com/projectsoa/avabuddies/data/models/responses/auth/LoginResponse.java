package com.projectsoa.avabuddies.data.models.responses.auth;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    public String token;
}
