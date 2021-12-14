package com.example.restservice.model;

import java.util.Date;

public class Entry {

	private long id;
	private Date date;
    private String url;
    private String username;
    private String password;

    public Entry() {
        this.id = -1;
        this.date = new Date();
        this.url = "";
        this.username = "";
        this.password = "";
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }    
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
