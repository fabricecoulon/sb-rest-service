package com.example.restservice.model;

public class User {
    private long id;
    private String username;
    private String hashpass;
    private String password;
    @Override
    public String toString() {
        return "User [hashpass=" + hashpass + ", id=" + id + ", password=" + password + ", username=" + username + "]";
    }
    public User(String username, String hashpass) {
        this.id = -1;
        this.username = username;
        this.hashpass = hashpass;
        this.password = "";
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public User() {
        this.id = -1;
        this.username = "";
        this.hashpass = "";
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getHashpass() {
        return hashpass;
    }
    public void setHashpass(String hashpass) {
        this.hashpass = hashpass;
    }
}
