package com.projectsoa.avabuddies.data.repositories;

import com.projectsoa.avabuddies.App;
import com.projectsoa.avabuddies.Constants;
import com.projectsoa.avabuddies.data.models.LoggedInUser;
import com.projectsoa.avabuddies.data.models.responses.LoginResponse;
import com.projectsoa.avabuddies.data.services.AuthService;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import dagger.android.DaggerApplication;
import dagger.android.DaggerService;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    private LoggedInUser loggedInUser;

    protected AuthService authService;

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public LoggedInUser getLoggedInUser() {
        return loggedInUser;
    }

    public void logout() {
        loggedInUser = null;
    }

    public Single<LoggedInUser> login(String email) {
        return authService.doLogin(email, Constants.SECRET)
                .map(loginResponse ->
                        new LoggedInUser(email, loginResponse.token))
                .doOnSuccess(user ->
                        loggedInUser = user);
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }
}
