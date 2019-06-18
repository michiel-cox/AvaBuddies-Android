package com.projectsoa.avabuddies.data.services;

import com.projectsoa.avabuddies.data.models.responses.user.UserListResponse;
import com.projectsoa.avabuddies.data.models.responses.user.UserResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @GET("users/profile")
    Single<UserResponse> fetchProfile();

    @GET("users/{id}")
    Single<UserResponse> fetchUser(@Path("id") String id);

    @GET("users/")
    Single<UserListResponse> fetchList();

    @DELETE("users/{id}")
    Completable deleteUser(@Path("id") String id);

    @PUT("users/{id}")
    @FormUrlEncoded
    Completable updateProfile(@Field("aboutme") String aboutMe, @Field("sharelocation") boolean sharelocation, @Field("isPrivate") boolean isPrivate, @Field("tags") List<String> tags);

    @PUT("users/{id}")
    @FormUrlEncoded
    Completable updateProfilePicture(@Field("image") String base64);

}
