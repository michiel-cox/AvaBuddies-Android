package com.projectsoa.avabuddies.data.models;

import com.projectsoa.avabuddies.data.models.responses.user.UserResponse;

import org.parceler.Parcel;

@Parcel
public class User {
    // Fields needs to be protected to be used as an Parcel
    protected String _id;
    protected String email;
    protected String name;
    protected boolean sharelocation;
    protected boolean isAdmin;
    protected String image;
    protected String password;
    protected String aboutme;

    public User() {
    }

    public User(UserResponse response) {
        _id = response._id;
        email = response.email;
        name = response.name;
        sharelocation = response.sharelocation;
        isAdmin = response.isAdmin;
        image = response.image;
        password = response.password;
        aboutme = response.aboutme;
    }

    public User(String id, String email, String name) {
        _id = id;
        this.email = email;
        this.name = name;
    }

    public String getId() {
        return _id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public boolean isSharelocation() {
        return sharelocation;
    }

    public void setSharelocation(boolean sharelocation) {
        this.sharelocation = sharelocation;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getImage() {
        return image;
    }

    public String getPassword() {
        return password;
    }

}
