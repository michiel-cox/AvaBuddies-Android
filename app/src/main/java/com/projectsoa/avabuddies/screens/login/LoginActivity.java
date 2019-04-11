package com.projectsoa.avabuddies.screens.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.microsoft.aad.adal.ADALError;
import com.microsoft.aad.adal.AuthenticationCallback;
import com.microsoft.aad.adal.AuthenticationContext;
import com.microsoft.aad.adal.AuthenticationException;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.IDispatcher;
import com.microsoft.aad.adal.Logger;
import com.microsoft.aad.adal.PromptBehavior;
import com.microsoft.aad.adal.Telemetry;
import com.microsoft.aad.adal.UserInfo;
import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseActivity;
import com.projectsoa.avabuddies.data.repositories.LoginRepository;
import com.projectsoa.avabuddies.screens.main.MainActivity;
import com.projectsoa.avabuddies.screens.register.RegisterActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import butterknife.OnClick;

public class LoginActivity extends BaseActivity {


    @Inject
    protected LoginRepository loginRepository;
    private LoginViewModel viewModel;

    @OnClick(R.id.btn_login)
    public void login() {
        onCallGraphClicked();
    }

    public void onLogin(String email, String name){
        loginRepository.login(email).subscribe(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }, throwable -> {
            throwable.printStackTrace();
            register(email, name);
        });
    }

    public void register(String email, String name){
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("email",email);
        intent.putExtra("name",name);
        startActivity(intent);
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_login;
    }

    /* UI & Debugging Variables */
    private static final String TAG = LoginActivity.class.getSimpleName();

    /* Azure AD Constants */
    /* Authority is in the form of https://login.microsoftonline.com/yourtenant.onmicrosoft.com */
    private static final String AUTHORITY = "https://login.microsoftonline.com/common";
    /* The clientID of your application is a unique identifier which can be obtained from the app registration portal */
    private static final String CLIENT_ID = "cb6d5283-741e-4dc4-8cb2-e73d03629ced";
    /* Resource URI of the endpoint which will be accessed */
    private static final String RESOURCE_ID = "https://graph.microsoft.com/";
    /* The Redirect URI of the application (Optional) */
    private static final String REDIRECT_URI = "http://localhost";

    /* Microsoft Graph Constants */
    private final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me";

    /* Azure AD Variables */
    private AuthenticationContext mAuthContext;
    private AuthenticationResult mAuthResult;

    /* Handler to do an interactive sign in and acquire token */
    private Handler mAcquireTokenHandler;
    /* Boolean variable to ensure invocation of interactive sign-in only once in case of multiple  acquireTokenSilent call failures */
    private static AtomicBoolean sIntSignInInvoked = new AtomicBoolean();
    /* Constant to send message to the mAcquireTokenHandler to do acquire token with Prompt.Auto*/
    private static final int MSG_INTERACTIVE_SIGN_IN_PROMPT_AUTO = 1;
    /* Constant to send message to the mAcquireTokenHandler to do acquire token with Prompt.Always */
    private static final int MSG_INTERACTIVE_SIGN_IN_PROMPT_ALWAYS = 2;

    /* Constant to store user id in shared preferences */
    private static final String USER_ID = "user_id";

    /* Telemetry variables */
    // Flag to turn event aggregation on/off
    private static final boolean sTelemetryAggregationIsRequired = true;

    /* Telemetry dispatcher registration */
    static {
        Telemetry.getInstance().registerDispatcher(new IDispatcher() {
            @Override
            public void dispatchEvent(Map<String, String> events) {
                // Events from ADAL will be sent to this callback
                for (Map.Entry<String, String> entry : events.entrySet()) {
                    Log.d(TAG, entry.getKey() + ": " + entry.getValue());
                }
            }
        }, sTelemetryAggregationIsRequired);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = getViewModel(LoginViewModel.class);

        mAuthContext = new AuthenticationContext(getApplicationContext(), AUTHORITY, false);

        final boolean isLogout = getIntent().getBooleanExtra("logout", false);

        if (isLogout) {
            onLogout();
        }

        /* Instantiate handler which can invoke interactive sign-in to get the Resource
         * sIntSignInInvoked ensures interactive sign-in is invoked one at a time */

        mAcquireTokenHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (sIntSignInInvoked.compareAndSet(false, true)) {
                    if (isLogout || msg.what == MSG_INTERACTIVE_SIGN_IN_PROMPT_ALWAYS) {
                        mAuthContext.acquireToken(getActivity(), RESOURCE_ID, CLIENT_ID, REDIRECT_URI, PromptBehavior.Always, getAuthInteractiveCallback());
                    } else if (msg.what == MSG_INTERACTIVE_SIGN_IN_PROMPT_AUTO) {
                        mAuthContext.acquireToken(getActivity(), RESOURCE_ID, CLIENT_ID, REDIRECT_URI, PromptBehavior.Auto, getAuthInteractiveCallback());
                    }
                }
            }
        };

        /* ADAL Logging callback setup */

        Logger.getInstance().setExternalLogger(new Logger.ILogger() {
            @Override
            public void Log(String tag, String message, String additionalMessage, Logger.LogLevel level, ADALError errorCode) {
                // You can filter the logs  depending on level or errorcode.
                Log.d(TAG, message + " " + additionalMessage);
            }
        });


        if (isLogout) return;
        /*Attempt an acquireTokenSilent call to see if we're signed in*/
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userId = preferences.getString(USER_ID, "");
        if (!TextUtils.isEmpty(userId)) {
            mAuthContext.acquireTokenSilentAsync(RESOURCE_ID, CLIENT_ID, userId, getAuthSilentCallback());
        }
    }

    //
    // Core Auth methods used by ADAL
    // ==================================
    // onActivityResult() - handles redirect from System browser
    // onCallGraphClicked() - attempts to get tokens for graph, if it succeeds calls graph & updates UI
    // callGraphAPI() - called on successful token acquisition which makes an HTTP request to graph
    // onLogout() - Signs user out of the app & updates UI
    //

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAuthContext.onActivityResult(requestCode, resultCode, data);
    }

    /*
     * End user clicked call Graph API button, time for Auth
     * Use ADAL to get an Access token for the Microsoft Graph API
     */
    private void onCallGraphClicked() {
        mAcquireTokenHandler.sendEmptyMessage(MSG_INTERACTIVE_SIGN_IN_PROMPT_AUTO);
    }

    private void callGraphAPI() {
        Log.d(TAG, "Starting volley request to graph");

        /* Make sure we have a token to send to graph */
        if (mAuthResult.getAccessToken() == null) {
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("key", "value");
        } catch (Exception e) {
            Log.d(TAG, "Failed to put parameters: " + e.toString());
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MSGRAPH_URL,
                parameters, response -> {
                    /* Successfully called graph, process data and send to UI */
                    Log.d(TAG, "Response: " + response.toString());

                    updateGraphUI(response);
                }, error -> Log.d(TAG, "Error: " + error.toString())) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + mAuthResult.getAccessToken());
                return headers;
            }
        };

        Log.d(TAG, "Adding HTTP GET to Queue, Request: " + request.toString());
        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void onLogout() {
        // End user has clicked the Sign Out button
        // Kill the token cache
        // Optionally call the signout endpoint to fully sign out the user account
        mAuthContext.getCache().removeAll();
        mAuthResult = null;
        updateSignedOutUI();
    }

    //
    // UI Helper methods
    // ================================
    // updateGraphUI() - Sets graph response in UI
    // onLogin() - Updates UI when token acquisition succeeds
    // updateSignedOutUI() - Updates UI when app sign out succeeds
    //

    private void updateGraphUI(JSONObject response) {

    }

    @SuppressLint("SetTextI18n")
    private void onLogin() {
        UserInfo userInfo = mAuthResult.getUserInfo();
        onLogin(userInfo.getDisplayableId(),  String.format("%s %s",  userInfo.getGivenName(), userInfo.getFamilyName()));
    }

    @SuppressLint("SetTextI18n")
    private void updateSignedOutUI() {

    }

    //
    // ADAL Callbacks
    // ======================
    // getActivity() - returns activity so we can acquireToken within a callback
    // getAuthSilentCallback() - callback defined to handle acquireTokenSilent() case
    // getAuthInteractiveCallback() - callback defined to handle acquireToken() case
    //

    public Activity getActivity() {
        return this;
    }

    /* Callback used in for silent acquireToken calls.
     * Looks if tokens are in the cache (refreshes if necessary and if we don't forceRefresh)
     * else errors that we need to do an interactive request.
     */
    private AuthenticationCallback<AuthenticationResult> getAuthSilentCallback() {
        return new AuthenticationCallback<AuthenticationResult>() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                if (authenticationResult == null || TextUtils.isEmpty(authenticationResult.getAccessToken())
                        || authenticationResult.getStatus() != AuthenticationResult.AuthenticationStatus.Succeeded) {
                    Log.d(TAG, "Silent acquire token Authentication Result is invalid, retrying with interactive");
                    /* retry with interactive */
                    mAcquireTokenHandler.sendEmptyMessage(MSG_INTERACTIVE_SIGN_IN_PROMPT_AUTO);
                    return;
                }
                /* Successfully got a token, call graph now */
                Log.d(TAG, "Successfully authenticated");
                /* Store the mAuthResult */
                mAuthResult = authenticationResult;
                /* call graph */
                callGraphAPI();

                /* update the UI to post call graph state */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onLogin();
                    }
                });
            }

            @Override
            public void onError(Exception exception) {
                /* Failed to acquireToken */
                Log.e(TAG, "Authentication failed: " + exception.toString());
                if (exception instanceof AuthenticationException) {
                    AuthenticationException authException = ((AuthenticationException) exception);
                    ADALError error = authException.getCode();
                    logHttpErrors(authException);
                    /*  Tokens expired or no session, retry with interactive */
                    if (error == ADALError.AUTH_REFRESH_FAILED_PROMPT_NOT_ALLOWED) {
                        mAcquireTokenHandler.sendEmptyMessage(MSG_INTERACTIVE_SIGN_IN_PROMPT_AUTO);
                    } else if (error == ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION) {
                        /* Device is in Doze mode or App is in stand by mode.
                           Wake up the app or show an appropriate prompt for the user to take action
                           More information on this : https://github.com/AzureAD/azure-activedirectory-library-for-android/wiki/Handle-Doze-and-App-Standby */
                        Log.e(TAG, "Device is in doze mode or the app is in standby mode");
                    }
                    return;
                }
                /* Attempt an interactive on any other exception */
                mAcquireTokenHandler.sendEmptyMessage(MSG_INTERACTIVE_SIGN_IN_PROMPT_AUTO);
            }
        };
    }

    private void logHttpErrors(AuthenticationException authException) {
        int httpResponseCode = authException.getServiceStatusCode();
        Log.d(TAG, "HTTP Response code: " + authException.getServiceStatusCode());
        if (httpResponseCode < 200 || httpResponseCode > 300) {
            // logging http response headers in case of a http error.
            HashMap<String, List<String>> headers = authException.getHttpResponseHeaders();
            if (headers != null) {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                    sb.append(entry.getKey());
                    sb.append(":");
                    sb.append(entry.getValue().toString());
                    sb.append("; ");
                }
                Log.e(TAG, "HTTP Response headers: " + sb.toString());
            }
        }
    }

    /* Callback used for interactive request.  If succeeds we use the access
     * token to call the Microsoft Graph. Does not check cache
     */
    private AuthenticationCallback<AuthenticationResult> getAuthInteractiveCallback() {
        return new AuthenticationCallback<AuthenticationResult>() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                if (authenticationResult == null || TextUtils.isEmpty(authenticationResult.getAccessToken())
                        || authenticationResult.getStatus() != AuthenticationResult.AuthenticationStatus.Succeeded) {
                    Log.e(TAG, "Authentication Result is invalid");
                    return;
                }
                /* Successfully got a token, call graph now */
                Log.d(TAG, "Successfully authenticated");
                Log.d(TAG, "ID Login: " + authenticationResult.getIdToken());

                /* Store the auth result */
                mAuthResult = authenticationResult;

                /* Store User id to SharedPreferences to use it to acquire token silently later */
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                preferences.edit().putString(USER_ID, authenticationResult.getUserInfo().getUserId()).apply();

                /* call graph */
                callGraphAPI();

                /* update the UI to post call graph state */
                runOnUiThread(() -> onLogin());
                /* set the sIntSignInInvoked boolean back to false  */
                sIntSignInInvoked.set(false);
            }

            @Override
            public void onError(Exception exception) {
                /* Failed to acquireToken */
                Log.e(TAG, "Authentication failed: " + exception.toString());
                if (exception instanceof AuthenticationException) {
                    ADALError error = ((AuthenticationException) exception).getCode();
                    if (error == ADALError.AUTH_FAILED_CANCELLED) {
                        Log.e(TAG, "The user cancelled the authorization request");
                    } else if (error == ADALError.AUTH_FAILED_NO_TOKEN) {
                        // In this case ADAL has found a token in cache but failed to retrieve it.
                        // Retry interactive with Prompt.Always to ensure we do an interactive sign in
                        mAcquireTokenHandler.sendEmptyMessage(MSG_INTERACTIVE_SIGN_IN_PROMPT_ALWAYS);
                    } else if (error == ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION) {
                        /* Device is in Doze mode or App is in stand by mode.
                           Wake up the app or show an appropriate prompt for the user to take action
                           More information on this : https://github.com/AzureAD/azure-activedirectory-library-for-android/wiki/Handle-Doze-and-App-Standby */
                        Log.e(TAG, "Device is in doze mode or the app is in standby mode");
                    }
                }
                /* set the sIntSignInInvoked boolean back to false  */
                sIntSignInInvoked.set(false);
            }
        };
    }

    @Override
    public void onBackPressed() {

    }
}
