package com.projectsoa.avabuddies.data.models;

public class LoggedInUser {
    private final String token;
    private User user;

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

