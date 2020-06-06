package com.example.usergoogle;

import androidx.appcompat.app.AppCompatActivity;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "Tag";
    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    CognitoUserAttributes cognitoUserAttributes;
    String email,token,password,username;
    EditText user,pass;
    CognitoCachingCredentialsProvider credentialsProvider;
    private CognitoUserPool cognitoUserPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set the dimensions of the sign-in button.
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        user = findViewById(R.id.user);
        pass = findViewById(R.id.pass);
        cognitoUserAttributes = new CognitoUserAttributes();


// Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        Log.i(TAG, " Google sign in options gso " + gso);

        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-south-1:7eaab23e-638e-485f-9353-7a97bef9e36e", // Identity pool ID
                Regions.AP_SOUTH_1 // Region

        );
        Log.i(TAG, " crediential  " + credentialsProvider.toString());


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Log.i(TAG, "cognito user got the Google sign in client  " + mGoogleSignInClient);


// Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);

        Log.i(TAG, " user already signed in " + account);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        Log.i(TAG, "Signin button clicked ");
                        username = user.getText().toString();
                        password  = pass.getText().toString();
                        if(username!=null&&password!=null)
                        {
                            signIn();
                        }

                        else
                        {
                            Toast.makeText(MainActivity.this,"Fill username & password",Toast.LENGTH_LONG).show();
                        }
                        break;

                    // ...
                }
            }
        });


    }

    private void updateUI(GoogleSignInAccount acct) {

        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getId();
            String personEmail = acct.getEmail();
            String personId = acct.getIdToken();
            Uri personPhoto = acct.getPhotoUrl();


            Toast.makeText(MainActivity.this, "Sucess" + personFamilyName + personId, Toast.LENGTH_LONG).show();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            Log.i(TAG, "signin activityresult  " + data);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // cognitoUserAttributes.addAttribute("given_name",String.valueOf(account.getGivenName()));
            cognitoUserAttributes.addAttribute("email", String.valueOf(account.getEmail()));
            token =account.getIdToken();


                String t = account.getId();
            CognitoSettings cognitoUserSettings = new CognitoSettings(MainActivity.this);
          cognitoUserSettings.getUserpool().signUpInBackground(username, password, cognitoUserAttributes, null, signupCallback);
            // Signed in successfully, show authenticated UI.
            Log.i(TAG, "cognito user settings SUCCESS " + cognitoUserSettings + account.getDisplayName() + "," + account.getFamilyName() + "," + account.getGivenName() + "," + account.getId());
            Log.i(TAG, "cognito settings  " + cognitoUserAttributes.getAttributes().toString());



            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
    final SignUpHandler signupCallback = new SignUpHandler() {

        @Override
        public void onSuccess(CognitoUser cognitoUser, SignUpResult signUpResult) {
            boolean signupConfirmationState = signUpResult.getUserConfirmed();
            Log.i(TAG, "SIGNUP SUCESS IS CONFIRMED" + signupConfirmationState);
            if (!signupConfirmationState) {
                Intent i = new Intent(MainActivity.this, verifyemail.class);
                    Bundle b = new Bundle();

                    b.putString("username",username);
                    i.putExtras(b);
                startActivity(i);

                Log.i(TAG, "SIGNUP SUCESS IS NOT CONFIRMED ,verification code has sent to" + signUpResult.getCodeDeliveryDetails().getDestination());

            } else {
                Intent i = new Intent(MainActivity.this, Home.class);
                startActivity(i);
                Log.i(TAG, "SIGNUP SUCESS .......confirmed" + signupConfirmationState);

            }

        }

        @Override
        public void onFailure(Exception exception) {
            Log.i(TAG, "SIGNUP Failedd" + exception);

        }

    };

    }

  /*  final SignUpHandler signupCallback = new SignUpHandler() {

        @Override
        public void onSuccess(CognitoUser cognitoUser, SignUpResult signUpResult) {
            Log.i(TAG, "signuphandler :" + cognitoUser);
            Log.i(TAG, "signuphandler :" + signUpResult.isUserConfirmed().toString());

            cognitoUser.getSessionInBackground(new AuthenticationHandler() {

                @Override
                public void onSuccess(CognitoUserSession session, CognitoDevice newDevice) {
                    String idToken = session.getIdToken().getJWTToken();
                    Log.w(TAG, "Token = " +idToken);
                    Map<String, String> logins = new HashMap<String, String>();
                    logins.put("cognito-idp.ap-south-1_ZGuXjLdfZ.amazonaws.com/ap-south-1_ZGuXjLdfZ", session.getIdToken().getJWTToken());
                    credentialsProvider.setLogins(logins);
                    Log.i(TAG, "cognito user session SUCCESS  sessions:" + session);
                    Log.i(TAG, "cognito user session SUCCESS  id Token :" + idToken);
                    Log.i(TAG, "cognito user session SUCCESS Logins " + logins);
                    Log.i(TAG, "cognito user session SUCCESS  creditial provider" + credentialsProvider);





                }

                @Override
                public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {

                }

                @Override
                public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

                }

                @Override
                public void authenticationChallenge(ChallengeContinuation continuation) {

                }

                @Override
                public void onFailure(Exception exception) {
                    Log.i(TAG, "Signup handler failed  " + exception);

                }



            });
            Log.i(TAG, "SIGNUP user SUCCESS " + cognitoUser);


        }

        @Override
        public void onFailure(Exception exception) {
            Log.i(TAG, "SIGNUP was FAILED" +exception);

        }
    };
}
*/