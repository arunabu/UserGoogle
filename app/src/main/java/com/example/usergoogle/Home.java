package com.example.usergoogle;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.util.Base64;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class Home extends AppCompatActivity {
    CognitoCachingCredentialsProvider credentialsProvider;
    EditText oneEd, twoEd,threeEd,fourEd;
    Button button ,update,delete,get;
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        oneEd = findViewById(R.id.one);
        twoEd = findViewById(R.id.two);
        t = findViewById(R.id.t);

        threeEd = findViewById(R.id.three);
        fourEd = findViewById(R.id.four);
        button = findViewById(R.id.button);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        get = findViewById(R.id.get);
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-south-1:7eaab23e-638e-485f-9353-7a97bef9e36e", // Identity pool ID
                Regions.AP_SOUTH_1 // Region

        );
        LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
                Regions.AP_SOUTH_1, credentialsProvider);
        final MyInterface myInterface = factory.build(MyInterface.class);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = oneEd.getText().toString();
                String user_id = twoEd.getText().toString();
                String phoneNumber = threeEd.getText().toString();
                String Lastname =fourEd.getText().toString();

                RequestClass request = new RequestClass(name, phoneNumber,user_id,Lastname);



                new AsyncTask<RequestClass, Void, ResponseClass>() {
                    @Override
                    protected ResponseClass doInBackground(RequestClass... params) {
                        // invoke "echo" method. In case it fails, it will throw a
                        // LambdaFunctionException.
                        try {
                            return myInterface.AddFunction(params[0]);

                        } catch (LambdaFunctionException lfe) {
                            Log.e("Tag", "Failed to invoke echo", lfe);
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(ResponseClass result) {
                        if (result == null) {
                            return;
                        }

                        // Do a toast
                        Toast.makeText(Home.this, result.getGreetings(), Toast.LENGTH_LONG).show();
                    }
                }.execute(request);

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = oneEd.getText().toString();
                String user_id = twoEd.getText().toString();
                String phoneNumber = threeEd.getText().toString();
                String Lastname =fourEd.getText().toString();
                RequestClass request = new RequestClass(name, phoneNumber,user_id,Lastname);



                new AsyncTask<RequestClass, Void, ResponseClass>() {
                    @Override
                    protected ResponseClass doInBackground(RequestClass... params) {
                        // invoke "echo" method. In case it fails, it will throw a
                        // LambdaFunctionException.
                        try {
                            return myInterface.updateFunction(params[0]);

                        } catch (LambdaFunctionException lfe) {
                            Log.e("Tag", "Failed to invoke echo", lfe);
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(ResponseClass result) {
                        if (result == null) {
                            return;
                        }

                        // Do a toast
                        Toast.makeText(Home.this, result.getGreetings(), Toast.LENGTH_LONG).show();
                    }
                }.execute(request);

            }
        });




        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = oneEd.getText().toString();
             //   String user_id = twoEd.getText().toString();
             //  String phoneNumber = threeEd.getText().toString();
             //  String Lastname =fourEd.getText().toString();
                RequestClass request = new RequestClass(name);



                new AsyncTask<RequestClass, Void, ResponseClass>() {
                    @Override
                    protected ResponseClass doInBackground(RequestClass... params) {
                        // invoke "echo" method. In case it fails, it will throw a
                        // LambdaFunctionException.
                        try {
                            return myInterface.DeleteFunction(params[0]);

                        } catch (LambdaFunctionException lfe) {
                            Log.e("Tag", "Failed to invoke echo", lfe);
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(ResponseClass result) {
                        if (result == null) {
                            Toast.makeText(Home.this, "null", Toast.LENGTH_LONG).show();

                            return;
                        }

                        // Do a toast
                        Toast.makeText(Home.this, "Deleted", Toast.LENGTH_LONG).show();
                    }
                }.execute(request);


            }
        });



            get.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = oneEd.getText().toString();
                    String user_id = twoEd.getText().toString();
                    String phoneNumber = threeEd.getText().toString();
                    final String Lastname =fourEd.getText().toString();
                    RequestClass request = new RequestClass(name, phoneNumber,user_id,Lastname);



                    new AsyncTask<RequestClass, Void, ResponseClass>() {
                        @Override
                        protected ResponseClass doInBackground(RequestClass... params) {
                            // invoke "echo" method. In case it fails, it will throw a
                            // LambdaFunctionException.
                            try {
                                return myInterface.GetItemFunction(params[1]);

                            } catch (LambdaFunctionException lfe) {
                                Log.e("Tag", "Failed to invoke echo", lfe);
                                return null;
                            }
                        }

                        @Override
                        protected void onPostExecute(ResponseClass result) {
                            if (result == null) {

                                Toast.makeText(Home.this,"Check console", Toast.LENGTH_LONG).show();
                                return;
                            }

                            // Do a toast]
                            t.setText(result.getGreetings());
                            Toast.makeText(Home.this, result.getGreetings(), Toast.LENGTH_LONG).show();
                        }
                    }.execute(request);

                }
            });




    }

}