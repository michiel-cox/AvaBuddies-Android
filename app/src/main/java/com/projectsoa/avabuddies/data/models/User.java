package com.projectsoa.avabuddies.data.models;

import com.projectsoa.avabuddies.data.models.responses.UserResponse;

public class User {
    private final String _id;
    private String email;
   private String name;
   private boolean sharelocation;
   private boolean isAdmin;
   private String image;
   private String password;

   public User(UserResponse response){
       _id = response._id;
       email = response.email;
       name = response.name;
       sharelocation = response.sharelocation;
       isAdmin = response.isAdmin;
       image = response.image;
       password = response.password;
   }

    public User(String id, String email, String name, boolean sharelocation, boolean isAdmin, String image, String password) {
        _id = id;
        this.email = email;
        this.name = name;
        this.sharelocation = sharelocation;
        this.isAdmin = isAdmin;
        this.image = image;
        this.password = password;
    }

    public User(String id, String email, String name){
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


    public boolean isSharelocation() {
        return sharelocation;
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
