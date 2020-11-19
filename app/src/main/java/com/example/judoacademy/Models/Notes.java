package com.example.judoacademy.Models;

public class Notes {
    String title,content;

    public Notes(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Notes() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
