package com.example.usergoogle;

import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

class CognitoSettings {

    private  String userPoolId = "ap-south-1_ZGuXjLdfZ";
    private String clientId = "1k6optm76p7rn1hmgvmvkkqm73";
    private  String clientSecret ="130hkfsm2cii6tgbfs3a12ftoukd6111mtmrsvlcp1tsc0ibield";
    private    Regions cognitoRegion = Regions.AP_SOUTH_1;
    private Context context;


    public CognitoSettings(Context context) {
        this.context = context;
    }

    public String getUserPoolId() {
        return userPoolId;
    }

    public void setUserPoolId(String userPoolId) {
        this.userPoolId = userPoolId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Regions getCognitoRegion() {
        return cognitoRegion;
    }

    public void setCognitoRegion(Regions cognitoRegion) {
        this.cognitoRegion = cognitoRegion;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public CognitoUserPool getUserpool()
    {
        return new CognitoUserPool(context,userPoolId,clientId,clientSecret,cognitoRegion);
    }
}
