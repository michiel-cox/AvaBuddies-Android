package com.projectsoa.avabuddies.core.dagger.builders;

import com.projectsoa.avabuddies.data.sockets.SocketIO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SocketBuilder {
    @Singleton
    @Provides
    static SocketIO provideSocketIO() {
        return new SocketIO();
    }
}
