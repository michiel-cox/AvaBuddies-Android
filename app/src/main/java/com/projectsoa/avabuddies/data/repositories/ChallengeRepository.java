package com.projectsoa.avabuddies.data.repositories;

import com.projectsoa.avabuddies.data.models.Challenge;
import com.projectsoa.avabuddies.data.models.responses.challengee.ChallengeListResponse;
import com.projectsoa.avabuddies.data.models.responses.challengee.ChallengeResponse;
import com.projectsoa.avabuddies.data.services.ChallengeService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class ChallengeRepository {
    private ChallengeService challengeService;

    public ChallengeRepository(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    public Single<List<Challenge>> getList(){
        return challengeService.fetchList().map(ChallengeListResponse -> {
            List<Challenge> challenges = new ArrayList<>();
            for(ChallengeResponse challenge : ChallengeListResponse.Challenges){
                challenges.add(new Challenge(challenge));
            }
            return challenges;
        });
    }

}
