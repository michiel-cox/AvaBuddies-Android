package com.projectsoa.avabuddies.data.models.responses.user;

import com.google.gson.annotations.SerializedName;
import com.projectsoa.avabuddies.data.models.Tag;

import java.util.List;

public class UserResponse {
    @SerializedName("name")
    public String name;

    @SerializedName("sharelocation")
    public boolean sharelocation;

    @SerializedName("isAdmin")
    public boolean isAdmin;

    @SerializedName("isPrivate")
    public boolean isPrivate;

    @SerializedName("email")
    public String email;

    @SerializedName("image")
    public String image;

    @SerializedName("_id")
    public String _id;

    @SerializedName("password")
    public String password;

    @SerializedName("aboutme")
    public String aboutme;

    @SerializedName("tags")
    public List<Tag> tags;


}
