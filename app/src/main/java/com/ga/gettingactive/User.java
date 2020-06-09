package com.ga.gettingactive;

public class User {
    private String uid;
    private String firstname;
    private String lastname;


    public User(){
    }

    public User(String uid, String firstname, String lastname) {
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
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
