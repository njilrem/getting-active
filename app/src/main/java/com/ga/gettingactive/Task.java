package com.ga.gettingactive;

public class Task {
    private String title;
    private String description;
    private String type;
    private String[] hashtags;

    public Task(){

    }

    public Task(String title, String text){
        this.title = title;
        this.description = text;
    }

    public String getType() {
        return type;
    }

    public String[] getHashtags() {
        return hashtags;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
