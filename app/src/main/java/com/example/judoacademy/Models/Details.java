package com.example.judoacademy.Models;

public class Details {
    String name,status;

    public Details(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public Details() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
