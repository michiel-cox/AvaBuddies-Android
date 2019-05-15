package com.projectsoa.avabuddies.core.dagger.builders;

import com.projectsoa.avabuddies.screens.login.LoginActivity;
import com.projectsoa.avabuddies.screens.main.MainActivity;
import com.projectsoa.avabuddies.screens.main.chat.MessagesActivity;
import com.projectsoa.avabuddies.screens.main.nearby.NearbyFragment;
import com.projectsoa.avabuddies.screens.main.nearby.list.NearbyListFragment;
import com.projectsoa.avabuddies.screens.main.profile.ProfileChangeFragment;
import com.projectsoa.avabuddies.screens.main.profile.ProfileFragment;
import com.projectsoa.avabuddies.screens.main.publicprofile.PublicProfileFragment;
import com.projectsoa.avabuddies.screens.main.qrread.QRReadFragment;
import com.projectsoa.avabuddies.screens.main.qrshow.QRShowFragment;
import com.projectsoa.avabuddies.screens.main.search.SearchFragment;
import com.projectsoa.avabuddies.screens.main.tag.TagsFragment;
import com.projectsoa.avabuddies.screens.register.RegisterActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Put all the Activities & Fragments & View etc in here
 */
@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector()
    abstract LoginActivity contributeLoginActivity();

    @ContributesAndroidInjector()
    abstract RegisterActivity contributeRegisterActivity();

    @ContributesAndroidInjector()
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector()
    abstract ProfileFragment contributeProfileFragment();

    @ContributesAndroidInjector()
    abstract SearchFragment contributeSearchFragment();

    @ContributesAndroidInjector()
    abstract PublicProfileFragment contributePublicProfileFragment();

    @ContributesAndroidInjector()
    abstract ProfileChangeFragment contributeProfileChangeFragment();
    @ContributesAndroidInjector()
    abstract NearbyFragment contributeNearbyFragment();

    @ContributesAndroidInjector()
    abstract NearbyListFragment contributeNearbyListFragment();

    @ContributesAndroidInjector()
    abstract QRReadFragment contributeQRReadFragment();

    @ContributesAndroidInjector()
    abstract QRShowFragment contributeQRShowFragment();

    @ContributesAndroidInjector
    abstract TagsFragment contributeTagsFragment();

    @ContributesAndroidInjector
    abstract MessagesActivity contributemessagesActivity();


}
