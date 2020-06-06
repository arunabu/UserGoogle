package com.example.usergoogle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.regions.Regions;

import java.util.HashMap;
import java.util.Map;

public class verifyemail extends AppCompatActivity {
    Button verify;
    EditText email,code;
    private static final String TAG = "TAG";
    private CognitoUserCodeDeliveryDetails CognitoUserCodeDeliveryDetails;
    private CognitoUserSession CognitoUserSession;
    private CognitoUserPool CognitoUserPool;
    CognitoUser cognitoUser;
    String username;
    CognitoCachingCredentialsProvider credentialsProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyemail);
        verify = findViewById(R.id.verify);
        code = findViewById(R.id.code);
        email  = findViewById(R.id.email);
         username = getIntent().getStringExtra("username");
        email.setText(username);
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-south-1:7eaab23e-638e-485f-9353-7a97bef9e36e", // Identity pool ID
                Regions.AP_SOUTH_1 // Region

        );
        Log.i(TAG, " crediential  " + credentialsProvider.toString());

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ConfirmTask().execute(String.valueOf(code.getText()),String.valueOf(email.getText()));
            }
        });

    }
    private  class ConfirmTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            final String[] result = new String[1];

            final GenericHandler confirmationCallBack = new GenericHandler() {
                @Override
                public void onSuccess() {
                    result[0] = "sucessed";
                }
                @Override
                public void onFailure(Exception exception) {
                    result[0] = "failed"+exception.getLocalizedMessage();
                }
            };
            CognitoSettings cognitoSettings = new CognitoSettings(verifyemail.this);
            CognitoUser cognitoUser = cognitoSettings.getUserpool().getUser(strings[1]);
            cognitoUser.confirmSignUp(strings[0],false,confirmationCallBack);
            return result[0];
        }

        @Override
        protected void onPostExecute(String s) {
                super.onPostExecute(s);
            Log.i(TAG, "confirmation " + s);
            CognitoSettings cognitoSettings = new CognitoSettings(verifyemail.this);
            cognitoUser =  cognitoSettings.getUserpool().getUser(username);
            Log.i(TAG,"in button clicked");
            cognitoUser.getSessionInBackground(new AuthenticationHandler() {
                @Override
                public void onSuccess(com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession userSession, CognitoDevice newDevice) {
                    String JWTToken = userSession.getAccessToken().toString();
                    String refreshtoken = userSession.getRefreshToken().getToken().toString();
                    String idToken = userSession.getIdToken().toString();

                    Log.i(TAG,"JWT Token :"+JWTToken);
                    Log.i(TAG,"refreshtoken Token :"+refreshtoken);
                    Log.i(TAG,"idToken Token :"+idToken);

                    Map<String, String> logins = new HashMap<String, String>();
                    logins.put("cognito-idp.ap-south-1.amazonaws.com/ap-south-1_ZGuXjLdfZ>", userSession.getIdToken().getJWTToken());
                    credentialsProvider.setLogins(logins);

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
                    Log.i(TAG,"Creating key was Failed " + exception);

                }


            });
        }

    }
    }

