package com.projectsoa.avabuddies.data.repositories;

import com.projectsoa.avabuddies.data.models.LoggedInUser;

import javax.inject.Singleton;

public class LoginRepository {
    private LoggedInUser user;

    public boolean isLoggedIn() {
        return user != null;
    }

    public LoggedInUser getLoggedInUser() {
        return user;
    }

    public void logout() {
        user = null;
    }
    public void login(String email) {
        user = new LoggedInUser(email);
    }
}
