package com.projectsoa.avabuddies.data.repositories;

import com.projectsoa.avabuddies.Constants;
import com.projectsoa.avabuddies.data.models.LoggedInUser;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.models.responses.UserResponse;
import com.projectsoa.avabuddies.data.services.AuthService;
import com.projectsoa.avabuddies.data.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Completable;
import io.reactivex.Single;

public class UserRepository {

    private UserService userService;

    public UserRepository(UserService userService){
        this.userService = userService;
    }

    public Single<User> getProfile(){
        return userService.fetchProfile().map(profileResponse -> new User(profileResponse.user));
    }

    public Single<List<User>> getList(){
        return userService.fetchList().map(userListResponse -> {
            List<User> users = new ArrayList<>();
            for (UserResponse user : userListResponse.users) {
                users.add(new User(user));
            }
            return users;
        });
    }
}
