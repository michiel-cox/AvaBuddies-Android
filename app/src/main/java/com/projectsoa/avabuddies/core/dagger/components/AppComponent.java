package com.projectsoa.avabuddies.core.dagger.components;

import com.projectsoa.avabuddies.App;
import com.projectsoa.avabuddies.core.dagger.builders.ActivityBuilder;
import com.projectsoa.avabuddies.core.dagger.modules.AppModule;
import com.projectsoa.avabuddies.core.dagger.modules.RepositoryModule;
import com.projectsoa.avabuddies.core.dagger.modules.ServiceModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        ActivityBuilder.class,
        ServiceModule.class,
        RepositoryModule.class
})
public interface AppComponent extends AndroidInjector<App> {
    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<App> {}
}

