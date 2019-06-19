package com.projectsoa.avabuddies.core.dagger.builders;

import com.projectsoa.avabuddies.data.repositories.ChallengeRepository;
import com.projectsoa.avabuddies.data.repositories.FriendRepository;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.TagRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.data.services.ChallengeService;
import com.projectsoa.avabuddies.data.services.FriendService;
import com.projectsoa.avabuddies.data.services.TagService;
import com.projectsoa.avabuddies.data.services.UserService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryBuilder {

    @Singleton
    @Provides
    static LoginRepository provideLoginRepository() {
        return new LoginRepository();
    }

    @Singleton
    @Provides
    static UserRepository provideUserRepository(UserService userService) {
        return new UserRepository(userService);
    }

    @Singleton
    @Provides
    static FriendRepository provideFriendRepository(FriendService friendService, LoginRepository loginRepository, UserRepository userRepository) {
        return new FriendRepository(friendService, loginRepository, userRepository);
    }

    @Singleton
    @Provides
    static TagRepository tagRepository(TagService tagService) {
        return new TagRepository(tagService);
    }
    @Singleton
    @Provides
    static ChallengeRepository ChallengeRepository(ChallengeService challengeService) {
        return new ChallengeRepository(challengeService);
    }


}
