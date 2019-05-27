package com.projectsoa.avabuddies.data.services;

import com.projectsoa.avabuddies.data.models.responses.user.ProfileResponse;
import com.projectsoa.avabuddies.data.models.responses.user.UserListResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

    @GET("user/{profile}")
    Single<ProfileResponse> fetchProfile();

    @GET("user/{id}")
    Single<ProfileResponse> fetchUser(@Path("id") String id);

    @GET("user/")
    Single<UserListResponse> fetchList();

    @DELETE("destroy/{id}")
    Completable deleteUser(@Path("id") String id);

    @POST("user/updateprofile")
    @FormUrlEncoded
    Completable updateProfile(@Field("aboutme") String aboutMe, @Field("sharelocation") boolean sharelocation, @Field("isPrivate") boolean isPrivate, @Field("tags") List<String> tags);

    @POST("user/updateprofilepicture")
    @FormUrlEncoded
    Completable updateProfilePicture(@Field("image") String base64);

}
