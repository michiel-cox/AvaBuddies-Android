package com.projectsoa.avabuddies.core.dagger.modules;

import com.projectsoa.avabuddies.core.dagger.builders.ViewModelBuilder;
import com.projectsoa.avabuddies.core.dagger.utils.ViewModelFactory;
import com.projectsoa.avabuddies.core.dagger.utils.ViewModelKey;
import com.projectsoa.avabuddies.screens.login.LoginViewModel;
import com.projectsoa.avabuddies.screens.main.MainViewModel;

import javax.inject.Singleton;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module(includes = ViewModelBuilder.class)
public abstract class ViewModelModule {

    @Binds
    @Singleton
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}