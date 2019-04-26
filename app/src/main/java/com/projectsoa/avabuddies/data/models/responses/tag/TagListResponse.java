package com.projectsoa.avabuddies.data.models.responses.tag;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TagListResponse {

    @SerializedName("tags")
    public List<TagResponse> tags;

}
