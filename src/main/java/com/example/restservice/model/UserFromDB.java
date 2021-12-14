package com.example.restservice.model;

public class UserFromDB {
    private long id;
    private String username;
    private String hashpass;
    public UserFromDB(String username, String hashpass) {
        this.id = -1;
        this.username = username;
        this.hashpass = hashpass;
    }
    public UserFromDB() {
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
