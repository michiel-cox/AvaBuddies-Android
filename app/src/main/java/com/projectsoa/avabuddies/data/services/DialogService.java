package com.projectsoa.avabuddies.data.services;

import com.projectsoa.avabuddies.data.models.responses.chat.DialogListResponse;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface DialogService {
    @GET("chat/")
    Single<DialogListResponse> fetchList();
}
