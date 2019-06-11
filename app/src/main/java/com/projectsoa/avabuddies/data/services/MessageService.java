package com.projectsoa.avabuddies.data.services;

import com.projectsoa.avabuddies.data.models.responses.chat.MessageListResponse;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface MessageService {
    @GET("messages")
    Single<MessageListResponse> fetchList();
}
