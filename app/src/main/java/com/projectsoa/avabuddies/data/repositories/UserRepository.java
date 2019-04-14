package com.projectsoa.avabuddies.data.repositories;

import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.models.responses.user.UserResponse;
import com.projectsoa.avabuddies.data.services.UserService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class UserRepository {

    private UserService userService;

    public UserRepository(UserService userService) {
        this.userService = userService;
    }

    public Single<User> getProfile() {
        return userService.fetchProfile().map(profileResponse -> new User(profileResponse.user));
    }

    public Single<User> getUser(String id) {
        return userService.fetchUser(id).map(profileResponse -> new User(profileResponse.user));
    }

    public Single<List<User>> getList() {
        return userService.fetchList().map(userListResponse -> {
            List<User> users = new ArrayList<>();
            for (UserResponse user : userListResponse.users) {
                users.add(new User(user));
            }
            return users;
        });
    }

    public Completable delete(User user) {
        return userService.deleteUser(user.getId());
    }

    public Completable update(User user) {
        return userService.updateProfile(user.getAboutme(), user.isSharelocation());
    }

    public Completable updateProfilePicture(User user) {
        return userService.updateProfilePicture(user.getImage());
    }
}
