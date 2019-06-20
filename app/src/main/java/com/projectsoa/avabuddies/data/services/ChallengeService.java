package com.projectsoa.avabuddies.data.services;

import com.projectsoa.avabuddies.data.models.responses.challengee.ChallengeListResponse;
import com.projectsoa.avabuddies.data.models.responses.challengee.ChallengeResponse;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface ChallengeService {

    @GET("challenges/")
    Single<ChallengeListResponse> fetchList();

    @GET("challenges/{id}")
    Single<ChallengeResponse> getChallenge();

}
