package com.example.usergoogle;

import android.widget.ArrayAdapter;

import com.google.gson.JsonArray;

import java.util.List;

public class ResponseClass {
    String Greetings,user_id,phoneNumber,name,Lastname;


    public ResponseClass(String Greetings) {
        this.Greetings = Greetings;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getGreetings() {
        return Greetings;
    }

    public void setGreetings(String greetings) {
        Greetings = greetings;
    }

    public String getUser_id() {

        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public ResponseClass() {
    }
}
