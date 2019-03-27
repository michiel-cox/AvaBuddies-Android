package com.projectsoa.avabuddies.data;

import com.projectsoa.avabuddies.data.repositories.LoginRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public abstract class DataModule {

    @Singleton
    @Provides
    static Retrofit provideRetrofit() {
        return new Retrofit.Builder().baseUrl("<de backend api URL>")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    @Singleton
    @Provides
    static LoginRepository provideLoginRepository() { return new LoginRepository(); }
}
