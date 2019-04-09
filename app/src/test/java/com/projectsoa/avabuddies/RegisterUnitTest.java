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

import io.reactivex.Single;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RegisterUnitTest extends BaseUnitTest {

    private LoginRepository loginRepository;

    @Before
    public void setUp(){
        loginRepository = new LoginRepository();
    }

    @Test
    public void signup_onCorrect(){

        String expectedEmail = "bob@builder.com";
        String expectedName = "Bob Builder";
        String expectedToken = "abcdefghijklmnopqrstuvwxyz1234567890";
        boolean expectedLocation = true;

        loginRepository.setAuthService(new AuthService() {
            @Override
            public Single<LoginResponse> doLogin(String email, String password) {
                return Single.just(new LoginResponse(){{
                    token = expectedToken;
                }});
            }

            @Override
            public Single<SignupResponse> doSignup(String email, String password, String name, boolean sharelocation) {
                return Single.just(new SignupResponse(){{
                    user = new UserResponse(){{
                        email = expectedEmail;
                        name = expectedName;
                        sharelocation = expectedLocation;
                    }};
                }});
            }
        });

        loginRepository.setUserRepository(new UserRepository(new UserService() {
            @Override
            public Single<ProfileResponse> fetchProfile() {
                return Single.just(new ProfileResponse(){{
                    user = new UserResponse(){{
                        email = expectedEmail;
                        name = expectedName;
                        sharelocation = expectedLocation;
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
        }));


        loginRepository.register(expectedEmail, expectedName, expectedLocation).blockingGet();

        assertTrue(loginRepository.isLoggedIn());
        assertNotNull(loginRepository.getLoggedInUser());
        assertEquals(loginRepository.getLoggedInUser().getUser().getEmail(), expectedEmail);
        assertEquals(loginRepository.getLoggedInUser().getUser().getName(), expectedName);
        assertEquals(loginRepository.getLoggedInUser().getUser().isSharelocation(), expectedLocation);
        assertEquals(loginRepository.getLoggedInUser().getToken(), expectedToken);
    }

    @Test
    public void signup_onException(){

        String email = "bob@builder.com";
        String name = "Bob Builder";
        boolean location = true;

        loginRepository.setAuthService(new AuthService() {
            @Override
            public Single<LoginResponse> doLogin(String email, String password) {
                return Single.error(new Exception());
            }

            @Override
            public Single<SignupResponse> doSignup(String email, String password, String name, boolean sharelocation) {
                return Single.error(new Exception());
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
        }));

        loginRepository.register(email, name, location).blockingGet();
        
        assertFalse(loginRepository.isLoggedIn());
        assertNull(loginRepository.getLoggedInUser());
    }

}