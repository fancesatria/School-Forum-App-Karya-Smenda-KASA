package com.ukom.kasapengguna.model;

public class LoginModel {
    public String email, password, username;
    public int id;

    public LoginModel(String email, String password) {
        this.email = email;
        this.password = password;
//        this.username = username;
//        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
