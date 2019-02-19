package com.example.juanc.parkinglotdemo4.Network;

public class LoginInfo {

    private String email;
    private String password;

    public LoginInfo(String uemail, String upassword) {
        email = uemail;
        password = upassword;
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
}