package com.projectsoa.avabuddies.core.dagger.modules;

import com.projectsoa.avabuddies.Constants;
import com.projectsoa.avabuddies.core.dagger.builders.ServiceBuilder;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ServiceBuilder.class)
public class ServiceModule {

    @Singleton
    @Provides
    static Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder().baseUrl(Constants.API_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    static OkHttpClient provideOkHttpClient(LoginRepository loginRepository) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();


        clientBuilder.addInterceptor(chain -> {
            okhttp3.Request request = chain.request();

            Headers.Builder headersBuilder = request.headers().newBuilder();
            if (loginRepository.isLoggedIn()) {
                headersBuilder.add("Authorization", String.format("Bearer %s", loginRepository.getLoggedInUser().getToken())).build();
            }

            request = request.newBuilder().headers(headersBuilder.build()).build();
            return chain.proceed(request);
        });

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientBuilder.addInterceptor(interceptor);

        return clientBuilder.build();
    }
}
