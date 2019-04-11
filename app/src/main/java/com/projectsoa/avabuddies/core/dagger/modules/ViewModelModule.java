package com.projectsoa.avabuddies.core.dagger.modules;

import com.projectsoa.avabuddies.core.dagger.builders.ViewModelBuilder;
import com.projectsoa.avabuddies.core.dagger.utils.ViewModelFactory;

import javax.inject.Singleton;

import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;

@Module(includes = ViewModelBuilder.class)
public abstract class ViewModelModule {

    @Binds
    @Singleton
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}