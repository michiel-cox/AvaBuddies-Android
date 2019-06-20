package com.projectsoa.avabuddies.data.models;

import android.media.Image;

import com.projectsoa.avabuddies.data.models.responses.user.UserResponse;
import com.stfalcon.chatkit.commons.models.IUser;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class User implements IUser {
    // Fields needs to be protected to be used as an Parcel
    protected String _id;
    protected String email;
    protected String name;
    protected boolean sharelocation;
    protected boolean isPrivate;
    protected boolean isAdmin;
    protected String image;
    protected String password;
    protected String aboutme;
    protected List<Tag> tags;


    public User() {
    }

    public User(UserResponse response) {
        _id = response._id;
        email = response.email;
        name = response.name;
        sharelocation = response.sharelocation;
        isPrivate = response.isPrivate;
        isAdmin = response.isAdmin;
        image = response.image;
        password = response.password;
        aboutme = response.aboutme;
        tags = response.tags;
    }

    public User(UserResponse response, String photo) {
        _id = response._id;
        email = response.email;
        name = response.name;
        sharelocation = response.sharelocation;
        isPrivate = response.isPrivate;
        isAdmin = response.isAdmin;
        if (response.image == null || response.image.isEmpty()){
            image = photo;
        } else {
            image = response.image;
        }
        password = response.password;
        aboutme = response.aboutme;
        tags = response.tags;
    }

    public User(String id, String email, String name) {
        _id = id;
        this.email = email;
        this.name = name;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public List<Tag> getTags() {
        return tags;
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

    public boolean isShareprofile() {
        return isPrivate;
    }

    public void setSharelocation(boolean sharelocation) {
        this.sharelocation = sharelocation;
    }

    public void setShareprofile(boolean shareprofile) {
        this.isPrivate = shareprofile;
    }

    public void setImage(String base64) {
        this.image = base64;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getImage() {
        return image;
    }

    public String getAvatar() {
        return image;
    }

    public String getPassword() {
        return password;
    }

}
