package com.projectsoa.avabuddies.core.dagger.builders;

import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
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
        return new UserRepository(userService); }

}
