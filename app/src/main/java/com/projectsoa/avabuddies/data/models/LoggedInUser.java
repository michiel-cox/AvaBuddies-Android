package com.projectsoa.avabuddies.data.models;

public class LoggedInUser {
    private final String email;
    private final String token;

    public LoggedInUser(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}

