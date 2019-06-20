package com.projectsoa.avabuddies.data.models.responses.friend;

import com.google.gson.annotations.SerializedName;
import com.projectsoa.avabuddies.data.models.responses.MessageResponse;

import java.util.List;

public class ConnectionsResponse extends MessageResponse {

    @SerializedName("friends")
    public List<FriendResponse> connections;

}
