package com.example.judoacademy.Models;

public class UserDetails {
    String name, category, clearance, email, key;

    public UserDetails(String name, String category, String clearance, String email, String key) {
        this.name = name;
        this.category = category;
        this.clearance = clearance;
        this.email = email;
        this.key = key;
    }

    public UserDetails() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getClearance() {
        return clearance;
    }

    public void setClearance(String clearance) {
        this.clearance = clearance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
