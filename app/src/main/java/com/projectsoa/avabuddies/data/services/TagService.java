package com.projectsoa.avabuddies.data.services;

import com.projectsoa.avabuddies.data.models.responses.tag.TagListResponse;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface TagService {

    @GET("tags/")
    Single<TagListResponse> fetchList();

}
