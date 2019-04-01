package com.projectsoa.avabuddies.data.models;

public class LoggedInUser {
    private User user;
    private final String token;

    public LoggedInUser(String token) {
        this.token = token;
    }


    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

