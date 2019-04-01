package com.projectsoa.avabuddies.data.repositories;

import com.projectsoa.avabuddies.Constants;
import com.projectsoa.avabuddies.data.models.LoggedInUser;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.models.responses.UserResponse;
import com.projectsoa.avabuddies.data.services.AuthService;
import com.projectsoa.avabuddies.data.services.UserService;

import io.reactivex.Completable;
import io.reactivex.Single;

public class UserRepository {

    private UserService userService;

    public UserRepository(UserService userService){
        this.userService = userService;
    }

    public Single<User> getProfile(){
        return userService.fetchProfile().map(loginResponse -> {
            UserResponse userResponse = loginResponse.user;
            return new User(userResponse._id, userResponse.email, userResponse.name, userResponse.sharelocation, userResponse.isAdmin, userResponse.image, userResponse.password);
        });
    }
}
