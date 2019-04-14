package com.projectsoa.avabuddies;

import com.projectsoa.avabuddies.data.models.responses.auth.LoginResponse;
import com.projectsoa.avabuddies.data.models.responses.user.ProfileResponse;
import com.projectsoa.avabuddies.data.models.responses.auth.SignupResponse;
import com.projectsoa.avabuddies.data.models.responses.user.UserListResponse;
import com.projectsoa.avabuddies.data.models.responses.user.UserResponse;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.data.services.AuthService;
import com.projectsoa.avabuddies.data.services.UserService;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Completable;
import io.reactivex.Single;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class LoginUnitTest {

    private LoginRepository loginRepository;

    @Before
    public void setUp(){
        loginRepository = new LoginRepository();
    }

    @Test
    public void login_onCorrect(){
        String expectedEmail = "bob@builder.com";
        String expectedToken = "abcdefghijklmnopqrstuvwxyz1234567890";

        loginRepository.setAuthService(new AuthService() {
            @Override
            public Single<LoginResponse> doLogin(String email, String password) {
                return Single.just(new LoginResponse(){{
                    token = expectedToken;
                }});
            }

            @Override
            public Single<SignupResponse> doSignup(String email, String password, String name, boolean sharelocation) {
                return null;
            }
        });

        loginRepository.setUserRepository(new UserRepository(new UserService() {
            @Override
            public Single<ProfileResponse> fetchProfile() {
                return Single.just(new ProfileResponse(){{
                    user = new UserResponse(){{
                        email = expectedEmail;
                    }};
                }});
            }

            @Override
            public Single<ProfileResponse> fetchUser(String id) {
                return null;
            }

            @Override
            public Single<UserListResponse> fetchList() {
                return null;
            }

            @Override
            public Completable deleteUser(String id) {
                return null;
            }

            @Override
            public Completable updateProfile(String aboutMe, boolean sharelocation) {
                return null;
            }

            @Override
            public Completable updateProfilePicture(String base64) {
                return null;
            }
        }));


        loginRepository.login(expectedEmail).blockingAwait();

        assertTrue(loginRepository.isLoggedIn());
        assertNotNull(loginRepository.getLoggedInUser());
        assertEquals(loginRepository.getLoggedInUser().getUser().getEmail(), expectedEmail);
        assertEquals(loginRepository.getLoggedInUser().getToken(), expectedToken);
    }

    @Test
    public void login_onException(){
        String email = "bob@builder.com";

        loginRepository.setAuthService(new AuthService() {
            @Override
            public Single<LoginResponse> doLogin(String email, String password) {
                return Single.error(new Exception());
            }

            @Override
            public Single<SignupResponse> doSignup(String email, String password, String name, boolean sharelocation) {
                return null;
            }
        });

        loginRepository.setUserRepository(new UserRepository(new UserService() {
            @Override
            public Single<ProfileResponse> fetchProfile() {
                return Single.error(new Exception());
            }

            @Override
            public Single<ProfileResponse> fetchUser(String id) {
                return null;
            }

            @Override
            public Single<UserListResponse> fetchList() {
                return null;
            }

            @Override
            public Completable deleteUser(String id) {
                return null;
            }

            @Override
            public Completable updateProfile(String aboutMe, boolean sharelocation) {
                return null;
            }

            @Override
            public Completable updateProfilePicture(String base64) {
                return null;
            }
        }));

        loginRepository.login(email).blockingGet();

        assertFalse(loginRepository.isLoggedIn());
        assertNull(loginRepository.getLoggedInUser());
    }

}