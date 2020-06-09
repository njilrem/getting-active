package com.ga.gettingactive;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final List<TaskContainer> archive;
    private final List<TaskContainer> myTasks;
    private final List<TaskContainer> tasks;
    private String uid;
    private String firstname;
    private String lastname;


    public User(){
        archive = new ArrayList<>();
        myTasks = new ArrayList<>();
        tasks = new ArrayList<>();
    }

    public User(String uid, String firstname, String lastname) {
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
        archive = new ArrayList<>();
        myTasks = new ArrayList<>();
        tasks = new ArrayList<>();
    }

    public List<TaskContainer> getArchive() {
        return archive;
    }

    public List<TaskContainer> getMyTasks() {
        return myTasks;
    }

    public List<TaskContainer> getTasks() {
        return tasks;
    }

    public String getUid() {
        return uid;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
}
