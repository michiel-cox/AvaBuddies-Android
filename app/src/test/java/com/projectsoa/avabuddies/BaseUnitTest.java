package com.projectsoa.avabuddies;

import android.content.Intent;
import android.os.Build;

import com.projectsoa.avabuddies.core.dagger.components.AppComponent;
import com.projectsoa.avabuddies.core.dagger.components.DaggerAppComponent;
import com.projectsoa.avabuddies.core.dagger.modules.AppModule;
import com.projectsoa.avabuddies.screens.login.LoginActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import androidx.test.platform.app.InstrumentationRegistry;
import it.cosenonjaviste.daggermock.DaggerMockRule;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
public abstract class BaseUnitTest {
/*

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
/*
    public DaggerMockRule<AppComponent> mockitoRule = new DaggerMockRule<>(AppComponent.class, new AppModule())
            .customizeBuilder((DaggerMockRule.BuilderCustomizer<AppComponent.Builder>) builder -> {
                builder.seedInstance(getApp());
                return builder;
            }).set(component -> component.inject(getApp()));*/

/*
    private static App getApp() {
        return (App) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
    }
*/
    @Before
    public void setUpBase(){
        MockitoAnnotations.initMocks(this);
    }
}
