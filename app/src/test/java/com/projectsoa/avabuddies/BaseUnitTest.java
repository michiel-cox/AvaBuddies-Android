package com.projectsoa.avabuddies;

import android.content.Intent;
import android.os.Build;

import com.projectsoa.avabuddies.screens.login.LoginActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
public abstract class BaseUnitTest {

    @Before
    public void setUpBase(){
        MockitoAnnotations.initMocks(this);
    }
}
