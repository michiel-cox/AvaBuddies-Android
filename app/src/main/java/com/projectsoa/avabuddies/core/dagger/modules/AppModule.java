package com.projectsoa.avabuddies.core.dagger.modules;

import android.content.Context;

import com.projectsoa.avabuddies.App;
import com.projectsoa.avabuddies.utils.Utils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ViewModelModule.class})
public class AppModule {

    @Singleton
    @Provides
    public Context provideContext(App application) {
        return application;
    }

    @Singleton
    @Provides
    public Utils provideUtils(Context context) {
        return new Utils(context);
    }
    /*
     */
}
