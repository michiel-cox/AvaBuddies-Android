package com.projectsoa.avabuddies.core.dagger.builders;

import com.projectsoa.avabuddies.data.services.AuthService;
import com.projectsoa.avabuddies.data.services.DialogService;
import com.projectsoa.avabuddies.data.services.ChallengeService;
import com.projectsoa.avabuddies.data.services.FriendService;
import com.projectsoa.avabuddies.data.services.MessageService;
import com.projectsoa.avabuddies.data.services.TagService;
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

    @Provides
    FriendService getFriendService(Retrofit retroFit) {
        return retroFit.create(FriendService.class);
    }

    @Provides
    TagService getTagService(Retrofit retroFit) {
        return retroFit.create(TagService.class);
    }
    @Provides
    ChallengeService getChallengeService(Retrofit retrofit){ return retrofit.create(ChallengeService.class);}

    @Provides
    DialogService getDialogService (Retrofit retroFit) { return  retroFit.create(DialogService.class);}

    @Provides
    MessageService getMessageService (Retrofit retroFit) { return  retroFit.create(MessageService.class);}
}
