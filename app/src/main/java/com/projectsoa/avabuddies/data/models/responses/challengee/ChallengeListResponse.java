package com.projectsoa.avabuddies.data.models.responses.challengee;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChallengeListResponse {

    @SerializedName("Challanges")
    public List<ChallengeResponse> Challenges;

}
