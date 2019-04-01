package com.projectsoa.avabuddies.data.models.responses;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("name")
    public String name;

    @SerializedName("sharelocation")
    public boolean sharelocation;

    @SerializedName("isAdmin")
    public boolean isAdmin;

    @SerializedName("email")
    public String email;

    @SerializedName("image")
    public String image;

    @SerializedName("_id")
    public String _id;

    @SerializedName("password")
    public String password;
}
