package com.projectsoa.avabuddies.data.services;

import com.projectsoa.avabuddies.data.models.responses.LoginResponse;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface UserService {

    @POST("user/profile")
    Single<LoginResponse> fetchProfile(@Field("email") String email, @Field("password") String password);
}
