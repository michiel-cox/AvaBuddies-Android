package com.projectsoa.avabuddies;

import com.projectsoa.avabuddies.data.models.LoggedInUser;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.models.responses.LoginResponse;
import com.projectsoa.avabuddies.data.models.responses.ProfileResponse;
import com.projectsoa.avabuddies.data.models.responses.SignupResponse;
import com.projectsoa.avabuddies.data.models.responses.UserResponse;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.data.repositories.UserRepository;
import com.projectsoa.avabuddies.data.services.AuthService;
import com.projectsoa.avabuddies.data.services.UserService;

import org.junit.Before;
import org.junit.Test;

import java.net.PasswordAuthentication;

import io.reactivex.Single;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class LoginUnitTest extends BaseUnitTest {

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
        }));

        loginRepository.login(email).blockingGet();

        assertFalse(loginRepository.isLoggedIn());
        assertNull(loginRepository.getLoggedInUser());
    }

}