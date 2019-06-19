package com.projectsoa.avabuddies.data.services;

import com.projectsoa.avabuddies.data.models.responses.MessageResponse;
import com.projectsoa.avabuddies.data.models.responses.friend.ConnectionsResponse;
import com.projectsoa.avabuddies.data.models.responses.friend.FriendsResponse;
import com.projectsoa.avabuddies.data.models.responses.friend.RequestsResponse;

import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FriendService {

    @GET("friends/{id}")
    Single<RequestsResponse> fetchRequests(@Path("id") String id);

    @GET("friends/")
    Single<ConnectionsResponse> fetchConnections();

    @POST("friends/")
    @FormUrlEncoded
    Single<MessageResponse> doRequest(@Field("id") String friendId);

    @GET("friends/")
    Single<FriendsResponse> fetchFriends();

    @PUT("friends/{id}")
    @FormUrlEncoded
    Single<MessageResponse> doAcceptRequest(@Path("id") String friendId, @Field("type") String type);

    @DELETE("friends/{id}")
    Single<MessageResponse> doDenyRequest(@Path("id") String id);

    @DELETE("friends/{id}")
    Single<MessageResponse> doCancelRequest(@Path("id") String id);

    @PUT("friends/{id}")
    @FormUrlEncoded
    Single<MessageResponse> doValidateRequest(@Path("id") String friendId, @Field("type") String type);



}
