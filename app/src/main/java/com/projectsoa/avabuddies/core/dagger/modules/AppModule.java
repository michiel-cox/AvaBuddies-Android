package com.projectsoa.avabuddies.core.dagger.modules;

import android.content.Context;

import com.projectsoa.avabuddies.App;
import com.projectsoa.avabuddies.core.dagger.builders.ViewModelBuilder;
import com.projectsoa.avabuddies.utils.Utils;

import javax.inject.Singleton;

import androidx.lifecycle.ViewModel;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = { ViewModelModule.class })
public class AppModule {

    @Singleton
    @Provides
    Context provideContext(App application){
        return application;
    }

    @Singleton
    @Provides
    Utils provideUtils(Context context){
        return new Utils(context);
    }
/*
*/
}
