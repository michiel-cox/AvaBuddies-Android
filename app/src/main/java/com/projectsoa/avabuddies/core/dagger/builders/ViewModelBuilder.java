package com.projectsoa.avabuddies.core.dagger.builders;

import com.projectsoa.avabuddies.core.dagger.utils.ViewModelKey;
import com.projectsoa.avabuddies.screens.login.LoginViewModel;
import com.projectsoa.avabuddies.screens.main.MainViewModel;
import com.projectsoa.avabuddies.screens.main.profile.ProfileFragment;
import com.projectsoa.avabuddies.screens.main.profile.ProfileViewModel;
import com.projectsoa.avabuddies.screens.main.publicprofile.PublicProfileFragment;
import com.projectsoa.avabuddies.screens.main.publicprofile.PublicProfileViewModel;
import com.projectsoa.avabuddies.screens.register.RegisterViewModel;

import javax.inject.Singleton;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Put all the ViewModels in here
 */
@Module
public abstract class ViewModelBuilder {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindLoginViewModel(LoginViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    abstract ViewModel bindProfileViewModel(ProfileViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel.class)
    abstract ViewModel bindRegisterViewModel(RegisterViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PublicProfileViewModel.class)
    abstract ViewModel bindPublicProfileViewModel(PublicProfileViewModel viewModel);
}
