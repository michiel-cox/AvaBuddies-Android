package com.projectsoa.avabuddies.data.services;

import com.projectsoa.avabuddies.data.models.responses.MessageResponse;
import com.projectsoa.avabuddies.data.models.responses.chat.DialogListResponse;
import com.projectsoa.avabuddies.data.models.responses.chat.DialogResponse;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DialogService {
    @GET("chats")
    Single<DialogListResponse> fetchList();

    @POST("chats/{id}")
    Completable addChat(@Path("id") String id);
}
