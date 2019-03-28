package com.projectsoa.avabuddies.data.services;

import com.projectsoa.avabuddies.data.models.responses.LoginResponse;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthService {

    @POST("auth/login")
    @FormUrlEncoded
    Single<LoginResponse> doLogin(@Field("email") String email, @Field("password") String password);

    @POST("auth/signup")
    @FormUrlEncoded
    Single<LoginResponse> doSignup(@Field("email") String email, @Field("password") String password);
}
