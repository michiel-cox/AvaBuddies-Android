package com.projectsoa.avabuddies.data.services;

import com.projectsoa.avabuddies.data.models.responses.LoginResponse;
import com.projectsoa.avabuddies.data.models.responses.SignupResponse;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthService {

    @POST("auth/login")
    @FormUrlEncoded
    Single<LoginResponse> doLogin(@Field("email") String email, @Field("password") String password);

    @POST("auth/signup")
    @FormUrlEncoded
    Single<SignupResponse> doSignup(@Field("email") String email, @Field("password") String password, @Field("name") String name, @Field("sharelocation") boolean sharelocation);
}
