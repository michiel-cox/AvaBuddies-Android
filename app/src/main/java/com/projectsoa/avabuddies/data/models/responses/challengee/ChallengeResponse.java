package com.projectsoa.avabuddies.data.models.responses.challengee;

import com.google.gson.annotations.SerializedName;

public class ChallengeResponse {

    @SerializedName("title")
    public String name;

    @SerializedName("_id")
    public String _id;

    @SerializedName("description")
    public String description;

    @SerializedName("image")
    public String image;


}
