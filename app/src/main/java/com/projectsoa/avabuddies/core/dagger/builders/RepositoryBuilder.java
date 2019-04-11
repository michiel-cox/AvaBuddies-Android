package com.projectsoa.avabuddies.core.dagger.builders;

import com.projectsoa.avabuddies.data.repositories.FriendRepository;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.data.services.FriendService;
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

}
