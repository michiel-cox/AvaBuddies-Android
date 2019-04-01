package com.projectsoa.avabuddies.core.dagger.builders;

import com.projectsoa.avabuddies.data.services.AuthService;
import com.projectsoa.avabuddies.data.services.UserService;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ServiceBuilder {

    @Provides
    AuthService getAuthService(Retrofit retroFit) {
        return retroFit.create(AuthService.class);
    }

    @Provides
    UserService getUserService(Retrofit retroFit) {
        return retroFit.create(UserService.class);
    }

}
