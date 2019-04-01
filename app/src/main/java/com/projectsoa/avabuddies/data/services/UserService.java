package com.projectsoa.avabuddies.data.services;

import com.projectsoa.avabuddies.data.models.responses.ProfileResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {

    @GET("user/profile")
    Single<ProfileResponse> fetchProfile();
}
