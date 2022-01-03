package com.example.restservice.model;

public class Role {
    private int user_id;
    private int role;
    
    public Role(int user_id, int role) {
        this.user_id = -1;
        this.role = -1;
    }

    @Override
    public String toString() {
        return "Role [role=" + role + ", user_id=" + user_id + "]";
    }
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public int getRole() {
        return role;
    }
    public void setRole(int role) {
        this.role = role;
    }
    
}
