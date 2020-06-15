package com.ga.gettingactive.tasklist;

import java.util.ArrayList;

public class TaskContainer {
    private String title;
    private String description;
    private ArrayList<String> hashtags;
    private String tip;

    public TaskContainer(){

    }

    public TaskContainer(String title, String text, String tip){
        this.title = title;
        this.description = text;
        this.tip = tip;
    }

    public String getTip() { return tip; }

    public ArrayList<String> getHashtags() {
        return hashtags;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "TaskContainer{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", hashtags='" + hashtags + '\'' +
                ", tip='" + tip + '\'' +
                '}';
    }
}
