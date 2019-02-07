package com.example.juanc.parkinglotdemo4.Network;

public class LoginInfo {

    private String email;
    private byte[] password;

    public LoginInfo(String uemail, byte[] upassword) {
        email = uemail;
        password = upassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }
}