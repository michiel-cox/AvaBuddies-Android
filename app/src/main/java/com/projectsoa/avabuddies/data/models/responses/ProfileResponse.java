package com.projectsoa.avabuddies.data.models.responses;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
    @SerializedName("message")
    public String message;

    @SerializedName("user")
    public String user;

    public class User {

        @SerializedName("_id")
        public String id;

        @SerializedName("email")
        public String email;

    }
}
