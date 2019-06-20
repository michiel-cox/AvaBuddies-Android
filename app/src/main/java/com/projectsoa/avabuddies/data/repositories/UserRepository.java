package com.projectsoa.avabuddies.data.repositories;

import com.projectsoa.avabuddies.data.models.Tag;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.models.responses.user.UserResponse;
import com.projectsoa.avabuddies.data.services.UserService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class UserRepository {

    private UserService userService;
    private String microsoftPhoto;

    public UserRepository(UserService userService) {
        this.userService = userService;
    }

    public Single<User> getProfile() {
        return userService.fetchProfile().map(profileResponse -> new User(profileResponse, microsoftPhoto));
    }

    // This is temporary.
    public Single<User> getUser(String id) {
        return userService.fetchList().map(userListResponse -> {
            for (UserResponse user : userListResponse.users) {
                if(user._id.equals(id)) return user;
            }
            throw new Exception("User not found.");
        }).map(User::new);
    }
    /*
    TODO: Enable when backend has fixed getting a single user
    public Single<User> getUser(String id) {
        return userService.fetchUser(id).map(profileResponse -> new User(profileResponse.user));
    }
    */

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
        return userService.deleteUser();
    }

    public Completable update(User user) {
        List<String> responseList = new ArrayList<>();
        for(Tag tag : user.getTags()){
            responseList.add(tag.get_id());
        }
        return userService.updateProfile(user.getAboutme(), user.isSharelocation(), user.isShareprofile(), responseList);
    }

    public Completable updateProfilePicture(User user) {
        return userService.updateProfilePicture(user.getImage());
    }

    public Completable updateProfilePictureFromMicrosoft( ) {
        return userService.updateProfilePicture(microsoftPhoto);
    }
    public void updateMicrosoftProfilePicture(String image) {
        microsoftPhoto = image;
    }
    public void resetMicrosoftProfilePicture() {
        microsoftPhoto = null;
    }
}
