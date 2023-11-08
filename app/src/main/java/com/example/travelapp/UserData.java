package com.example.travelapp;

public class UserData {
    private String user_name;
    private String user_id;
    private String user_password;

    UserData(String user_name, String user_id, String user_password) {
        this.user_name = user_name;
        this.user_id = user_id;
        this.user_password = user_password;
    }

    public String getUser_name() { return this.user_name; };

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_id() { return this.user_id; };

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_password() { return this.user_password; };

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }
}
