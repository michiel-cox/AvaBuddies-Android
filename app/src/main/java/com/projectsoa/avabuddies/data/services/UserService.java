package com.projectsoa.avabuddies.data.services;

import com.projectsoa.avabuddies.data.models.responses.user.ProfileResponse;
import com.projectsoa.avabuddies.data.models.responses.user.UserListResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserService {

    @GET("user/profile")
    Single<ProfileResponse> fetchProfile();

    @GET("user/user/{id}")
    Single<ProfileResponse> fetchUser(@Path("id") String id);

    @GET("user/list")
    Single<UserListResponse> fetchList();
}
