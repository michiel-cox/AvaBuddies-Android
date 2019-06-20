package com.projectsoa.avabuddies.data.repositories;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.projectsoa.avabuddies.Constants;
import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.data.models.LoggedInUser;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.services.AuthService;


import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;

import static android.graphics.Bitmap.Config.RGB_565;

public class LoginRepository {
    private LoggedInUser loggedInUser;

    private AuthService authService;
    private UserRepository userRepository;

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public LoggedInUser getLoggedInUser() {
        return loggedInUser;
    }

    public void logout() {
        loggedInUser = null;
    }

    public Completable login(String email) {
        return authService.doLogin(email, Constants.SECRET)
                .map(loginResponse -> {
                    loggedInUser = new LoggedInUser(loginResponse.token);
                    loggedInUser.setUser(userRepository.getProfile().blockingGet());
                    return loggedInUser;
                })
                .ignoreElement();
    }

    public Completable register(String email, String name, boolean sharelocation) {
        return
                authService.doSignup(email, Constants.SECRET, name, sharelocation)
                        .ignoreElement()
                        .concatWith(login(email))
                        .doOnComplete(this::updateMicrosoftPhoto);
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private void updateMicrosoftPhoto() {
        userRepository.updateProfilePictureFromMicrosoft().subscribe(() -> {
                },
                throwable -> System.out.println("mislukt"));
    }
}
