package com.projectsoa.avabuddies.data.repositories;

import com.projectsoa.avabuddies.Constants;
import com.projectsoa.avabuddies.data.models.LoggedInUser;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.services.AuthService;
import com.projectsoa.avabuddies.data.services.UserService;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

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
                .map(loginResponse ->  {
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
                .concatWith(login(email));
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
}
