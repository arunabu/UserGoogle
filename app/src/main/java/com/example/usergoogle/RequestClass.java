package com.example.usergoogle;

public class RequestClass {
    String name,Lastname,user_id,phoneNumber;
    public RequestClass(String name, String phoneNumber, String user_id, String Lastname) {
        this.name =name;
        this.user_id = user_id;
        this.phoneNumber = phoneNumber;
        this.Lastname = Lastname;

    }

    public RequestClass(String name) {
        this.name = name;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public RequestClass() {
    }
}
