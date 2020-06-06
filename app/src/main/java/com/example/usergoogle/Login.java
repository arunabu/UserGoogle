package com.example.usergoogle;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSRefreshableSessionCredentials;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentityprovider.model.CompromisedCredentialsEventActionType;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
EditText username,password;
TextView test;
Button login;
String user = null;
String pass = null;
    private static final String TAG = "TAG";
    CognitoUser cognitoUser;
    CognitoCachingCredentialsProvider credentialsProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        test = findViewById(R.id.T);
        login = findViewById(R.id.login);
        username  = findViewById(R.id.username);
        password = findViewById(R.id.password);
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-south-1:7eaab23e-638e-485f-9353-7a97bef9e36e", // Identity pool ID
                Regions.AP_SOUTH_1 // Region

        );
            test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i =new Intent(Login.this,MainActivity.class);
                    startActivity(i);
                }
            });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass =password.getText().toString();
                if ((user==null)&&(pass==null)){
                    Toast.makeText(Login.this,"Fill username & password",Toast.LENGTH_LONG).show();

                }
                else
                {
                    CognitoSettings cognitoSettings = new CognitoSettings(Login.this);
                     cognitoUser =  cognitoSettings.getUserpool().getUser(user);
                    Log.i(TAG,"in button clicked");
                    cognitoUser.getSessionInBackground(authenticationHandler);
                }

            }
        });
    }

     AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
         @Override
         public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {

                                 String JWTToken = userSession.getAccessToken().getExpiration().toString();
                     String refreshtoken = userSession.getRefreshToken().getToken().toString();
                     String idToken = userSession.getIdToken().getExpiration().toString();

                     Log.i(TAG,"JWT Token :"+JWTToken);
                     Log.i(TAG,"refreshtoken Token :"+refreshtoken);
                     Log.i(TAG,"idToken Token :"+idToken);

                     Map<String, String> logins = new HashMap<String, String>();
                     logins.put("cognito-idp.ap-south-1.amazonaws.com/ap-south-1_ZGuXjLdfZ>",userSession.getIdToken().getJWTToken());
                     credentialsProvider.setLogins(logins);


             Log.i(TAG, " crediential  " + credentialsProvider.toString());

             Log.i(TAG, "JWT Token :" + JWTToken);

            // Map<String, String> logins = new HashMap<String, String>();
           //  logins.put("cognito-idp.ap-south-1.amazonaws.com/ap-south-1_ZGuXjLdfZ>", userSession.getIdToken().getJWTToken());
           //  credentialsProvider.setLogins(logins);


             Log.i(TAG, "Login succesfull ,can get tokens here  " + JWTToken);
             Intent i = new Intent(Login.this, Home.class);
             Bundle b = new Bundle();
             b.putString("JWTToken", JWTToken);
             i.putExtras(b);
             startActivity(i);

         }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
            Log.i(TAG,"in getAuthentication details()");
            if (((userId==null)||(pass==null))){
                Toast.makeText(Login.this,"Fill username & password",Toast.LENGTH_LONG).show();

            }
            else {
                AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, pass, null);
                authenticationContinuation.setAuthenticationDetails(authenticationDetails);
                authenticationContinuation.continueTask();
            }
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
            Log.i(TAG,"in getMFACode()");

        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
            Log.i(TAG,"in authenticationChallenge()");

        }

        @Override
        public void onFailure(Exception exception) {
            Log.i(TAG,"Login failed "+exception.getLocalizedMessage());
            Toast.makeText(Login.this,"Fill username & password",Toast.LENGTH_LONG).show();

        }
    };

}
