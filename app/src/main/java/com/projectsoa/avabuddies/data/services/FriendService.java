package com.projectsoa.avabuddies.data.services;

import com.projectsoa.avabuddies.data.models.responses.MessageResponse;
import com.projectsoa.avabuddies.data.models.responses.friend.ConnectionsResponse;
import com.projectsoa.avabuddies.data.models.responses.friend.FriendsResponse;
import com.projectsoa.avabuddies.data.models.responses.friend.RequestsResponse;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface FriendService {

    @GET("friend/requests")
    Single<RequestsResponse> fetchRequests();

    @GET("friend/allconnections")
    Single<ConnectionsResponse> fetchConnections();

    @POST("friend/request")
    @FormUrlEncoded
    Single<MessageResponse> doRequest(@Field("friend") String friendId);

    @GET("friend/friends")
    Single<FriendsResponse> fetchFriends();

    @POST("friend/acceptrequest")
    @FormUrlEncoded
    Single<MessageResponse> doAcceptRequest(@Field("friend") String friendId);


    @POST("friend/denyrequest")
    @FormUrlEncoded
    Single<MessageResponse> doDenyRequest(@Field("friend") String friendId);

    @POST("friend/cancelrequest")
    @FormUrlEncoded
    Single<MessageResponse> doCancelRequest(@Field("friend") String friendId);

    @POST("friend/validaterequest")
    @FormUrlEncoded
    Single<MessageResponse> doValidateRequest(@Field("friend") String friendId);



}
