package com.projectsoa.avabuddies.data.models;

public class LoggedInUser {
    private final String email;

    public LoggedInUser(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}

