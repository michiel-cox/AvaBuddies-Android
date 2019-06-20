package com.projectsoa.avabuddies.data.models.responses.user;

import com.google.gson.annotations.SerializedName;
import com.projectsoa.avabuddies.data.models.responses.tag.TagListResponse;

import java.util.List;

public class UserListResponse {

    @SerializedName("users")
    public List<UserResponse> users;
}
