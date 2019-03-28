package com.projectsoa.avabuddies.core.dagger.builders;

import com.projectsoa.avabuddies.data.repositories.LoginRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryBuilder {

    @Singleton
    @Provides
    static LoginRepository provideLoginRepository() {
        return new LoginRepository(); }

}
